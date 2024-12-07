package com.marianbastiurea.lifeofbees;

import com.marianbastiurea.lifeofbees.Security.JwtTokenProvider;
import com.marianbastiurea.lifeofbees.Users.User;
import com.marianbastiurea.lifeofbees.Users.UserRepository;
import com.marianbastiurea.lifeofbees.Users.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.util.*;

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


    public LifeOfBeesController(LifeOfBeesRepository lifeOfBeesRepository,UserRepository userRepository, UserService userService,JwtTokenProvider jwtTokenProvider ) {
        this.lifeOfBeesRepository = lifeOfBeesRepository;
        this.games = new HashMap<>();
        this.userRepository = userRepository;
        this.userService=userService;
        this.jwtTokenProvider=jwtTokenProvider;
    }

    @PostMapping("/game")
    public ResponseEntity<Map<String, String>> createGame(@RequestBody GameRequest gameRequest, @RequestHeader("Authorization") String authorizationHeader) {
        System.out.println("Received request to create game: " + gameRequest);
        String jwtToken = authorizationHeader.replace("Bearer ", "");
        LocalDate startDate = LocalDate.parse(gameRequest.getStartDate());
        String weatherApiUrl = "http://localhost:8081/api/weather/" + startDate;
        WeatherData weatherData = restTemplate.getForObject(weatherApiUrl, WeatherData.class);
        Map<String, WeatherData> allWeatherData = new HashMap<>();
        allWeatherData.put(weatherData.getDate().toString(), weatherData);

        String userIdFromToken;

        try {
            userIdFromToken = jwtTokenProvider.extractUsername(jwtToken);
            System.out.println("acesta e userId din LifeOfBeesController");
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid token"));
        }

        if (!userIdFromToken.equals(gameRequest.getUserId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "Token does not match userId"));
        }

        User user = null;
        if (gameRequest.getUserId() != null) {
            user = userRepository.findById(gameRequest.getUserId()).orElse(null);
        }

        LifeOfBees lifeOfBeesGame = LifeOfBeesFactory.createLifeOfBeesGame(
                gameRequest.getGameName(),
                gameRequest.getLocation(),
                gameRequest.getStartDate(),
                gameRequest.getNumberOfStartingHives(),
                gameRequest.getUserId(),
                gameRequest.isPublic(),
                allWeatherData
        );
        LifeOfBees savedGame = lifeOfBeesRepository.save(lifeOfBeesGame);
        userService.addGameToUser(user, savedGame.getId());

        Map<String, String> response = new HashMap<>();
        response.put("token", jwtToken);
        response.put("gameId", savedGame.getId());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/game/{gameId}")
    public GameResponse getGame(@PathVariable String gameId, Principal principal) {
        System.out.println("Cerere pentru gameId: " + gameId);
        LifeOfBees lifeOfBeesGame = lifeOfBeesRepository.findById(gameId)
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
    public GameResponse iterateGame(@PathVariable String gameId, Principal principal) {
        System.out.println("Cerere pentru iterație gameId: " + gameId);
        LifeOfBees lifeOfBeesGame = lifeOfBeesRepository.findById(gameId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Game not found"));

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
        LifeOfBees lifeOfBeesGame = lifeOfBeesRepository.findById(gameId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Game not found"));
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
        gameResponse.setId(game.getId());
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
        System.out.println("acestea sunt datele trimise catre React: "+gameResponse);
        return gameResponse;
    }

    @GetMapping("/getHoneyQuantities/{gameId}")
    public ResponseEntity<HarvestHoney> getHoneyQuantities(@PathVariable String gameId, Principal principal) {
        System.out.println("Cerere pentru getHoneyQuantities, gameId: " + gameId);
        LifeOfBees lifeOfBeesGame = lifeOfBeesRepository.findById(gameId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Game not found"));
        String userId = principal.getName(); // Extras din JWT sau alt mecanism de autentificare
        if (!lifeOfBeesGame.getUserId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied");
        }
        Apiary apiary = lifeOfBeesGame.getApiary();
        apiary.honeyHarvestedByHoneyType(); // Actualizează cantitățile recoltate (dacă este necesar)
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
        LifeOfBees lifeOfBeesGame = lifeOfBeesRepository.findById(gameId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Game not found"));

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
        LifeOfBees lifeOfBeesGame = lifeOfBeesRepository.findById(gameId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Game not found"));

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




    // Metoda pentru ștergerea jocurilor dacă se atinge limita
    @DeleteMapping("/checkGameLimit/{userId}")
    public ResponseEntity<?> checkAndEnforceGameLimit(@PathVariable String userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return ResponseEntity.badRequest().body("Utilizatorul nu a fost găsit.");
        }
        if (user.getGameIds().size() > 5) { // Setează limita dorită (5 sau 10)
            String oldestGameId = user.getGameIds().remove(0);
            lifeOfBeesRepository.deleteById(oldestGameId);
            userRepository.save(user);
            return ResponseEntity.ok("Jocul cel mai vechi a fost șters pentru a respecta limita.");
        }
        return ResponseEntity.ok("Nu este nevoie de ștergere, limita nu a fost atinsă.");
    }

}
