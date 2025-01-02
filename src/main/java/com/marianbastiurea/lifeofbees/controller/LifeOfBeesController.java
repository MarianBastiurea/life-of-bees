package com.marianbastiurea.lifeofbees.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.marianbastiurea.lifeofbees.game.LifeOfBeesFactory;
import com.marianbastiurea.lifeofbees.game.LifeOfBeesRepository;
import com.marianbastiurea.lifeofbees.game.LifeOfBeesService;
import com.marianbastiurea.lifeofbees.action.ActionOfTheWeek;
import com.marianbastiurea.lifeofbees.bees.Apiary;
import com.marianbastiurea.lifeofbees.bees.HarvestHoney;
import com.marianbastiurea.lifeofbees.bees.Hive;
import com.marianbastiurea.lifeofbees.history.*;
import com.marianbastiurea.lifeofbees.weather.WeatherData;
import com.marianbastiurea.lifeofbees.game.LifeOfBees;
import com.marianbastiurea.lifeofbees.security.JwtTokenProvider;
import com.marianbastiurea.lifeofbees.users.User;
import com.marianbastiurea.lifeofbees.users.UserService;
import com.marianbastiurea.lifeofbees.view.GameRequest;
import com.marianbastiurea.lifeofbees.view.GameResponse;
import com.marianbastiurea.lifeofbees.view.HivesView;
import com.marianbastiurea.lifeofbees.view.HomePageGameResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import com.marianbastiurea.lifeofbees.weather.WeatherService;

import java.security.Principal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.server.ResponseStatusException;


@RestController
@RequestMapping("/api/bees")
public class LifeOfBeesController {
    //TODO delete games field
    private Map<Integer, LifeOfBees> games;
    private final LifeOfBeesRepository lifeOfBeesRepository;
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    private final GameHistoryService gameHistoryService;

    @Autowired
    private LifeOfBeesService lifeOfBeesService;
    @Autowired
    private WeatherService weatherService;


    public LifeOfBeesController(LifeOfBeesRepository lifeOfBeesRepository,
                                UserService userService, JwtTokenProvider jwtTokenProvider,
                                 GameHistoryService gameHistoryService) {
        this.lifeOfBeesRepository = lifeOfBeesRepository;
        this.games = new HashMap<>();
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.gameHistoryService=gameHistoryService;
    }

