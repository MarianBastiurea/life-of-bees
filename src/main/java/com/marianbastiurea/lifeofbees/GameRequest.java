package com.marianbastiurea.lifeofbees;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GameRequest {
    private String gameType;
    private String gameName;
    private String location;
    private String startDate;
    private int numberOfStartingHives;
    private Map<String, WeatherData> allWeatherData;
    private String userId;
    private String username;
    private String password;
    private String token;
    private String gameId;
    private List<LifeOfBees> gameHistory;

    public List<LifeOfBees> getGameHistory() {
        return gameHistory;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

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

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public Integer getNumberOfStartingHives() {
        return numberOfStartingHives;
    }

    public void setNumberOfStartingHives(int numberOfStartingHives) {
        this.numberOfStartingHives = numberOfStartingHives;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGameType() {
        return gameType;
    }

    public void setGameType(String gameType) {
        this.gameType = gameType;
    }

    @Override
    public String toString() {
        return "GameRequest{" +
                "gameType=" + gameType +
                ", gameName='" + gameName + '\'' +
                ", location='" + location + '\'' +
                ", startDate='" + startDate + '\'' +
                ", numberOfStartingHives=" + numberOfStartingHives +
                ", allWeatherData=" + allWeatherData +
                ", userId='" + userId + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", token='" + token + '\'' +
                ", gameId='" + gameId + '\'' +
                ", gameHistory=" + gameHistory +
                '}';
    }
}
