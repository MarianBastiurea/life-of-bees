package com.marianbastiurea.lifeofbees.bees;

import java.util.*;

public class EggFrames {

    private int daysToHatch = 20;
    public final static int maxEggPerFrame = 6400;
    private int numberOfEggFrames;
    private LinkedList<Integer> eggsByDay;
    private int maxEggFrames = 6;

    public EggFrames(int numberOfEggFrames, LinkedList<Integer> eggsByDay) {
        this.numberOfEggFrames = numberOfEggFrames;
        this.eggsByDay = eggsByDay;
    }

    public EggFrames() {
    }

    public EggFrames(int numberOfEggFrames) {
        this.numberOfEggFrames = numberOfEggFrames;

        this.eggsByDay = new LinkedList<>(Collections.nCopies(daysToHatch, 0));
    }

    public static EggFrames getRandomEggFrames() {
        Random random = new Random();
        EggFrames eggFrames = new EggFrames(random.nextInt(3, 5));
        for (int j = 0; j < 20; j++) {
            eggFrames.ageOneDay(random.nextInt(800, 901));
        }
        return eggFrames;
    }

    public int getNumberOfEggFrames() {
        return numberOfEggFrames;
    }

    public EggFrames splitEggFrames() {
        LinkedList<Integer> newEggBatches = new LinkedList<>();
        LinkedList<Integer> updatedEggBatches = new LinkedList<>();
        for (int eggs : eggsByDay) {
            int halfEggs = eggs / 2;
            newEggBatches.add(halfEggs);
            updatedEggBatches.add(halfEggs);
        }
        eggsByDay = updatedEggBatches;
        numberOfEggFrames = 3;
        return new EggFrames(3, newEggBatches);
    }

    List<Integer> extractEggBatchesForFrame() {
        int currentSum = 0;
        int index = 0;
        while (index < eggsByDay.size() && currentSum + eggsByDay.get(index) <= maxEggPerFrame) {
            currentSum += eggsByDay.get(index);
            index++;
        }
        List<Integer> extractedBatches = new ArrayList<>(eggsByDay.subList(0, index));
        for (int i = 0; i < index; i++) {
            eggsByDay.set(i, 0);
        }
        numberOfEggFrames--;
        return extractedBatches;
    }

    public void addEggBatches(List<Integer> batchesToAdd) {
        for (int i = 0; i < batchesToAdd.size(); i++) {
                eggsByDay.set(i, eggsByDay.get(i) + batchesToAdd.get(i));
        }
            numberOfEggFrames++;
    }

    public boolean isFull() {
        int totalEggs = getEggs();
        return totalEggs >= maxEggPerFrame * numberOfEggFrames;
    }

    public int getEggs() {
        return eggsByDay.stream().mapToInt(Integer::intValue).sum();
    }

    public boolean is80PercentFull() {
        int totalEggs = getEggs();
        return totalEggs >= numberOfEggFrames * maxEggPerFrame * 0.8;
    }

    public int ageOneDay(int eggsToAdd) {
        int currentEggs = getEggs();
        int maxEggs = maxEggPerFrame * numberOfEggFrames;
        eggsByDay.addFirst(Math.min(eggsToAdd, maxEggs - currentEggs));
        return eggsByDay.removeLast();
    }

    public void incrementNumberOfEggFrames() {
        if (this.numberOfEggFrames < 6) {
            this.numberOfEggFrames++;
        }
    }

    public boolean isFullEggFrames() {
        return numberOfEggFrames == maxEggFrames;
    }

    public boolean checkIfAll6EggsFrameAre80PercentFull() {
        return isFullEggFrames() || !is80PercentFull();
    }

    public boolean canAddNewEggsFrame() {
        return isFullEggFrames() && is80PercentFull();
    }

    @Override
    public String toString() {
        return "EggFrames{" +
                "numberOfEggFrames=" + numberOfEggFrames +
                ", eggBatches=" + eggsByDay +
                '}';
    }
}
