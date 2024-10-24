package com.marianbastiurea.lifeofbees;

import java.util.ArrayList;
import java.util.List;

public class GameResponse {

    private List<HivesView> hives = new ArrayList<>();
    private List<ActionOfTheWeek> actionOfTheWeek;
    private List<ActionMoveEggsFrame> actionMoveEggsFrames;
    private double temperature;
    private double windSpeed;
    private double precipitation;
    private double moneyInTheBank;
    private String currentDate;
    private double totalKgOfHoneyHarvested;

    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
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

    public List<ActionMoveEggsFrame> getActionMoveEggsFrames() {
        return actionMoveEggsFrames;
    }

    public void setActionMoveEggsFrames(List<ActionMoveEggsFrame> actionMoveEggsFrames) {
        this.actionMoveEggsFrames = actionMoveEggsFrames;
    }

    @Override
    public String toString() {
        return "GameResponse{" +
                "hives=" + hives +
                ", actionOfTheWeek=" + actionOfTheWeek +
                ", actionMoveEggsFrames=" + actionMoveEggsFrames +
                ", temperature=" + temperature +
                ", windSpeed=" + windSpeed +
                ", precipitation=" + precipitation +
                ", moneyInTheBank=" + moneyInTheBank +
                ", currentDate='" + currentDate + '\'' +
                ", totalKgOfHoneyHarvested=" + totalKgOfHoneyHarvested +
                '}';
    }
}

class HivesView {

    int id;
    int ageOfQueen;
    int bees;
    String honeyType;
    int eggsFrame;
    int honeyFrame;
    double kgOfHoney;
    private boolean itWasSplit;

    public HivesView(int id, int ageOfQueen, int bees, String honeyType, int eggsFrame, int honeyFrame, double kgOfHoney, boolean itWasSplit) {
        this.id = id;
        this.ageOfQueen = ageOfQueen;
        this.bees = bees;
        this.honeyType = honeyType;
        this.eggsFrame = eggsFrame;
        this.honeyFrame = honeyFrame;
        this.kgOfHoney = kgOfHoney;
        this.itWasSplit=itWasSplit;
    }

    public int getId() {
        return id;
    }

    public int getAgeOfQueen() {
        return ageOfQueen;
    }

    public int getBees() {
        return bees;
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

    public double getKgOfHoney() {
        return kgOfHoney;
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
                ", bees=" + bees +
                ", honeyType='" + honeyType + '\'' +
                ", eggsFrame=" + eggsFrame +
                ", honeyFrame=" + honeyFrame +
                ", kgOfHoney=" + kgOfHoney +
                '}';
    }
}
