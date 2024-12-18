package com.marianbastiurea.lifeofbees;

public class HomePageGameResponse {
    private String gameName;
    private String location;
    private int hivesNumber;
    private double moneyInTheBank;
    private double totalKgOfHoneyHarvested;
    private String gameId;

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getHivesNumber() {
        return hivesNumber;
    }

    public void setHivesNumber(int hivesNumber) {
        this.hivesNumber = hivesNumber;
    }

    public double getTotalKgOfHoneyHarvested() {
        return totalKgOfHoneyHarvested;
    }

    public void setTotalKgOfHoneyHarvested(double totalKgOfHoneyHarvested) {
        this.totalKgOfHoneyHarvested = totalKgOfHoneyHarvested;
    }

    public double getMoneyInTheBank() {
        return moneyInTheBank;
    }

    public void setMoneyInTheBank(double moneyInTheBank) {
        this.moneyInTheBank = moneyInTheBank;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    @Override
    public String toString() {
        return "HomePageGameResponse{" +
                "gameName='" + gameName + '\'' +
                ", location='" + location + '\'' +
                ", hivesNumber=" + hivesNumber +
                ", moneyInTheBank=" + moneyInTheBank +
                ", totalKgOfHoneyHarvested=" + totalKgOfHoneyHarvested +
                ", gameId='" + gameId + '\'' +
                '}';
    }

    public HomePageGameResponse(String gameName, String location, int hivesNumber, double moneyInTheBank,
                                double totalKgOfHoneyHarvested, String gameId) {
        this.gameName = gameName;
        this.location = location;
        this.hivesNumber = hivesNumber;
        this.moneyInTheBank = moneyInTheBank;
        this.totalKgOfHoneyHarvested = totalKgOfHoneyHarvested;
        this.gameId = gameId;
    }
}
