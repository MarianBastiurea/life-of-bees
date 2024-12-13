package com.marianbastiurea.lifeofbees;

import com.marianbastiurea.lifeofbees.Users.User;
import com.marianbastiurea.lifeofbees.Users.UserRepository;
import com.marianbastiurea.lifeofbees.Users.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    public LifeOfBeesController(LifeOfBeesRepository lifeOfBeesRepository,UserRepository userRepository, UserService userService) {
        this.lifeOfBeesRepository = lifeOfBeesRepository;
        this.games = new HashMap<>();
        this.userRepository = userRepository;
        this.userService=userService;
    }



    @PostMapping("/game")
    public String createGame(@RequestBody GameRequest gameRequest) {
        LocalDate startDate = LocalDate.parse(gameRequest.getStartDate());
        String weatherApiUrl = "http://localhost:8081/api/weather/" + startDate;
        WeatherData weatherData = restTemplate.getForObject(weatherApiUrl, WeatherData.class);
        Map<String, WeatherData> allWeatherData = new HashMap<>();
        allWeatherData.put(weatherData.getDate().toString(), weatherData);
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

        // Verifică limita după crearea jocului
       // checkAndEnforceGameLimit(userId);
        return savedGame.getId();
    }



    @GetMapping("/game/{gameId}")
    public GameResponse getGame(@PathVariable String gameId) {
        LifeOfBees lifeOfBeesGame = lifeOfBeesRepository.findById(gameId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Game not found"));
        System.out.println("Acestea sunt datele trimise către React: " + getGameResponse(lifeOfBeesGame));
        return getGameResponse(lifeOfBeesGame);
    }

    @PostMapping("/iterate/{gameId}")
    public GameResponse iterateGame(@PathVariable String gameId) {
        Optional<LifeOfBees> optionalGame = lifeOfBeesRepository.findById(gameId);
        if (!optionalGame.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Game not found");
        }
        LifeOfBees lifeOfBeesGame = optionalGame.get();
        LifeOfBeesService lifeOfBeesService = new LifeOfBeesService();
        lifeOfBeesGame = lifeOfBeesGame.iterateOneWeek(lifeOfBeesGame, lifeOfBeesService);
        lifeOfBeesRepository.save(lifeOfBeesGame);
        GameResponse response = getGameResponse(lifeOfBeesGame);
        System.out.println("GameResponse după iterație: " + response);

        return response;
    }



    @PostMapping("/submitActionsOfTheWeek/{gameId}")
    public GameResponse submitActionsOfTheWeek(@PathVariable String gameId, @RequestBody List<ActionOfTheWeek> approvedActions) {
        Optional<LifeOfBees> optionalGame = lifeOfBeesRepository.findById(gameId);
        if (!optionalGame.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Game not found");
        }

        LifeOfBees lifeOfBeesGame = optionalGame.get();
        Apiary apiary = lifeOfBeesGame.getApiary();
        LocalDate date = lifeOfBeesGame.getCurrentDate();
        for (ActionOfTheWeek action : approvedActions) {
            switch (action.getActionType()) {
                case "ADD_EGGS_FRAME":
                    List<Integer> eggHiveIds = (List<Integer>) action.getData().get("hiveIds");
                    if (eggHiveIds != null) {
                        for (Integer hiveId : eggHiveIds) {
                            Hive hive = apiary.getHiveById(hiveId);
                            hive.addNewEggsFrameInHive();
                        }
                    }
                    break;

                case "ADD_HONEY_FRAME":
                    List<Integer> honeyHiveIds = (List<Integer>) action.getData().get("hiveIds");
                    if (honeyHiveIds != null) {
                        for (Integer hiveId : honeyHiveIds) {
                            Hive hive = apiary.getHiveById(hiveId);
                            if (hive != null) {
                                hive.addNewHoneyFrameInHive();
                            }
                        }
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
                        for (Integer hiveId : splitHiveIds) {
                            Hive hive = apiary.getHiveById(hiveId);
                            if (hive != null) {
                                apiary.splitHive(hive);
                            }
                        }
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
        lifeOfBeesGame.getActionOfTheWeek().clear();
        lifeOfBeesRepository.save(lifeOfBeesGame);
        GameResponse response = getGameResponse(lifeOfBeesGame);
        System.out.println("GameResponse after submit action of the week: " + response);
        return response;
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
    public ResponseEntity<HarvestHoney> getHoneyQuantities(@PathVariable String gameId) {
        Optional<LifeOfBees> optionalGame = lifeOfBeesRepository.findById(gameId);
        if (!optionalGame.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Game not found");
        }
        LifeOfBees lifeOfBeesGame = optionalGame.get();
        Apiary apiary = lifeOfBeesGame.getApiary();
        apiary.honeyHarvestedByHoneyType();
        HarvestHoney honeyData = apiary.getTotalHarvestedHoney();
        return ResponseEntity.ok(honeyData);
    }


    @PostMapping("/sellHoney/{gameId}")
    public ResponseEntity<String> sendSellHoneyQuantities(
            @PathVariable String gameId,
            @RequestBody Map<String, Double> requestData) {
        double revenue = requestData.getOrDefault("totalValue", 0.0);
        HarvestHoney soldHoneyData = new HarvestHoney();
        for (Map.Entry<String, Double> entry : requestData.entrySet()) {
            if (!entry.getKey().equals("totalValue")) {
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
                        return ResponseEntity.badRequest().body("Invalid honey type: " + entry.getKey());
                }
            }
        }
        Optional<LifeOfBees> optionalGame = lifeOfBeesRepository.findById(gameId);
        if (!optionalGame.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Game not found");
        }
        LifeOfBees lifeOfBeesGame = optionalGame.get();
        Apiary apiary = lifeOfBeesGame.getApiary();
        apiary.updateHoneyStock(soldHoneyData);
        lifeOfBeesGame.setTotalKgOfHoneyHarvested(apiary.getTotalKgHoneyHarvested());
        lifeOfBeesGame.setMoneyInTheBank(lifeOfBeesGame.getMoneyInTheBank() + revenue);

        lifeOfBeesRepository.save(lifeOfBeesGame);

        return ResponseEntity.ok("Stock and revenue updated successfully.");
    }

    @PostMapping("/buyHives/{gameId}")
    public ResponseEntity<?> buyHives(@PathVariable String gameId, @RequestBody Map<String, Integer> request) {
        Integer numberOfHives = request.get("numberOfHives"); // Fără conversie, fiind deja Integer
        Optional<LifeOfBees> optionalGame = lifeOfBeesRepository.findById(gameId);
        if (!optionalGame.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Game not found");
        }
        LifeOfBees lifeOfBeesGame = optionalGame.get();
        Apiary apiary = lifeOfBeesGame.getApiary();
        Hive.addHivesToApiary(apiary.createHive(numberOfHives, lifeOfBeesGame.getCurrentDate()), lifeOfBeesGame);
        lifeOfBeesGame.setMoneyInTheBank(lifeOfBeesGame.getMoneyInTheBank() - numberOfHives * 500);
        lifeOfBeesRepository.save(lifeOfBeesGame);
        return ResponseEntity.ok("Hives bought successfully");
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
