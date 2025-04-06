package com.marianbastiurea.lifeofbees.bees;

import java.util.Objects;

import static com.marianbastiurea.lifeofbees.bees.ApiaryParameters.MAX_EGGS_DAILY_LAID_BY_QUEEN;

public class Queen {
    private double feedBeesIndex;
    private int ageOfQueen;

    public Queen(int ageOfQueen) {
        this.ageOfQueen = ageOfQueen;
        this.feedBeesIndex = 1.0;
    }

    /*
    just for tests

     */
    public Queen(int ageOfQueen, double feedBeesIndex) {
        this.ageOfQueen = ageOfQueen;
        this.feedBeesIndex = feedBeesIndex;
    }

    public Queen() {
    }

    public void setFeedBeesIndex(double feedBeesIndex) {
        this.feedBeesIndex = feedBeesIndex;
    }

    public int getAgeOfQueen() {
        return ageOfQueen;
    }

    public void setAgeOfQueen(int ageOfQueen) {
        this.ageOfQueen = ageOfQueen;
    }

    public int makeEggs(double productivity, double weatherIndex) {
        return (int) (MAX_EGGS_DAILY_LAID_BY_QUEEN * this.ageOfQueenIndex() * productivity * weatherIndex * feedBeesIndex);
    }

    public double ageOfQueenIndex() {
        return switch (ageOfQueen) {
            case 0, 1, 2, 3 -> 1;
            case 4 -> 0.75;
            default -> 0;
        };
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Queen queen = (Queen) o;
        return ageOfQueen == queen.ageOfQueen &&
                Double.compare(queen.feedBeesIndex, feedBeesIndex) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(ageOfQueen, feedBeesIndex);
    }

    @Override
    public String toString() {
        return "Queen{" +
                "ageOfQueen=" + ageOfQueen +
                ", feedBeesIndex=" + feedBeesIndex +
                '}';
    }
}
