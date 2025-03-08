package com.marianbastiurea.lifeofbees.bees;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.LinkedList;
import java.util.Objects;

import static com.marianbastiurea.lifeofbees.bees.ApiaryParameters.*;

public class EggFrames {

    private static final Logger logger = LoggerFactory.getLogger(EggFrames.class);
    public static RandomParameters randomParameters = new RandomParameters();
    public LinkedList<Integer> eggsByDay;
    public boolean wasMovedAnEggsFrame;
    private int numberOfEggFrames;

    public EggFrames(int numberOfEggFrames, LinkedList<Integer> eggsByDay) {
        this.numberOfEggFrames = numberOfEggFrames;
        this.eggsByDay = new LinkedList<>(eggsByDay);
    }


    public EggFrames(int numberOfEggFrames, LinkedList<Integer> eggsByDay, boolean wasMovedAnEggsFrame) {
        this.numberOfEggFrames = numberOfEggFrames;
        this.eggsByDay = eggsByDay;
        this.wasMovedAnEggsFrame = wasMovedAnEggsFrame;
    }

    public EggFrames() {
    }

    public EggFrames(int numberOfEggFrames) {
        this.numberOfEggFrames = numberOfEggFrames;
        this.eggsByDay = new LinkedList<>(Collections.nCopies(daysToHatch, 1000));
    }

    /*
    Use this constructor for tests only.
     */
    public EggFrames(int numberOfEggFrames, int dailyEggs, boolean wasMovedAnEggsFrame) {
        this.numberOfEggFrames = numberOfEggFrames;
        this.eggsByDay = new LinkedList<>(Collections.nCopies(daysToHatch, dailyEggs));
        this.wasMovedAnEggsFrame = wasMovedAnEggsFrame;
    }

    public EggFrames(int numberOfEggFrames, double eggFramesFillPercentage) {
        this.numberOfEggFrames = numberOfEggFrames;
        double o = numberOfEggFrames * maxEggPerFrame * eggFramesFillPercentage / daysToHatch;
        this.eggsByDay = new LinkedList<>(Collections.nCopies(daysToHatch, (int) o));
    }

    public static EggFrames getRandomEggFrames() {
        EggFrames eggFrames = new EggFrames(randomParameters.numberOfEggFrames());
        randomParameters.getRandomEggCounts(daysToHatch)
                .forEach(eggFrames::hatchBees);

        logger.debug("Finishing method getRandomEggFrames");
        return eggFrames;
    }

    public void setWasMovedAnEggsFrame(boolean wasMovedAnEggsFrame) {
        this.wasMovedAnEggsFrame = wasMovedAnEggsFrame;
    }

    public int getNumberOfEggFrames() {
        return numberOfEggFrames;
    }

    public void setNumberOfEggFrames(int numberOfEggFrames) {
        this.numberOfEggFrames = numberOfEggFrames;
    }

    public EggFrames splitEggFrames() {
        LinkedList<Integer> newEggBatches = new LinkedList<>();
        for (int i = 0; i < eggsByDay.size(); i++) {
            int halfEggs = eggsByDay.get(i) / 2;
            newEggBatches.add(halfEggs);
            eggsByDay.set(i, halfEggs);
        }
        numberOfEggFrames = 3;

        return new EggFrames(3, newEggBatches, false);
    }


    public int getEggs() {
        return eggsByDay.stream().mapToInt(Integer::intValue).sum();
    }


    public int hatchBees(int eggsToAdd) {
        eggsByDay.addFirst(Math.min(eggsToAdd, maxEggPerFrame * numberOfEggFrames - getEggs()));
        return eggsByDay.removeLast();
    }

    public void incrementNumberOfEggFrames() {
        if (this.numberOfEggFrames < maxNumberOfEggFrames)
            this.numberOfEggFrames++;
    }

    public boolean isMaxNumberOfEggFrames() {
        return numberOfEggFrames == maxNumberOfEggFrames;
    }


    public boolean canAddNewEggsFrame() {
        return !isMaxNumberOfEggFrames() && isFull();
    }

    public boolean checkIfAll6EggsFrameAre80PercentFull() {
        return isMaxNumberOfEggFrames() || isFull();
    }

    public boolean isFull() {
        int eggs = getEggs();
        return eggs >= numberOfEggFrames * maxEggPerFrame * fullnessFactor;
    }


    public void moveAnEggFrame(EggFrames destinationEggFrame) {
        for (int i = 0; i < daysToHatch; i++) {
            int sourceEggs = eggsByDay.get(i);
            int destinationEggs = destinationEggFrame.eggsByDay.get(i);
            int eggsToMove = sourceEggs / numberOfEggFrames;
            eggsByDay.set(i, sourceEggs - eggsToMove);
            destinationEggFrame.eggsByDay.set(i, destinationEggs + eggsToMove);

        }
        setWasMovedAnEggsFrame(true);
        numberOfEggFrames--;
        destinationEggFrame.numberOfEggFrames++;
    }


    public void hibernateEggFrames() {
        for (int i = 0; i < daysToHatch; i++) {
            Integer sourceEggs = eggsByDay.get(i);
            int eggsToMove = sourceEggs / numberOfEggFrames;
            eggsByDay.set(i, sourceEggs - eggsToMove);
        }
        numberOfEggFrames--;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EggFrames eggFrames = (EggFrames) o;
        return numberOfEggFrames == eggFrames.numberOfEggFrames && wasMovedAnEggsFrame == eggFrames.wasMovedAnEggsFrame && Objects.equals(eggsByDay, eggFrames.eggsByDay);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numberOfEggFrames, eggsByDay, wasMovedAnEggsFrame, maxNumberOfEggFrames);
    }

    @Override
    public String toString() {
        return "EggFrames{" +
                "numberOfEggFrames=" + numberOfEggFrames +
                ", eggsByDay=" + eggsByDay +
                ", wasMovedAnEggsFrame=" + wasMovedAnEggsFrame +
                ", maxNumberOfEggFrames=" + maxNumberOfEggFrames +
                '}';
    }
}
