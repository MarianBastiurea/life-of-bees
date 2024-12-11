package com.marianbastiurea.lifeofbees;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.*;
import java.util.List;


@Document(collection = "games")
public class LifeOfBees {
    @Id
    private String id;
    private String userId;

    private Apiary apiary;
    private List<ActionOfTheWeek> actionOfTheWeek;
    private String gameName;
    private String location;
    private LocalDate currentDate;
    private WeatherData weatherData;
    private double moneyInTheBank;
    private double totalKgOfHoneyHarvested;
    private boolean isPublic;



    public LifeOfBees(String id, String userId, Apiary apiary,
                      String gameName, String location, LocalDate currentDate,
                      WeatherData weatherData, double moneyInTheBank, double totalKgOfHoneyHarvested,
                      List<ActionOfTheWeek> actionOfTheWeek) {
        this.id=id;
        this.userId=userId;
        this.apiary = apiary;
        this.gameName = gameName;
        this.location = location;
        this.currentDate = currentDate;
        this.moneyInTheBank = moneyInTheBank;
        this.totalKgOfHoneyHarvested = totalKgOfHoneyHarvested;
        this.actionOfTheWeek = actionOfTheWeek;
        this.weatherData = weatherData;

    }

    public LifeOfBees(String gameName, String userId,Apiary apiary, List<ActionOfTheWeek> actionOfTheWeek,
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
        this.userId=userId;
    }

    public LifeOfBees() {
    }

    @Override
    public String toString() {
        return "LifeOfBees{" +
                "id='" + id + '\'' +
                ", userId='" + userId + '\'' +
                ", apiary=" + apiary +
                ", actionOfTheWeek=" + actionOfTheWeek +
                ", gameName='" + gameName + '\'' +
                ", location='" + location + '\'' +
                ", currentDate=" + currentDate +
                ", weatherData=" + weatherData +
                ", moneyInTheBank=" + moneyInTheBank +
                ", totalKgOfHoneyHarvested=" + totalKgOfHoneyHarvested +
                ", isPublic=" + isPublic +
                '}';
    }

    public LifeOfBees iterateOneWeek(LifeOfBees lifeOfBeesGame, LifeOfBeesService lifeOfBeesService) {
        LocalDate date = lifeOfBeesGame.getCurrentDate();
        WeatherData dailyWeather = lifeOfBeesService.fetchWeatherForDate(date);
        List<ActionOfTheWeek> actionsOfTheWeek = new ArrayList<>();
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
                int bees = hive.getEggFrames().ageOneDay(numberOfEggs);
                hive.checkIfCanAddNewEggsFrameInHive(actionsOfTheWeek);
                hive.checkIfHiveCouldBeSplit(month, date.getDayOfMonth(), actionsOfTheWeek, lifeOfBeesGame);
                hive.checkAndAddEggsToBees(bees);
                hive.fillUpExistingHoneyFrameFromHive(lifeOfBeesGame);
                hive.getBeesBatches().removeLast();
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
        return new LifeOfBees(id,userId, apiary, gameName, location, date, dailyWeather, moneyInTheBank, totalKgOfHoneyHarvested, actionOfTheWeek);
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
    return weatherData;}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}