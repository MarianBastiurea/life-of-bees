package com.marianbastiurea.lifeofbees;


import java.time.LocalDate;

public class BeesBatch {
    private int numberOfBees;
    private final LocalDate creationDate;

    public BeesBatch(int numberOfBees, LocalDate creationDate) {
        this.numberOfBees = numberOfBees;
        this.creationDate = creationDate;
    }

    public int getNumberOfBees() {
        return numberOfBees;
    }

    public void setNumberOfBees(int numberOfBees) {
        this.numberOfBees = numberOfBees;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    @Override
    public String toString() {
        return "BeesBatch{" +
                "numberOfBees=" + numberOfBees +
                ", creationDate=" + creationDate +
                '}'+ "\n";
    }
}
