package com.marianbastiurea.lifeofbees;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;
import java.util.*;
import java.util.List;


@Document(collection = "games")
public class LifeOfBees {
    @Id
    private String gameId;
    private String userId;
    private Apiary apiary;
    private List<ActionOfTheWeek> actionOfTheWeek;
    private String gameName;
    private String location;
    private LocalDate currentDate;
    private WeatherData weatherData;
    private double moneyInTheBank;
    private double totalKgOfHoneyHarvested;
    private String gameType;

    @Field("history")
    private List<LifeOfBees> gameHistory = new ArrayList<>();


    public LifeOfBees(String gameId, String gameType, String userId, Apiary apiary,
                      String gameName, String location, LocalDate currentDate,
                      WeatherData weatherData, double moneyInTheBank, double totalKgOfHoneyHarvested,
                      List<ActionOfTheWeek> actionOfTheWeek, List<LifeOfBees> gameHistory) {
        this.gameId = gameId;
        this.userId = userId;
        this.gameType = gameType;
        this.apiary = apiary;
        this.gameName = gameName;
        this.location = location;
        this.currentDate = currentDate;
        this.moneyInTheBank = moneyInTheBank;
        this.totalKgOfHoneyHarvested = totalKgOfHoneyHarvested;
        this.actionOfTheWeek = actionOfTheWeek;
        this.weatherData = weatherData;
        this.gameHistory = gameHistory;
    }

    public LifeOfBees(String gameName, String userId, String gameType, Apiary apiary, List<ActionOfTheWeek> actionOfTheWeek,
                      String location, LocalDate currentDate, WeatherData weatherData,
                      double moneyInTheBank, double totalKgOfHoneyHarvested) {
        this.apiary = apiary;
        this.actionOfTheWeek = actionOfTheWeek;
        this.gameName = gameName;
        this.location = location;
        this.currentDate = currentDate;
        this.weatherData = weatherData;
        this.moneyInTheBank = moneyInTheBank;
        this.totalKgOfHoneyHarvested = totalKgOfHoneyHarvested;
        this.userId = userId;
        this.gameType = gameType;
    }

    public LifeOfBees(LocalDate currentDate, WeatherData weatherData, Apiary apiary, double moneyInTheBank, double totalKgOfHoneyHarvested) {
        this.currentDate = currentDate;
        this.weatherData = weatherData;
        this.apiary = apiary;
        this.moneyInTheBank = moneyInTheBank;
        this.totalKgOfHoneyHarvested = totalKgOfHoneyHarvested;
    }

    @Override
    public String toString() {
        return "LifeOfBees{" +
                "gameId='" + gameId + '\'' +
                ", userId='" + userId + '\'' +
                ", apiary=" + apiary +
                ", actionOfTheWeek=" + actionOfTheWeek +
                ", gameName='" + gameName + '\'' +
                ", location='" + location + '\'' +
                ", currentDate=" + currentDate +
                ", weatherData=" + weatherData +
                ", moneyInTheBank=" + moneyInTheBank +
                ", totalKgOfHoneyHarvested=" + totalKgOfHoneyHarvested +
                ", gameType='" + gameType + '\'' +
                ", gameHistory=" + gameHistory +
                '}';
    }

    public List<LifeOfBees> getGameHistory() {
        return gameHistory;
    }

    public void setGameHistory(List<LifeOfBees> gameHistory) {
        this.gameHistory = gameHistory;
    }

    public LifeOfBees() {
    }

