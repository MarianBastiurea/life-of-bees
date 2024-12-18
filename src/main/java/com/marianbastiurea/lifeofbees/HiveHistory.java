package com.marianbastiurea.lifeofbees;

import java.time.LocalDate;
import java.util.Set;

public class HiveHistory {
    private int id;
    private Apiary apiary;
    private LocalDate currentDate;
    private WeatherData weatherData;
    private int beesNumber;
    private int queenAge;
    private double moneyInTheBank;
    private double KgOfHoney;
    private boolean itWasSplit;
    private boolean wasMovedAnEggsFrame;
    private int eggFramesNumber;
    private int honeyFrameNumber;
    private Set<HoneyType> honeyTypes;

    public HiveHistory() {
    }

    public HiveHistory(int id, Apiary apiary, LocalDate currentDate, WeatherData weatherData,
                       int beesNumber, int queenAge, double moneyInTheBank, double kgOfHoney,
                       boolean itWasSplit, boolean wasMovedAnEggsFrame, int eggFramesNumber,
                       int honeyFrameNumber, Set<HoneyType> honeyTypes) {
        this.id = id;
        this.apiary = apiary;
        this.currentDate = currentDate;
        this.weatherData = weatherData;
        this.beesNumber = beesNumber;
        this.queenAge = queenAge;
        this.moneyInTheBank = moneyInTheBank;
        KgOfHoney = kgOfHoney;
        this.itWasSplit = itWasSplit;
        this.wasMovedAnEggsFrame = wasMovedAnEggsFrame;
        this.eggFramesNumber = eggFramesNumber;
        this.honeyFrameNumber = honeyFrameNumber;
        this.honeyTypes = honeyTypes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Apiary getApiary() {
        return apiary;
    }

    public void setApiary(Apiary apiary) {
        this.apiary = apiary;
    }

    public LocalDate getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(LocalDate currentDate) {
        this.currentDate = currentDate;
    }

    public WeatherData getWeatherData() {
        return weatherData;
    }

    public void setWeatherData(WeatherData weatherData) {
        this.weatherData = weatherData;
    }

    public int getBeesNumber() {
        return beesNumber;
    }

    public void setBeesNumber(int beesNumber) {
        this.beesNumber = beesNumber;
    }

    public int getQueenAge() {
        return queenAge;
    }

    public void setQueenAge(int queenAge) {
        this.queenAge = queenAge;
    }

    public double getMoneyInTheBank() {
        return moneyInTheBank;
    }

    public void setMoneyInTheBank(double moneyInTheBank) {
        this.moneyInTheBank = moneyInTheBank;
    }

    public double getKgOfHoney() {
        return KgOfHoney;
    }

    public void setKgOfHoney(double kgOfHoney) {
        KgOfHoney = kgOfHoney;
    }

    public boolean isItWasSplit() {
        return itWasSplit;
    }

    public void setItWasSplit(boolean itWasSplit) {
        this.itWasSplit = itWasSplit;
    }

    public boolean isWasMovedAnEggsFrame() {
        return wasMovedAnEggsFrame;
    }

    public void setWasMovedAnEggsFrame(boolean wasMovedAnEggsFrame) {
        this.wasMovedAnEggsFrame = wasMovedAnEggsFrame;
    }

    public int getEggFramesNumber() {
        return eggFramesNumber;
    }

    public void setEggFramesNumber(int eggFramesNumber) {
        this.eggFramesNumber = eggFramesNumber;
    }

    public int getHoneyFrameNumber() {
        return honeyFrameNumber;
    }

    public void setHoneyFrameNumber(int honeyFrameNumber) {
        this.honeyFrameNumber = honeyFrameNumber;
    }

    public Set<HoneyType> getHoneyTypes() {
        return honeyTypes;
    }

    public void setHoneyTypes(Set<HoneyType> honeyTypes) {
        this.honeyTypes = honeyTypes;
    }

    @Override
    public String toString() {
        return "HiveHistory{" +
                "id=" + id +
                ", apiary=" + apiary +
                ", currentDate=" + currentDate +
                ", weatherData=" + weatherData +
                ", beesNumber=" + beesNumber +
                ", queenAge=" + queenAge +
                ", moneyInTheBank=" + moneyInTheBank +
                ", KgOfHoney=" + KgOfHoney +
                ", itWasSplit=" + itWasSplit +
                ", wasMovedAnEggsFrame=" + wasMovedAnEggsFrame +
                ", eggFramesNumber=" + eggFramesNumber +
                ", honeyFrameNumber=" + honeyFrameNumber +
                ", honeyTypes=" + honeyTypes +
                '}';
    }
}
