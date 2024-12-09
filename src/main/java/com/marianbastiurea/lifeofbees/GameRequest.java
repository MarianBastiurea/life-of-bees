package com.marianbastiurea.lifeofbees;


import org.springframework.data.annotation.Id;

import java.util.Map;

public class GameRequest {
    private boolean isPublic;
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

    public Map<String, WeatherData> getAllWeatherData() {
        return allWeatherData;
    }

    public void setAllWeatherData(Map<String, WeatherData> allWeatherData) {
        this.allWeatherData = allWeatherData;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
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

    @Override
    public String toString() {
        return "GameRequest{" +
                "isPublic=" + isPublic +
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
                '}';
    }
}
