package com.marianbastiurea.lifeofbees;

import com.marianbastiurea.lifeofbees.Security.JwtTokenProvider;
import com.marianbastiurea.lifeofbees.Users.User;
import com.marianbastiurea.lifeofbees.Users.UserRepository;
import com.marianbastiurea.lifeofbees.Users.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

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

    private Map<Integer, LifeOfBees> games;
    @Autowired
    private RestTemplate restTemplate;
    private final LifeOfBeesRepository lifeOfBeesRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    @Autowired
    private LifeOfBeesService lifeOfBeesService;


    public LifeOfBeesController(LifeOfBeesRepository lifeOfBeesRepository, UserRepository userRepository, UserService userService, JwtTokenProvider jwtTokenProvider) {
        this.lifeOfBeesRepository = lifeOfBeesRepository;
        this.games = new HashMap<>();
        this.userRepository = userRepository;
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/game")
    public ResponseEntity<Map<String, String>> createGame(@RequestBody GameRequest gameRequest, @RequestHeader("Authorization") String authorizationHeader, Authentication authentication) {
        System.out.println("Received request to create game: " + gameRequest);
        String jwtToken = authorizationHeader.replace("Bearer ", "");
        LocalDate startDate = LocalDate.parse(gameRequest.getStartDate());
        String weatherApiUrl = "http://localhost:8081/api/weather/" + startDate;
        WeatherData weatherData = restTemplate.getForObject(weatherApiUrl, WeatherData.class);
        Map<String, WeatherData> allWeatherData = new HashMap<>();
        allWeatherData.put(weatherData.getDate().toString(), weatherData);
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

        User user = userRepository.findById(gameRequest.getUserId()).orElse(null);


        if (user == null) {
            user = new User();
            user.setUsername(gameRequest.getUsername());
            user.setGamesList(new ArrayList<>());
            userRepository.save(user);
            System.out.println("Utilizator creat cu numele in createGame: " + user);
        } else {
            System.out.println("Utilizator găsit in createGame: " + user);
        }

        LifeOfBees lifeOfBeesGame = LifeOfBeesFactory.createLifeOfBeesGame(
                gameRequest.getGameName(),
                gameRequest.getLocation(),
                gameRequest.getStartDate(),
                gameRequest.getNumberOfStartingHives(),
                gameRequest.getUserId(),
                gameRequest.getGameType(),
                allWeatherData
        );

        LifeOfBees savedGame = lifeOfBeesRepository.save(lifeOfBeesGame);
        System.out.println("jocul nou creat este :" + lifeOfBeesGame);
        userService.addGameToUser(user, lifeOfBeesGame.getGameId());
        user = userRepository.findById(user.getUserId()).orElseThrow();
        System.out.println("Userid după salvare în createGame: " + user.getUserId());
        Map<String, String> response = new HashMap<>();
        response.put("token", jwtToken);
        response.put("gameId", savedGame.getGameId());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/game/{gameId}")
    public GameResponse getGame(@PathVariable String gameId, Principal principal) {
        System.out.println("Cerere pentru gameId: " + gameId);
        LifeOfBees lifeOfBeesGame = lifeOfBeesRepository.findByGameId(gameId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Game not found"));
        String userId = principal.getName();
        if (!lifeOfBeesGame.getUserId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied");
        }
        GameResponse response = getGameResponse(lifeOfBeesGame);
        System.out.println("Date trimise către React: " + response);
        return response;
    }

    @PostMapping("/iterate/{gameId}")
    public GameResponse iterateWeek(@PathVariable String gameId, Principal principal) {
        System.out.println("Cerere pentru iterație gameId: " + gameId);
        LifeOfBees lifeOfBeesGame = lifeOfBeesRepository.findByGameId(gameId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Game not found"));
        System.out.println("Acesta e jocul primit in Iterate:" + lifeOfBeesGame);
        String userId = principal.getName();
        if (!lifeOfBeesGame.getUserId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied");
        }
        lifeOfBeesGame = lifeOfBeesGame.iterateOneWeek(lifeOfBeesGame, lifeOfBeesService);
        lifeOfBeesRepository.save(lifeOfBeesGame);
        GameResponse response = getGameResponse(lifeOfBeesGame);
        System.out.println("GameResponse după iterație: " + response);
        return response;
    }

    @PostMapping("/submitActionsOfTheWeek/{gameId}")
    public GameResponse submitActionsOfTheWeek(
            @PathVariable String gameId,
            @RequestBody List<ActionOfTheWeek> approvedActions,
            Principal principal) {

        System.out.println("Cerere pentru submitActionsOfTheWeek, gameId: " + gameId);
        LifeOfBees lifeOfBeesGame = lifeOfBeesRepository.findByGameId(gameId)
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
        lifeOfBeesGame.getActionOfTheWeek().clear();
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
            gameResponse.getHives().add(new HivesView(hive.getId(), hive.getAgeOfQueen(), hive.getEggFrames().getNumberOfEggFrames(), hive.getHoneyFrames().size(), hive.isItWasSplit()));
        }
        gameResponse.setTemperature(game.getWeatherData().getTemperature());
        gameResponse.setActionOfTheWeek(game.getActionOfTheWeek());
        gameResponse.setWindSpeed(game.getWeatherData().getWindSpeed());
        gameResponse.setMoneyInTheBank(game.getMoneyInTheBank());
        gameResponse.setPrecipitation(game.getWeatherData().getPrecipitation());
        gameResponse.setCurrentDate(game.getCurrentDate());
        gameResponse.setTotalKgOfHoneyHarvested(game.getTotalKgOfHoneyHarvested());
        gameResponse.setHistory(game.getGameHistory());
        System.out.println("acestea sunt datele trimise catre React: " + gameResponse);
        return gameResponse;
    }


    @GetMapping("/getHoneyQuantities/{gameId}")
    public ResponseEntity<HarvestHoney> getHoneyQuantities(@PathVariable String gameId, Principal principal) {
        System.out.println("Cerere pentru getHoneyQuantities, gameId: " + gameId);
        LifeOfBees lifeOfBeesGame = lifeOfBeesRepository.findByGameId(gameId)
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
        LifeOfBees lifeOfBeesGame = lifeOfBeesRepository.findByGameId(gameId)
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
        LifeOfBees lifeOfBeesGame = lifeOfBeesRepository.findByGameId(gameId)
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
        LifeOfBees lifeOfBeesGame = lifeOfBeesRepository.findByGameId(gameId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Game not found"));
        System.out.println("Acesta e jocul primit in HiveHistory:" + lifeOfBeesGame);
        String userId = principal.getName();
        if (!lifeOfBeesGame.getUserId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied");
        }

        List<HiveHistory> hiveHistories = new ArrayList<>();
        for (GameHistory historyItem : lifeOfBeesGame.getGameHistory()) {
            Apiary apiary = historyItem.getApiary();
            if (apiary != null) {
                for (Hive hive : apiary.getHives()) {
                    if (hive.getId() == hiveId) {
                        HiveHistory hiveHistory = new HiveHistory();
                        hiveHistory.setId(hive.getId());
                        hiveHistory.setCurrentDate(historyItem.getCurrentDate());
                        hiveHistory.setWeatherData(historyItem.getWeatherData());
                        hiveHistory.setBeesNumber(
                                hive.getBeesBatches().stream().mapToInt(Integer::intValue).sum()
                        );
                        hiveHistory.setQueenAge(hive.getQueen().getAgeOfQueen());
                        hiveHistory.setMoneyInTheBank(historyItem.getMoneyInTheBank());
                        hiveHistory.setItWasSplit(hive.isItWasSplit());
                        hiveHistory.setWasMovedAnEggsFrame(hive.isWasMovedAnEggsFrame());
                        hiveHistory.setEggFramesNumber(hive.getEggFrames().getNumberOfEggFrames());
                        hiveHistory.setHoneyFrameNumber(hive.getHoneyFrames().size());
                        double KgOfHoney = hive.getHoneyBatches().stream()
                                .mapToDouble(HoneyBatch::getKgOfHoney)
                                .sum();
                        Set<HoneyType> honeyTypes = hive.getHoneyBatches().stream()
                                .map(HoneyBatch::getHoneyType)
                                .collect(Collectors.toSet());

                        hiveHistory.setKgOfHoney(KgOfHoney);
                        hiveHistory.setHoneyTypes(honeyTypes);
                        hiveHistories.add(hiveHistory);
                    }
                }
            }
        }

        return hiveHistories;
    }


}
