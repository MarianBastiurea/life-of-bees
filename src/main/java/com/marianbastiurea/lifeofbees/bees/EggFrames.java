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
    private LinkedList<Integer> eggsByDay;
    private boolean wasMovedAnEggsFrame;
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
        this.eggsByDay = new LinkedList<>(Collections.nCopies(DAYS_TO_HATCH, 1000));
    }

    /*
    Use this constructor for tests only.
     */
    public EggFrames(int numberOfEggFrames, int dailyEggs, boolean wasMovedAnEggsFrame) {
        this.numberOfEggFrames = numberOfEggFrames;
        this.eggsByDay = new LinkedList<>(Collections.nCopies(DAYS_TO_HATCH, dailyEggs));
        this.wasMovedAnEggsFrame = wasMovedAnEggsFrame;
    }

    public EggFrames(int numberOfEggFrames, double eggFramesFillPercentage) {
        this.numberOfEggFrames = numberOfEggFrames;
        double o = numberOfEggFrames * MAX_EGG_PER_FRAME * eggFramesFillPercentage / DAYS_TO_HATCH;
        this.eggsByDay = new LinkedList<>(Collections.nCopies(DAYS_TO_HATCH, (int) o));
    }

    public static EggFrames getRandomEggFrames() {
        EggFrames eggFrames = new EggFrames(randomParameters.numberOfEggFrames());
        randomParameters.getRandomEggCounts(DAYS_TO_HATCH)
                .forEach(eggFrames::hatchBees);

        logger.debug("Finishing method getRandomEggFrames");
        return eggFrames;
    }

    public boolean isWasMovedAnEggsFrame() {
        return wasMovedAnEggsFrame;
    }

    public void resetEggFrameMovement() {
        this.wasMovedAnEggsFrame = false;
    }


    public int getNumberOfEggFrames() {
        return numberOfEggFrames;
    }


    public EggFrames splitEggFrames() {
        LinkedList<Integer> newEggBatches = new LinkedList<>();
        for (int i = 0; i < eggsByDay.size(); i++) {
            int halfEggs = eggsByDay.get(i) / 2;
            newEggBatches.add(halfEggs);
            eggsByDay.set(i, halfEggs);
        }
        numberOfEggFrames = NUMBER_OF_EGGS_FRAMES_WHEN_SPLIT;

        return new EggFrames(3, newEggBatches, false);
    }


    public int getEggs() {
        return eggsByDay.stream().mapToInt(Integer::intValue).sum();
    }


    public int hatchBees(int eggsToAdd) {
        eggsByDay.addFirst(Math.min(eggsToAdd, MAX_EGG_PER_FRAME * numberOfEggFrames - getEggs()));
        return eggsByDay.removeLast();
    }

    public void incrementNumberOfEggFrames() {
        if (this.numberOfEggFrames < MAX_NUMBER_OF_EGG_FRAMES)
            this.numberOfEggFrames++;
    }

    public boolean isMaxNumberOfEggFrames() {
        return numberOfEggFrames == MAX_NUMBER_OF_EGG_FRAMES;
    }


    public boolean canAddNewEggsFrame() {
        return !isMaxNumberOfEggFrames() && isFull();
    }

    public boolean checkIfAll6EggsFrameAre80PercentFull() {
        return isMaxNumberOfEggFrames() || isFull();
    }

    public boolean isFull() {
        int eggs = getEggs();
        return eggs >= numberOfEggFrames * MAX_EGG_PER_FRAME * FULLNESS_FACTOR;
    }


    public void moveAnEggFrame(EggFrames destinationEggFrame) {
        for (int i = 0; i < DAYS_TO_HATCH; i++) {
            int sourceEggs = eggsByDay.get(i);
            int destinationEggs = destinationEggFrame.eggsByDay.get(i);
            int eggsToMove = sourceEggs / numberOfEggFrames;
            eggsByDay.set(i, sourceEggs - eggsToMove);
            destinationEggFrame.eggsByDay.set(i, destinationEggs + eggsToMove);

        }
        this.wasMovedAnEggsFrame = true;
        numberOfEggFrames--;
        destinationEggFrame.numberOfEggFrames++;
    }


    public void hibernateEggFrames() {
        for (int i = 0; i < DAYS_TO_HATCH; i++) {
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
        return Objects.hash(numberOfEggFrames, eggsByDay, wasMovedAnEggsFrame, MAX_NUMBER_OF_EGG_FRAMES);
    }

    @Override
    public String toString() {
        return "EggFrames{" +
                "numberOfEggFrames=" + numberOfEggFrames +
                ", eggsByDay=" + eggsByDay +
                ", wasMovedAnEggsFrame=" + wasMovedAnEggsFrame +
                ", maxNumberOfEggFrames=" + MAX_NUMBER_OF_EGG_FRAMES +
                '}';
    }
}
