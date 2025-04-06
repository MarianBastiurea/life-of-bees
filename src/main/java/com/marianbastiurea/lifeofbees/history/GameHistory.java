package com.marianbastiurea.lifeofbees.history;

import com.marianbastiurea.lifeofbees.game.LifeOfBees;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "gameHistory")
public class GameHistory {

    @Id
    private String gameId;
    private String gameHistoryId;
    private LifeOfBees gameHistory;

    public GameHistory() {
    }

    public GameHistory(String gameId, LifeOfBees gameHistory) {
        this.gameId = gameId;
        this.gameHistory = gameHistory;
    }

    public String getGameId() {
        return gameId;
    }

    public String getGameHistoryId() {
        return gameHistoryId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public LifeOfBees getGameHistory() {
        return gameHistory;
    }

    @Override
    public String toString() {
        return "GameHistory{" +
                "gameId='" + gameId + '\'' +
                ", gameHistoryId='" + gameHistoryId + '\'' +
                ", gameHistory=" + gameHistory +
                '}';
    }
}