    public LifeOfBees iterateOneWeek(LifeOfBees lifeOfBeesGame, LifeOfBeesService lifeOfBeesService) {
        LocalDate date = lifeOfBeesGame.getCurrentDate();
        WeatherData dailyWeather = lifeOfBeesService.fetchWeatherForDate(date);
        List<ActionOfTheWeek> actionsOfTheWeek = new ArrayList<>();
       // System.out.println("acesta e jocul primit in metoda iterateOneWeek din clasa LifeOfBees: " + lifeOfBeesGame);
        List<LifeOfBees> currentHistory = lifeOfBeesGame.getGameHistory();
    //    System.out.println("acesta e getGameHistory in metoda iterateOneWeek: " + lifeOfBeesGame.getGameHistory());
        if (currentHistory == null || currentHistory.isEmpty()) {
            currentHistory = new ArrayList<>();
            currentHistory.add(new LifeOfBees(
                    lifeOfBeesGame.getGameId(),
                    lifeOfBeesGame.getGameType(),
                    lifeOfBeesGame.getUserId(),
                    lifeOfBeesGame.getApiary(),
                    lifeOfBeesGame.getGameName(),
                    lifeOfBeesGame.getLocation(),
                    lifeOfBeesGame.getCurrentDate(),
                    lifeOfBeesGame.getWeatherData(),
                    lifeOfBeesGame.getMoneyInTheBank(),
                    lifeOfBeesGame.getTotalKgOfHoneyHarvested(),
                    lifeOfBeesGame.getActionOfTheWeek(),
                    null
            ));
            lifeOfBeesGame.setGameHistory(currentHistory);
        }

       // System.out.println("situatia jocului dupa salvare in iterateOneWeek: " + currentHistory);

        for (int dailyIterator = 0; dailyIterator < 7; dailyIterator++) {
            List<Hive> hives = apiary.getHives();
            ArrayList<Hive> oldHives = new ArrayList<>(hives);
            for (Hive hive : oldHives) {
                Honey honey = new Honey();
                HarvestingMonths month = honey.getHarvestingMonth(date);
                Queen queen = hive.getQueen();
                double numberRandom = Math.random();
                if ((numberRandom < 0.5 && month.equals(HarvestingMonths.MAY) && date.getDayOfMonth() > 1 && date.getDayOfMonth() < 20) || queen.getAgeOfQueen() == 5) {
                    hive.changeQueen();
                }
                double whetherIndex = dailyWeather.weatherIndex(dailyWeather);
                int numberOfEggs = queen.makeEggs(honey, whetherIndex);
                System.out.println("numarul de oua: "+numberOfEggs);
                int bees = hive.getEggFrames().ageOneDay(numberOfEggs);
                System.out.println("numarul de albine: "+bees);
                hive.checkIfCanAddNewEggsFrameInHive(actionsOfTheWeek);
                hive.checkIfHiveCouldBeSplit(month, date.getDayOfMonth(), actionsOfTheWeek, lifeOfBeesGame);
                System.out.println("acestea sunt bees batches inainte de a  aduce  "+bees+"  albine in bees batches "+hive.getBeesBatches());
                hive.getBeesBatches().add(bees);
                System.out.println("acestea sunt bees batches dupa adus  "+bees+"  albine in bees batches "+hive.getBeesBatches());
                hive.fillUpExistingHoneyFrameFromHive(lifeOfBeesGame);
                System.out.println("bees batches inainte de a elimina primul batch:"+hive.getBeesBatches());
                hive.getBeesBatches().removeFirst();
                System.out.println("bees batches dupa de a elimina ultimul batch:"+hive.getBeesBatches());
                List<HoneyBatch> harvestedHoneyBatches = honey.harvestHoney(hive, month, date.getDayOfMonth());
                hive.addHoneyBatches(harvestedHoneyBatches, actionsOfTheWeek);
                hive.checkIfCanAddANewHoneyFrameInHive(actionsOfTheWeek);
                hive.checkIfCanMoveAnEggsFrame(actionsOfTheWeek, lifeOfBeesGame);
                apiary.checkInsectControl(month, date.getDayOfMonth(), actionsOfTheWeek);
                apiary.checkFeedBees(month, date.getDayOfMonth(), actionsOfTheWeek);
            }
            apiary.honeyHarvestedByHoneyType();
            System.out.println(apiary.getTotalHarvestedHoney());
            lifeOfBeesGame.setTotalKgOfHoneyHarvested(apiary.getTotalKgHoneyHarvested());

            if (date.isEqual(LocalDate.of(date.getYear(), 9, 30))) {
                apiary.hibernate(lifeOfBeesGame, actionsOfTheWeek);
                date = LocalDate.of(date.getYear() + 1, 3, 1);
                lifeOfBeesGame.setCurrentDate(date);
                break;
            }
            lifeOfBeesGame.setActionOfTheWeek(actionsOfTheWeek);
            System.out.println("Action of the day " + date.getDayOfMonth() + " is " + lifeOfBeesGame.getActionOfTheWeek());
            date = date.plusDays(1);
        }
        lifeOfBeesGame.setCurrentDate(date);
        LifeOfBees newLifeOfBeesGame = new LifeOfBees(date, dailyWeather, apiary, moneyInTheBank, totalKgOfHoneyHarvested);
        currentHistory.add(newLifeOfBeesGame);
        lifeOfBeesGame.setGameHistory(currentHistory);
        return new LifeOfBees(gameId, gameType, userId, apiary, gameName, location, date, dailyWeather, moneyInTheBank, totalKgOfHoneyHarvested, actionOfTheWeek, gameHistory);
    }

    public Apiary getApiary() {
        return apiary;
    }

    public WeatherData getWeatherData() {
        return weatherData;
    }

    public void setWeatherData(WeatherData weatherData) {
        this.weatherData = weatherData;
    }

    public double getMoneyInTheBank() {
        return moneyInTheBank;
    }

    public void setMoneyInTheBank(double moneyInTheBank) {
        this.moneyInTheBank = moneyInTheBank;
    }

    public String getGameName() {
        return gameName;
    }

    public String getLocation() {
        return location;
    }

    public LocalDate getCurrentDate() {
        return currentDate;
    }

    public double getTotalKgOfHoneyHarvested() {
        return totalKgOfHoneyHarvested;
    }

    public void setTotalKgOfHoneyHarvested(double totalKgOfHoneyHarvested) {
        this.totalKgOfHoneyHarvested = totalKgOfHoneyHarvested;
    }

    public void setCurrentDate(LocalDate currentDate) {
        this.currentDate = currentDate;
    }

    public List<ActionOfTheWeek> getActionOfTheWeek() {
        return actionOfTheWeek;
    }

    public void setActionOfTheWeek(List<ActionOfTheWeek> actionOfTheWeek) {
        this.actionOfTheWeek = actionOfTheWeek;
    }

    public WeatherData getAllWeatherData() {
        return weatherData;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getGameType() {
        return gameType;
    }

    public void setGameType(String gameType) {
        this.gameType = gameType;
    }
}