    @PostMapping("/game")
    public ResponseEntity<Map<String, String>> createGame(@RequestBody GameRequest gameRequest, @RequestHeader("Authorization") String authorizationHeader, Authentication authentication) {
        System.out.println("Received request to create game: " + gameRequest);
        String jwtToken = authorizationHeader.replace("Bearer ", "");
        LocalDate startDate = LocalDate.parse(gameRequest.getStartDate());
        WeatherData weatherData =weatherService.getWeatherData(startDate);
        String userIdFromToken;
        try {
            userIdFromToken = jwtTokenProvider.extractUserId(jwtToken);
            System.out.println("acesta e userId din Token" + userIdFromToken);
        } catch (Exception e) {
            System.out.println("An error occurred while extracting username: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid token"));
        }
        if (!userIdFromToken.equals(gameRequest.getUserId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "Token does not match userId in createGame"));
        }
        User user =userService.getUser(gameRequest);

        LifeOfBees lifeOfBeesGame = LifeOfBeesFactory.createLifeOfBeesGame(
                gameRequest.getGameName(),
                gameRequest.getLocation(),
                gameRequest.getStartDate(),
                gameRequest.getNumberOfStartingHives(),
                gameRequest.getUserId(),
                gameRequest.getGameType(),
                weatherData
        );

        LifeOfBees savedGame = lifeOfBeesRepository.save(lifeOfBeesGame);
        userService.addGameToUser(user, lifeOfBeesGame.getGameId());
        gameHistoryService.saveGameHistory(savedGame);
        Map<String, String> response = new HashMap<>();
        response.put("token", jwtToken);
        response.put("gameId", savedGame.getGameId());
        return ResponseEntity.ok(response);
    }


    @GetMapping("/game/{gameId}")
    public GameResponse getGame(@PathVariable String gameId, Principal principal) {
        System.out.println("Cerere pentru gameId: " + gameId);
        LifeOfBees lifeOfBeesGame = getByGameId(gameId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Game not found"));
        String userId = principal.getName();

        //Consider extracting a method for Access denied validation
        if (!lifeOfBeesGame.getUserId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied");
        }
        GameResponse response = getGameResponse(lifeOfBeesGame);
        System.out.println("Date trimise către React: " + response);
        return response;
    }

    private Optional<LifeOfBees> getByGameId(String gameId) {
        return lifeOfBeesRepository.findByGameId(gameId);
    }

    @PostMapping("/iterate/{gameId}")
    public GameResponse iterateWeek(@PathVariable String gameId, Principal principal) {
        System.out.println("Cerere pentru iterație gameId: " + gameId);
        LifeOfBees lifeOfBeesGame = getByGameId(gameId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Game not found"));
        System.out.println("Acesta e jocul primit in Iterate:" + lifeOfBeesGame);
        String userId = principal.getName();
        if (!lifeOfBeesGame.getUserId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied");
        }
        //TOOD
        //List<WeatherData> weatherDataNextWeek = weatherService.getWeatherForNextWeek(lifeOfBeesGame.getCurrentDate());
//        Map<String, WeatherData> allWeatherData = new HashMap<>();
//        allWeatherData.put(weatherData.getDate().toString(), weatherData);
//

        lifeOfBeesGame = lifeOfBeesGame.iterateOneWeek(lifeOfBeesGame, lifeOfBeesService);
        lifeOfBeesRepository.save(lifeOfBeesGame);
        GameResponse response = getGameResponse(lifeOfBeesGame);
        gameHistoryService.addGameInGameHistory(lifeOfBeesGame);

        return response;
    }

    //TODO remove submit actions of the week button
    @PostMapping("/submitActionsOfTheWeek/{gameId}")
    public GameResponse submitActionsOfTheWeek(
            @PathVariable String gameId,
            @RequestBody List<ActionOfTheWeek> approvedActions,
            Principal principal) {

        System.out.println("Cerere pentru submitActionsOfTheWeek, gameId: " + gameId);
        LifeOfBees lifeOfBeesGame = getByGameId(gameId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Game not found"));
        System.out.println("Acesta e jocul primit in action Of the week:" + lifeOfBeesGame);
        String userId = principal.getName();
        if (!lifeOfBeesGame.getUserId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied");
        }
        Apiary apiary = lifeOfBeesGame.getApiary();
        for (ActionOfTheWeek action : approvedActions) {
            processAction(action, apiary, lifeOfBeesGame);
        }
        lifeOfBeesGame.getActionsOfTheWeek().getActions().clear();
        lifeOfBeesRepository.save(lifeOfBeesGame);
        GameResponse response = getGameResponse(lifeOfBeesGame);
        System.out.println("GameResponse după submitActionsOfTheWeek: " + response);
        return response;
    }

    private void processAction(ActionOfTheWeek action, Apiary apiary, LifeOfBees lifeOfBeesGame) {
        switch (action.getActionType()) {
            case "ADD_EGGS_FRAME":
                List<Integer> eggHiveIds = (List<Integer>) action.getData().get("hiveIds");
                if (eggHiveIds != null) {
                    eggHiveIds.forEach(hiveId -> {
                        Hive hive = apiary.getHiveById(hiveId);
                        if (hive != null) {
                            hive.addNewEggsFrameInHive();
                        }
                    });
                }
                break;

            case "ADD_HONEY_FRAME":
                List<Integer> honeyHiveIds = (List<Integer>) action.getData().get("hiveIds");
                if (honeyHiveIds != null) {
                    honeyHiveIds.forEach(hiveId -> {
                        Hive hive = apiary.getHiveById(hiveId);
                        if (hive != null) {
                            hive.addNewHoneyFrameInHive();
                        }
                    });
                }
                break;

            case "MOVE_EGGS_FRAME":
                List<List<Integer>> hiveIdPairs = (List<List<Integer>>) action.getData().get("hiveIdPairs");
                if (hiveIdPairs != null) {
                    apiary.moveAnEggsFrame(hiveIdPairs);
                }
                break;

            case "FEED_BEES":
                Map<String, Object> feedBeesData = (Map<String, Object>) action.getData();
                String feedBeesAnswer = (String) feedBeesData.get("answer");
                apiary.doFeedBees(feedBeesAnswer, lifeOfBeesGame);
                break;

            case "SPLIT_HIVE":
                List<Integer> splitHiveIds = (List<Integer>) action.getData().get("hiveIds");
                if (splitHiveIds != null) {
                    splitHiveIds.forEach(hiveId -> {
                        Hive hive = apiary.getHiveById(hiveId);
                        if (hive != null) {
                            apiary.splitHive(hive);
                        }
                    });
                }
                break;

            case "INSECT_CONTROL":
                Map<String, Object> insectControlData = (Map<String, Object>) action.getData();
                String insectControlResponse = (String) insectControlData.get("answer");
                apiary.doInsectControl(insectControlResponse, lifeOfBeesGame);
                break;

            default:
                System.out.println("Unknown action type: " + action.getActionType());
                break;
        }
    }


    public GameResponse getGameResponse(LifeOfBees game) {
        GameResponse gameResponse = new GameResponse();
        gameResponse.setId(game.getGameId());
        for (Hive hive : game.getApiary().getHives()) {
            gameResponse.getHives().add(new HivesView(hive.getId(), hive.getAgeOfQueen(), hive.getEggFrames().getNumberOfEggFrames(), hive.getHoneyFrames().getNumberOfHoneyFrames(), hive.isItWasSplit()));
        }
        gameResponse.setTemperature(game.getWeatherData().getTemperature());
        //gameResponse.setActionsOfTheWeek(game.getActionsOfTheWeek());
        gameResponse.setWindSpeed(game.getWeatherData().getWindSpeed());
        gameResponse.setMoneyInTheBank(game.getMoneyInTheBank());
        gameResponse.setPrecipitation(game.getWeatherData().getPrecipitation());
        gameResponse.setCurrentDate(game.getCurrentDate());
        gameResponse.setTotalKgOfHoneyHarvested(game.getTotalKgOfHoneyHarvested());
        System.out.println("acestea sunt datele trimise catre React: " + gameResponse);
        return gameResponse;
    }


    @GetMapping("/getHoneyQuantities/{gameId}")
    public ResponseEntity<HarvestHoney> getHoneyQuantities(@PathVariable String gameId, Principal principal) {
        System.out.println("Cerere pentru getHoneyQuantities, gameId: " + gameId);
        LifeOfBees lifeOfBeesGame = getByGameId(gameId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Game not found"));
        System.out.println("Acesta e jocul primit in getHoney:" + lifeOfBeesGame);
        String userId = principal.getName();
        if (!lifeOfBeesGame.getUserId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied");
        }
        Apiary apiary = lifeOfBeesGame.getApiary();
        apiary.honeyHarvestedByHoneyType();
        HarvestHoney honeyData = apiary.getTotalHarvestedHoney();
        System.out.println("Cantitățile de miere trimise către client: " + honeyData);
        return ResponseEntity.ok(honeyData);
    }

    @PostMapping("/sellHoney/{gameId}")
    public ResponseEntity<String> sendSellHoneyQuantities(
            @PathVariable String gameId,
            @RequestBody Map<String, Double> requestData,
            Principal principal) {
        System.out.println("Cerere pentru vânzare miere, gameId: " + gameId);
        LifeOfBees lifeOfBeesGame = getByGameId(gameId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Game not found"));
        System.out.println("Acesta e jocul primit in sellHoney:" + lifeOfBeesGame);
        String userId = principal.getName();
        if (!lifeOfBeesGame.getUserId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied");
        }
        double revenue = requestData.getOrDefault("totalValue", 0.0);
        HarvestHoney soldHoneyData = new HarvestHoney();

        for (Map.Entry<String, Double> entry : requestData.entrySet()) {
            if (!entry.getKey().equalsIgnoreCase("totalValue")) {
                switch (entry.getKey().toLowerCase()) {
                    case "acacia":
                        soldHoneyData.Acacia = entry.getValue();
                        break;
                    case "rapeseed":
                        soldHoneyData.Rapeseed = entry.getValue();
                        break;
                    case "wildflower":
                        soldHoneyData.WildFlower = entry.getValue();
                        break;
                    case "linden":
                        soldHoneyData.Linden = entry.getValue();
                        break;
                    case "sunflower":
                        soldHoneyData.SunFlower = entry.getValue();
                        break;
                    case "falseindigo":
                        soldHoneyData.FalseIndigo = entry.getValue();
                        break;
                    default:
                        System.out.println("Tip de miere invalid: " + entry.getKey());
                        return ResponseEntity.badRequest().body("Invalid honey type: " + entry.getKey());
                }
            }
        }
        Apiary apiary = lifeOfBeesGame.getApiary();
        apiary.updateHoneyStock(soldHoneyData);
        lifeOfBeesGame.setTotalKgOfHoneyHarvested(apiary.getTotalKgHoneyHarvested());
        lifeOfBeesGame.setMoneyInTheBank(lifeOfBeesGame.getMoneyInTheBank() + revenue);
        lifeOfBeesRepository.save(lifeOfBeesGame);
        System.out.println("Stock și venituri actualizate pentru gameId: " + gameId);
        return ResponseEntity.ok("Stock and revenue updated successfully.");
    }


    @PostMapping("/buyHives/{gameId}")
    public ResponseEntity<String> buyHives(
            @PathVariable String gameId,
            @RequestBody Map<String, Integer> request,
            Principal principal) {
        System.out.println("Cerere pentru cumpărare stupi, gameId: " + gameId);
        LifeOfBees lifeOfBeesGame = getByGameId(gameId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Game not found"));
        System.out.println("Acesta e jocul primit in buyHives:" + lifeOfBeesGame);
        String userId = principal.getName();
        if (!lifeOfBeesGame.getUserId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied");
        }
        Integer numberOfHives = request.get("numberOfHives");
        if (numberOfHives == null || numberOfHives <= 0) {
            return ResponseEntity.badRequest().body("Invalid number of hives.");
        }
        Apiary apiary = lifeOfBeesGame.getApiary();
        Hive.addHivesToApiary(apiary.createHive(numberOfHives, lifeOfBeesGame.getCurrentDate()), lifeOfBeesGame);
        double totalCost = numberOfHives * 500;
        if (lifeOfBeesGame.getMoneyInTheBank() < totalCost) {
            return ResponseEntity.badRequest().body("Insufficient funds to buy hives.");
        }
        lifeOfBeesGame.setMoneyInTheBank(lifeOfBeesGame.getMoneyInTheBank() - totalCost);
        lifeOfBeesRepository.save(lifeOfBeesGame);
        System.out.println("Stupi cumpărați cu succes pentru gameId: " + gameId);
        return ResponseEntity.ok("Hives bought successfully.");
    }

    @GetMapping("/JohnDoeGames")
    public List<HomePageGameResponse> getJohnDoeGames() {
        List<LifeOfBees> userGames = lifeOfBeesService.getGamesForJohnDoe();
        if (userGames.isEmpty()) {
            System.out.println("Nu s-au găsit jocuri pentru utilizatorul JohnDoe.");
        } else {
            System.out.println("Jocuri găsite pentru utilizatorul JohnDoe: " + userGames);
        }
        return userGames.stream()
                .map(game -> {
                    System.out.println("Total Honey Harvested (before): " + game.getTotalKgOfHoneyHarvested());
                    System.out.println("Money in the Bank (before): " + game.getMoneyInTheBank());
                    return new HomePageGameResponse(
                            game.getGameName(),
                            game.getLocation(),
                            game.getApiary().getHives().size(),
                            game.getMoneyInTheBank(),
                            game.getTotalKgOfHoneyHarvested(),
                            game.getGameId()
                    );
                })
                .collect(Collectors.toList());

    }

    @GetMapping("/gamesForUser")
    public List<HomePageGameResponse> getGamesForUserByType(
            @RequestParam String userId,
            @RequestParam(required = false) String gameType) {
        System.out.println("solicitare jocuri pentru userId: " + userId);
        List<LifeOfBees> userGames = lifeOfBeesService.getGamesForUserByType(userId, gameType);
        System.out.println("jocurile gasite pentru userId: " + userGames);
        if (gameType != null) {
            userGames = userGames.stream()
                    .filter(game -> game.getGameType().equalsIgnoreCase(gameType))
                    .collect(Collectors.toList());
        }
        if (userGames.isEmpty()) {
            System.out.println("Nu s-au găsit jocuri pentru utilizatorul cu userId: " + userId
                    + (gameType != null ? " și tipul de joc: " + gameType : "."));
        } else {
            System.out.println("Jocuri găsite pentru utilizatorul cu userId: " + userId
                    + (gameType != null ? " și tipul de joc: " + gameType : "")
                    + ": " + userGames);
        }
        return userGames.stream()
                .map(game -> new HomePageGameResponse(
                        game.getGameName(),
                        game.getLocation(),
                        game.getApiary().getHives().size(),
                        game.getMoneyInTheBank(),
                        game.getTotalKgOfHoneyHarvested(),
                        game.getGameId()
                ))
                .collect(Collectors.toList());
    }

    @GetMapping("/HiveHistory/{gameId}")
    public List<HiveHistory> getHiveHistory(@PathVariable String gameId,
                                            @RequestParam("hiveId") Integer hiveId,
                                            Principal principal) {
        System.out.println("Cerere pentru istoricul stupilor in jocul cu gameId: " + gameId);
        GameHistory gameHistory = gameHistoryService.findGameBygameId(gameId);

        List<HiveHistory> hiveHistories = new ArrayList<>();
        for (LifeOfBees game : gameHistory.getGamesHistory()) {
            HiveHistory hiveHistory = new HiveHistory();
            hiveHistory.setCurrentDate(game.getCurrentDate());
            hiveHistory.setWeatherData(game.getWeatherData());
            hiveHistory.setMoneyInTheBank(game.getMoneyInTheBank());
            hiveHistory.setHive(game.getApiary().getHiveById(hiveId));
            hiveHistories.add(hiveHistory);
        }
        System.out.println("acesta sunt datele trimise catre HistoryHive:"+hiveHistories);
        return hiveHistories;
    }

    @GetMapping("/apiaryHistory/{gameId}")
    public ResponseEntity<List<ApiaryHistory>> getApiaryHistory(@PathVariable String gameId) throws JsonProcessingException {

        GameHistory gameHistory =gameHistoryService.findGameBygameId(gameId);
        List<ApiaryHistory> apiaryHistories = new ArrayList<>();

        for (LifeOfBees game : gameHistory.getGamesHistory()) {
            Apiary apiary = game.getApiary();
            ApiaryHistory apiaryHistory = new ApiaryHistory();
            apiaryHistory.setCurrentDate(game.getCurrentDate());
            apiaryHistory.setWeatherData(game.getWeatherData());
            apiaryHistory.setMoneyInTheBank(game.getMoneyInTheBank());
            apiaryHistory.setTotalKgOfHoneyHarvested(game.getTotalKgOfHoneyHarvested());
            List<String> formattedActions = game.getActionOfTheWeek().stream()
                    .filter(action -> {
                        List<Integer> hiveIds = (List<Integer>) action.getData().get("hiveIds");
                        return hiveIds != null && !hiveIds.isEmpty();
                    })
                    .map(action -> {
                        String actionType = action.getActionType().replace("_", " ").toLowerCase();
                        String hiveIds = ((List<Integer>) action.getData().get("hiveIds"))
                                .stream()
                                .map(Object::toString)
                                .collect(Collectors.joining(", "));
                        return actionType + " in hive(s) " + hiveIds;
                    })
                    .collect(Collectors.toList());

            apiaryHistory.setActionOfTheWeek(formattedActions);
            apiaryHistory.setHive(apiary.getHives());
            apiaryHistories.add(apiaryHistory);
            System.out.println("acesta e obiectul ApiaryHistory:" + apiaryHistory);

        }
        return ResponseEntity.ok(apiaryHistories);
    }

}


