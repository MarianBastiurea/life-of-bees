package com.marianbastiurea.lifeofbees;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class GameResponse {

    private String id;
    private List<HivesView> hives = new ArrayList<>();
    private List<ActionOfTheWeek> actionOfTheWeek;
    private double temperature;
    private double windSpeed;
    private double precipitation;
    private double moneyInTheBank;
    private LocalDate currentDate;
    private double totalKgOfHoneyHarvested;
    private List<GameHistory> history;

    public List<GameHistory> getHistory() {
        return history;
    }

    public void setHistory(List<GameHistory> history) {
        this.history = history;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDate getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(LocalDate currentDate) {
        this.currentDate = currentDate;
    }

    public List<HivesView> getHives() {
        return hives;
    }

    public void setHives(List<HivesView> hives) {
        this.hives = hives;
    }

    public List<ActionOfTheWeek> getActionOfTheWeek() {
        return actionOfTheWeek;
    }

    public void setActionOfTheWeek(List<ActionOfTheWeek> actionOfTheWeek) {
        this.actionOfTheWeek = actionOfTheWeek;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(double windSpeed) {
        this.windSpeed = windSpeed;
    }

    public double getMoneyInTheBank() {
        return moneyInTheBank;
    }

    public void setMoneyInTheBank(double moneyInTheBank) {
        this.moneyInTheBank = moneyInTheBank;
    }

    public double getPrecipitation() {
        return precipitation;
    }

    public void setPrecipitation(double precipitation) {
        this.precipitation = precipitation;
    }

    public double getTotalKgOfHoneyHarvested() {
        return totalKgOfHoneyHarvested;
    }

    public void setTotalKgOfHoneyHarvested(double totalKgOfHoneyHarvested) {
        this.totalKgOfHoneyHarvested = totalKgOfHoneyHarvested;
    }

    public void setWeatherData(WeatherData weatherData) {
        if (weatherData != null) {
            this.temperature = weatherData.getTemperature();
            this.precipitation = weatherData.getPrecipitation();
            this.windSpeed = weatherData.getWindSpeed();
        }
    }

    @Override
    public String toString() {
        return "GameResponse{" +
                "id='" + id + '\'' +
                ", hives=" + hives +
                ", actionOfTheWeek=" + actionOfTheWeek +
                ", temperature=" + temperature +
                ", windSpeed=" + windSpeed +
                ", precipitation=" + precipitation +
                ", moneyInTheBank=" + moneyInTheBank +
                ", currentDate=" + currentDate +
                ", totalKgOfHoneyHarvested=" + totalKgOfHoneyHarvested +
                ", history=" + history +
                '}';
    }
}

class HivesView {

    int id;
    int ageOfQueen;
    String honeyType;
    int eggsFrame;
    int honeyFrame;
    private boolean itWasSplit;

    public HivesView(int id, int ageOfQueen, int eggsFrame, int honeyFrame, boolean itWasSplit) {
        this.id = id;
        this.ageOfQueen = ageOfQueen;
        this.eggsFrame = eggsFrame;
        this.honeyFrame = honeyFrame;
        this.itWasSplit = itWasSplit;
    }

    public int getId() {
        return id;
    }

    public int getAgeOfQueen() {
        return ageOfQueen;
    }

    public String getHoneyType() {
        return honeyType;
    }

    public int getEggsFrame() {
        return eggsFrame;
    }

    public int getHoneyFrame() {
        return honeyFrame;
    }

    public boolean isItWasSplit() {
        return itWasSplit;
    }

    public void setItWasSplit(boolean itWasSplit) {
        this.itWasSplit = itWasSplit;
    }

    @Override
    public String toString() {
        return "HivesView{" +
                "id=" + id +
                ", ageOfQueen=" + ageOfQueen +
                ", honeyType='" + honeyType + '\'' +
                ", eggsFrame=" + eggsFrame +
                ", honeyFrame=" + honeyFrame +
                '}';
    }
}
