package com.marianbastiurea.lifeofbees.game;

import com.marianbastiurea.lifeofbees.action.ActionType;
import com.marianbastiurea.lifeofbees.action.ActionsOfTheWeek;
import com.marianbastiurea.lifeofbees.bees.Apiary;
import com.marianbastiurea.lifeofbees.bees.Hives;
import com.marianbastiurea.lifeofbees.time.BeeTime;
import com.marianbastiurea.lifeofbees.weather.WeatherData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Map;


@Document(collection = "games")
public class LifeOfBees {
    private static final Logger logger = LoggerFactory.getLogger(LifeOfBees.class);
    private String gameName;
    private String location;
    @Id
    private String gameId;
    private String userId;
    private Apiary apiary;
    private ActionsOfTheWeek actionsOfTheWeek;
    private Integer removedHiveId;
    private WeatherData weatherData;
    private double moneyInTheBank;
    private double totalKgOfHoneyHarvested;
    private String gameType;

    public LifeOfBees(String gameName, String userId, String gameType, Apiary apiary,
                      String location, WeatherData weatherData,
                      double moneyInTheBank, double totalKgOfHoneyHarvested, ActionsOfTheWeek actionsOfTheWeek) {
        this.apiary = apiary;
        this.gameName = gameName;
        this.location = location;
        this.weatherData = weatherData;
        this.moneyInTheBank = moneyInTheBank;
        this.totalKgOfHoneyHarvested = totalKgOfHoneyHarvested;
        this.userId = userId;
        this.gameType = gameType;
        this.actionsOfTheWeek = new ActionsOfTheWeek();
    }

    public LifeOfBees() {
    }

    @Override
    public String toString() {
        return "LifeOfBees{" +
                "gameName='" + gameName + '\'' +
                ", location='" + location + '\'' +
                ", gameId='" + gameId + '\'' +
                ", userId='" + userId + '\'' +
                ", apiary=" + apiary +
                ", actionsOfTheWeek=" + actionsOfTheWeek +
                ", removedHiveId=" + removedHiveId +
                ", weatherData=" + weatherData +
                ", moneyInTheBank=" + moneyInTheBank +
                ", totalKgOfHoneyHarvested=" + totalKgOfHoneyHarvested +
                ", gameType='" + gameType + '\'' +
                '}';
    }

    public void iterateOneWeek(Map<ActionType, Object> actions, List<WeatherData> weatherDataNextWeek) {
        logger.info("actions of the week: {}", actions);
        actionsOfTheWeek.executeActions(this, actions);
        WeatherData currentWeatherData = null;
        for (int dailyIterator = 0; dailyIterator < 7; dailyIterator++) {
            currentWeatherData = weatherDataNextWeek.get(dailyIterator);
            double weatherIndex = currentWeatherData.weatherIndex();
            Hives hives = apiary.getHives();
            hives.iterateOneDay(weatherIndex);
            apiary.honeyHarvestedByHoneyType();
            this.setTotalKgOfHoneyHarvested(apiary.getTotalKgHoneyHarvested());
            removedHiveId = hives.hibernate();
            if (removedHiveId != null) {
                this.setRemovedHiveId(removedHiveId);
                break;
            }
            this.setRemovedHiveId(removedHiveId);

        }
        weatherData = currentWeatherData;
        actionsOfTheWeek.createActions(apiary.getHives());
        this.setActionsOfTheWeek(actionsOfTheWeek);
    }


    public Apiary getApiary() {
        return apiary;
    }

    public void setApiary(Apiary apiary) {
        this.apiary = apiary;
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


    public double getTotalKgOfHoneyHarvested() {
        return totalKgOfHoneyHarvested;
    }

    public void setTotalKgOfHoneyHarvested(double totalKgOfHoneyHarvested) {
        this.totalKgOfHoneyHarvested = totalKgOfHoneyHarvested;
    }

    public ActionsOfTheWeek getActionsOfTheWeek() {
        return actionsOfTheWeek;
    }

    public void setActionsOfTheWeek(ActionsOfTheWeek actionsOfTheWeek) {
        this.actionsOfTheWeek = actionsOfTheWeek;
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

    public Integer getRemovedHiveId() {
        return removedHiveId;
    }

    public void setRemovedHiveId(Integer removedHiveId) {
        this.removedHiveId = removedHiveId;
    }

    public BeeTime getCurrentDate() {
        return this.getApiary().getHives().getCurrentDate();
    }
}