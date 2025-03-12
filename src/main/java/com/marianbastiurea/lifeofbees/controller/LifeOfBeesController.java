package com.marianbastiurea.lifeofbees.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.marianbastiurea.lifeofbees.action.ActionType;
import com.marianbastiurea.lifeofbees.bees.*;
import com.marianbastiurea.lifeofbees.game.LifeOfBees;
import com.marianbastiurea.lifeofbees.game.LifeOfBeesFactory;
import com.marianbastiurea.lifeofbees.game.LifeOfBeesService;
import com.marianbastiurea.lifeofbees.history.ApiaryHistory;
import com.marianbastiurea.lifeofbees.history.GameHistory;
import com.marianbastiurea.lifeofbees.history.GameHistoryService;
import com.marianbastiurea.lifeofbees.history.HiveHistory;
import com.marianbastiurea.lifeofbees.security.JwtTokenProvider;
import com.marianbastiurea.lifeofbees.time.BeeTime;
import com.marianbastiurea.lifeofbees.users.User;
import com.marianbastiurea.lifeofbees.users.UserService;
import com.marianbastiurea.lifeofbees.view.GameRequest;
import com.marianbastiurea.lifeofbees.view.GameResponse;
import com.marianbastiurea.lifeofbees.view.HivesView;
import com.marianbastiurea.lifeofbees.view.HomePageGameResponse;
import com.marianbastiurea.lifeofbees.weather.WeatherData;
import com.marianbastiurea.lifeofbees.weather.WeatherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.marianbastiurea.lifeofbees.bees.ApiaryParameters.priceOfAHive;


@RestController
@RequestMapping("/api/bees")
public class LifeOfBeesController {
    private static final Logger logger = LoggerFactory.getLogger(LifeOfBeesController.class);


    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    private final GameHistoryService gameHistoryService;

    @Autowired
    private LifeOfBeesService lifeOfBeesService;
    @Autowired
    private WeatherService weatherService;


    public LifeOfBeesController(
            UserService userService, JwtTokenProvider jwtTokenProvider,
            GameHistoryService gameHistoryService) {
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.gameHistoryService = gameHistoryService;
    }

    private static void accessDenied(LifeOfBees lifeOfBeesGame, String userId) {
        if (!lifeOfBeesGame.getUserId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied");
        }
    }

