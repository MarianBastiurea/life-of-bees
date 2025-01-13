package com.marianbastiurea.lifeofbees.history;

import com.marianbastiurea.lifeofbees.game.LifeOfBees;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "gameHistory")
public class GameHistory {

    @Id
    private String gameHistoryId;
    private String gameId;
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

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public LifeOfBees getGameHistory() {
        return gameHistory;
    }

    public void setGameHistory(LifeOfBees gameHistory) {
        this.gameHistory = gameHistory;
    }

    @Override
    public String toString() {
        return "GameHistory{" +
                "gameHistoryId='" + gameHistoryId + '\'' +
                ", gameId='" + gameId + '\'' +
                ", gameHistory=" + gameHistory +
                '}';
    }
}