    @PostMapping("/game")
    public ResponseEntity<Map<String, String>> createGame(@RequestBody GameRequest gameRequest, @RequestHeader("Authorization") String authorizationHeader) {
        logger.info("Received request to create game: {}", gameRequest);
        String jwtToken = authorizationHeader.replace("Bearer ", "");
        BeeTime startDate = new BeeTime(gameRequest.getStartDate());
        WeatherData weatherData = weatherService.getWeatherData(startDate);
        String userIdFromToken;
        try {
            userIdFromToken = jwtTokenProvider.extractUserId(jwtToken);
            System.out.println("userId din Token" + userIdFromToken);
        } catch (Exception e) {
            System.out.println("An error occurred while extracting username: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid token"));
        }
        if (!userIdFromToken.equals(gameRequest.getUserId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "Token does not match userId in createGame"));
        }
        User user = userService.getUser(gameRequest);

        LifeOfBees lifeOfBeesGame = LifeOfBeesFactory.createLifeOfBeesGame(
                gameRequest.getGameName(),
                gameRequest.getLocation(),
                gameRequest.getStartDate(),
                gameRequest.getNumberOfStartingHives(),
                gameRequest.getUserId(),
                gameRequest.getGameType(),
                weatherData
        );

        LifeOfBees savedGame = lifeOfBeesService.save(lifeOfBeesGame);
        gameHistoryService.saveGameHistory(savedGame);
        Map<String, String> response = new HashMap<>();
        response.put("token", jwtToken);
        response.put("gameId", savedGame.getGameId());
        logger.info("Game created successfully: {}", response);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/game/{gameId}")
    public GameResponse getGame(@PathVariable String gameId, Principal principal) {
        logger.info("Received request for game: {}", gameId);
        LifeOfBees lifeOfBeesGame = lifeOfBeesService.getByGameId(gameId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Game not found"));
        String gameType= lifeOfBeesGame.getGameType();
        if ("private".equals(gameType)){
        String userId = principal.getName();
        accessDenied(lifeOfBeesGame, userId);
        }
        GameResponse response = getGameResponse(lifeOfBeesGame);
        logger.info("Game sent to React from getGame: {}", response);
        return response;
    }

    @PostMapping("/iterate/{gameId}")
    public GameResponse iterateWeek(
            @PathVariable String gameId,
            @RequestBody Map<String, Map<ActionType, Object>> requestData,
            Principal principal) {
        logger.info("Received request for iterate game: {}", gameId);
        LifeOfBees lifeOfBeesGame = lifeOfBeesService.getByGameId(gameId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Game not found"));
        List<WeatherData> weatherDataNextWeek = weatherService.getWeatherForNextWeek(lifeOfBeesGame.getApiary().getHives().getCurrentDate());
        String gameType= lifeOfBeesGame.getGameType();
        if ("private".equals(gameType)){
            String userId = principal.getName();
            accessDenied(lifeOfBeesGame, userId);
        }
        Map<ActionType, Object> actions = requestData.get("actions");
        if (actions == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing 'actions' data in request");
        }
        lifeOfBeesGame.iterateOneWeek(actions, weatherDataNextWeek);
        lifeOfBeesService.save(lifeOfBeesGame);
        GameResponse response = getGameResponse(lifeOfBeesGame);
        gameHistoryService.saveGameHistory(lifeOfBeesGame);
        logger.info("Game sent to React from iterateWeek: {}", response);
        return response;
    }


    public GameResponse getGameResponse(LifeOfBees game) {
        logger.info("game received in GameResponse: {}", game);
        GameResponse gameResponse = new GameResponse();
        gameResponse.setId(game.getGameId());
        for (Hive hive : game.getApiary().getHives().getHives()) {
            gameResponse.getHives().add(new HivesView(hive.getId(), hive.getAgeOfQueen(), hive.getEggFrames().getNumberOfEggFrames(), hive.getHoneyFrames().getHoneyFrame().size(), hive.isItWasSplit()));
        }
        gameResponse.setTemperature(game.getWeatherData().getTemperature());
        gameResponse.setActions(game.getActionsOfTheWeek());
        gameResponse.setWindSpeed(game.getWeatherData().getWindSpeed());
        gameResponse.setMoneyInTheBank(game.getMoneyInTheBank());
        gameResponse.setPrecipitation(game.getWeatherData().getPrecipitation());
        gameResponse.setCurrentDate(game.getApiary().getHives().getCurrentDate().getLocalDate());
        gameResponse.setTotalKgOfHoneyHarvested(game.getTotalKgOfHoneyHarvested());
        gameResponse.setRemovedHiveId(game.getRemovedHiveId());
        logger.info("Data saved it GameResponse:  {}", gameResponse);
        return gameResponse;
    }


    @GetMapping("/getHoneyQuantities/{gameId}")
    public ResponseEntity<HarvestHoney> getHoneyQuantities(@PathVariable String gameId, Principal principal) {
        logger.info("Request for honey harvested in game:  {}", gameId);
        LifeOfBees lifeOfBeesGame = lifeOfBeesService.getByGameId(gameId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Game not found"));
        logger.info("This is the game received to extract honey harvested: {}", lifeOfBeesGame);
        String gameType= lifeOfBeesGame.getGameType();
        if ("private".equals(gameType)){
            String userId = principal.getName();
            accessDenied(lifeOfBeesGame, userId);
        }
        Apiary apiary = lifeOfBeesGame.getApiary();
        apiary.honeyHarvestedByHoneyType();
        HarvestHoney honeyData = apiary.getTotalHarvestedHoney();
        logger.info("Response for getHoneyQuantities:  {}", honeyData);
        return ResponseEntity.ok(honeyData);
    }

    @PostMapping("/sellHoney/{gameId}")
    public ResponseEntity<String> sendSellHoneyQuantities(
            @PathVariable String gameId,
            @RequestBody SellHoney requestData,
            Principal principal) {

        LifeOfBees lifeOfBeesGame = lifeOfBeesService.getByGameId(gameId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Game not found"));

        String gameType= lifeOfBeesGame.getGameType();
        if ("private".equals(gameType)){
            String userId = principal.getName();
            accessDenied(lifeOfBeesGame, userId);
        }
        double revenue = 0.0;
        if (requestData.totalValue != null) {
            revenue = Double.parseDouble(requestData.totalValue.toString());
        }
        Map<HoneyType, Double> honeyTypeToAmount = requestData.honeyTypeToAmount;
        if (honeyTypeToAmount == null || honeyTypeToAmount.isEmpty()) {
            return ResponseEntity.badRequest().body("No honey quantities provided for selling.");
        }
        HarvestHoney soldHoneyData = new HarvestHoney();
        for (Map.Entry<HoneyType, Double> entry : honeyTypeToAmount.entrySet()) {
            HoneyType honeyType = entry.getKey();
            soldHoneyData.setHoneyAmount(honeyType, entry.getValue());
        }
        Apiary apiary = lifeOfBeesGame.getApiary();
        apiary.updateHoneyStock(soldHoneyData);
        lifeOfBeesGame.setTotalKgOfHoneyHarvested(apiary.getTotalKgHoneyHarvested());
        lifeOfBeesGame.setMoneyInTheBank(lifeOfBeesGame.getMoneyInTheBank() + revenue);
        lifeOfBeesService.save(lifeOfBeesGame);
        return ResponseEntity.ok("Stock and revenue updated successfully.");
    }

    @PostMapping("/buyHives/{gameId}")
    public ResponseEntity<String> buyHives(
            @PathVariable String gameId,
            @RequestBody Map<String, Integer> request,
            Principal principal) {
        logger.info("Request to buy hives in game:  {}", gameId);
        LifeOfBees lifeOfBeesGame = lifeOfBeesService.getByGameId(gameId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Game not found"));
        String gameType= lifeOfBeesGame.getGameType();
        if ("private".equals(gameType)){
            String userId = principal.getName();
            accessDenied(lifeOfBeesGame, userId);
        }
        Integer numberOfHives = request.get("numberOfHives");
        int totalCost = numberOfHives * priceOfAHive;
        if (totalCost < lifeOfBeesGame.getMoneyInTheBank()) {
            Apiary apiary = lifeOfBeesGame.getApiary();
            apiary.getHives().addNewHivesToHives(apiary.getHives().createHives(numberOfHives, apiary.getHives().getCurrentDate()), lifeOfBeesGame);
            lifeOfBeesGame.setMoneyInTheBank(lifeOfBeesGame.getMoneyInTheBank() - totalCost);
            lifeOfBeesService.save(lifeOfBeesGame);
        }
        logger.info("new {} hives was added to apiary in game: {}", numberOfHives, gameId);
        return ResponseEntity.ok("Hives bought successfully.");
    }



    @GetMapping("/PublicGames")
    public List<HomePageGameResponse> getPublicGames(@RequestParam String gameType) {
        List<LifeOfBees> publicGames = lifeOfBeesService.getPublicGames(gameType);

        if (publicGames.isEmpty()) {
            logger.info("No public games");
        } else {
            logger.info("public games: {}", publicGames);
        }

        return publicGames.stream()
                .map(game -> new HomePageGameResponse(
                        game.getGameName(),
                        game.getLocation(),
                        game.getApiary().getHives().getHives().size(),
                        game.getMoneyInTheBank(),
                        game.getTotalKgOfHoneyHarvested(),
                        game.getGameId()
                ))
                .collect(Collectors.toList());
    }

    @GetMapping("/gamesForUser")
    public List<HomePageGameResponse> getGamesForUserByType(
            @RequestParam String userId,
            @RequestParam(required = false) String gameType) {
        List<LifeOfBees> userGames = lifeOfBeesService.getGamesForUserByType(userId, gameType);
        if (gameType != null) {
            userGames = userGames.stream()
                    .filter(game -> game.getGameType().equalsIgnoreCase(gameType))
                    .toList();
        }
        if (userGames.isEmpty()) {
            logger.info("No games found for userId: {}{}", userId,
                    gameType != null ? " game type: " + gameType : ".");

        } else {
            logger.info("Games found for userId: " + userId
                    + (gameType != null ? " game type: " + gameType : "")
                    + ": " + userGames);
        }
        return userGames.stream()
                .map(game -> new HomePageGameResponse(
                        game.getGameName(),
                        game.getLocation(),
                        game.getApiary().getHives().getHives().size(),
                        game.getMoneyInTheBank(),
                        game.getTotalKgOfHoneyHarvested(),
                        game.getGameId()
                ))
                .collect(Collectors.toList());
    }

    @GetMapping("/HiveHistory/{gameId}")
    public List<HiveHistory> getHiveHistory(@PathVariable String gameId,
                                            @RequestParam("hiveId") Integer hiveId) {
        logger.info("Request to show history for a hive from game:  {}", gameId);
        List<GameHistory> gameHistory = gameHistoryService.findGameHistoriesByGameId(gameId);
        List<HiveHistory> hiveHistories = new ArrayList<>();
        for (GameHistory game : gameHistory) {
            LifeOfBees lifeOfBees = game.getGameHistory();
            HiveHistory hiveHistory = new HiveHistory();
            hiveHistory.setCurrentDate(lifeOfBees.getCurrentDate());
            hiveHistory.setWeatherData(lifeOfBees.getWeatherData());
            hiveHistory.setMoneyInTheBank(lifeOfBees.getMoneyInTheBank());
            hiveHistory.setHive(lifeOfBees.getApiary().getHives().getHiveById(hiveId));
            hiveHistories.add(hiveHistory);
        }
        logger.info("history of {} hive was send it to game: {}", hiveId, gameId);
        return hiveHistories;
    }

    @GetMapping("/apiaryHistory/{gameId}")
    public ResponseEntity<List<ApiaryHistory>> getApiaryHistory(@PathVariable String gameId) throws JsonProcessingException {

        logger.info("Request to show history of apiary from game:  {}", gameId);
        List<GameHistory> gameHistory = gameHistoryService.findGameHistoriesByGameId(gameId);
        List<ApiaryHistory> apiaryHistories = new ArrayList<>();
        for (GameHistory game : gameHistory) {
            LifeOfBees lifeOfBeesGame = game.getGameHistory();
            Apiary apiary = lifeOfBeesGame.getApiary();
            ApiaryHistory apiaryHistory = new ApiaryHistory();
            apiaryHistory.setCurrentDate(lifeOfBeesGame.getCurrentDate());
            apiaryHistory.setWeatherData(lifeOfBeesGame.getWeatherData());
            apiaryHistory.setMoneyInTheBank(lifeOfBeesGame.getMoneyInTheBank());
            apiaryHistory.setTotalKgOfHoneyHarvested(lifeOfBeesGame.getTotalKgOfHoneyHarvested());
            apiaryHistory.setActionsOfTheWeek(lifeOfBeesGame.getActionsOfTheWeek());
            Hives hives = new Hives();
            hives.setHives(apiary.getHives().getHives());
            apiaryHistory.setHives(hives);
            apiaryHistories.add(apiaryHistory);
        }
        logger.info("Apiary's history was sent to React for game: {}", gameId);
        return ResponseEntity.ok(apiaryHistories);
    }

    @DeleteMapping("deleteGame/{gameId}")
    public ResponseEntity<String> deleteGame(@PathVariable String gameId) {
        try {
            boolean gameExistsInMain = lifeOfBeesService.existsById(gameId);
            boolean gameExistsInHistory = gameHistoryService.existsByGameId(gameId);
            if (!gameExistsInMain && !gameExistsInHistory) {
                throw new IllegalArgumentException("Game with ID " + gameId + " does not exist in any collection.");
            }

            if (gameExistsInMain) {
                lifeOfBeesService.deleteGameById(gameId);
            }

            if (gameExistsInHistory) {
                gameHistoryService.deleteGameById(gameId);
            }

            return ResponseEntity.ok("Game deleted successfully from relevant collections.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while deleting the game.");
        }
    }


    static class SellHoney {
        public Map<HoneyType, Double> honeyTypeToAmount;
        public Double totalValue;

        public SellHoney() {
        }
    }
}


