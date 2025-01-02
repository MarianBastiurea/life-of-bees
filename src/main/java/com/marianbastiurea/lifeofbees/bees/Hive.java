package com.marianbastiurea.lifeofbees.bees;

import com.marianbastiurea.lifeofbees.action.ActionType;
import com.marianbastiurea.lifeofbees.action.ActionsOfTheWeek;
import com.marianbastiurea.lifeofbees.time.BeeTime;

import java.time.Month;
import java.time.LocalDate;
import java.util.*;

public class Hive {
    private int id;
    public boolean itWasSplit;
    public boolean wasMovedAnEggsFrame;
    private EggFrames eggFrames;
    private Queen queen;
    LinkedList<Integer> beesBatches = new LinkedList<>();
    private HoneyFrames honeyFrames;
    public List<HoneyBatch> honeyBatches;

    public Hive() {
    }

    public Hive(EggFrames eggFrames, LinkedList<Integer> beesBatches, HoneyFrames honeyFrames, List<HoneyBatch> honeyBatches) {
        this.eggFrames = eggFrames;
        this.beesBatches = beesBatches;
        this.honeyFrames = honeyFrames;
        this.honeyBatches = honeyBatches;
    }

    public Hive(int id, boolean itWasSplit, Queen queen) {
        this.id = id;
        this.itWasSplit = itWasSplit;
        this.queen = queen;
    }

    public Hive(boolean wasMovedAnEggsFrame) {
        this.wasMovedAnEggsFrame = wasMovedAnEggsFrame;
    }

    public Hive(
            int hiveIdCounter,
            boolean itWasSplit,
            boolean wasMovedAnEggsFrame,
            EggFrames eggFrames,
            HoneyFrames honeyFrames,
            LinkedList<Integer> beesBatches,
            List<HoneyBatch> honeyBatches,
            Queen queen) {
        this.id = hiveIdCounter;
        this.itWasSplit = itWasSplit;
        this.wasMovedAnEggsFrame = wasMovedAnEggsFrame;
        this.eggFrames = eggFrames;
        this.honeyFrames = honeyFrames;
        this.beesBatches = beesBatches;
        this.honeyBatches = honeyBatches;
        this.queen = queen;
    }

    public boolean isItWasSplit() {
        return itWasSplit;
    }

    public void setWasMovedAnEggsFrame(boolean wasMovedAnEggsFrame) {
        this.wasMovedAnEggsFrame = wasMovedAnEggsFrame;
    }

    public boolean isWasMovedAnEggsFrame() {
        return wasMovedAnEggsFrame;
    }

    public void setItWasSplit(boolean itWasSplit) {
        this.itWasSplit = itWasSplit;
    }

    @Override
    public String toString() {
        return "Hive{" +
                "id=" + id +
                ", itWasSplit=" + this.itWasSplit +
                ",wasMovedAnEggsFrame=" + this.wasMovedAnEggsFrame +
                ", numberOfHoneyFrame=" + this.honeyFrames.getHoneyFrame().size() +
                ", numberOfEggsFrame=" + this.eggFrames.getNumberOfEggFrames() + "\n" +
                ", eggFrames=" + this.eggFrames + "\n" +
                ", age of queen=" + this.queen.getAgeOfQueen() +
                ", beesBatches=" + this.beesBatches +
                ", honeyFrames=" + this.honeyFrames +
                ", honeyBatches=" + this.honeyBatches +
                '}';
    }

    public EggFrames getEggFrames() {
        return eggFrames;
    }

    public void setEggFrames(EggFrames eggFrames) {
        this.eggFrames = eggFrames;
    }

    public void setHoneyFrames(HoneyFrames honeyFrames1) {
        this.honeyFrames = honeyFrames1;
    }

    public void setHoneyBatches(List<HoneyBatch> honeyBatches) {
        this.honeyBatches = honeyBatches;
    }

    public Queen getQueen() {

        return queen;
    }

    public void setQueen(Queen queen) {

        this.queen = queen;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAgeOfQueen() {
        return getQueen().getAgeOfQueen();
    }

    public List<HoneyBatch> getHoneyBatches() {
        return honeyBatches;
    }

    public HoneyFrames getHoneyFrames() {
        return honeyFrames;
    }


    public LinkedList<Integer> getBeesBatches() {
        return beesBatches;
    }

    public void setBeesBatches(LinkedList<Integer> beesBatches) {
        this.beesBatches = beesBatches;
    }


    public void checkAndAddEggsToBees(int bees) {
        this.getBeesBatches().add(bees);
    }

    public boolean checkIfHiveCouldBeSplit(LocalDate currentDate) {

        if (this.itWasSplit) {
            return false;
        }
        return BeeTime.timeToSplitHive(currentDate) && this.eggFrames.isFullEggFrames() &&
                this.eggFrames.is80PercentFull();
    }

    public void addNewEggsFrameInHive() {
        if (this.eggFrames != null) {
            this.eggFrames.incrementNumberOfEggFrames();
        }
    }


    public void addHoneyBatches
            (List<HoneyBatch> honeyBatches) {
        if (honeyBatches != null && !honeyBatches.isEmpty()) {
            this.honeyBatches.addAll(honeyBatches);
        }
    }


    public void changeQueen(LocalDate currentDate) {
        double numberRandom = Math.random();
        Month month = currentDate.getMonth();
        int dayOfMonth = currentDate.getDayOfMonth();
        if ((numberRandom < 0.5 && month == Month.MAY && dayOfMonth > 1 && dayOfMonth < 20) || queen.getAgeOfQueen() == 5) {
            queen = new Queen(0);
        }
    }

    public void ageOneDay(int numberOfEggs) {
        int bees = getEggFrames().ageOneDay(numberOfEggs);
        getBeesBatches().add(bees);
    }

    public LinkedList<Integer> splitBeesBatches() {
        LinkedList<Integer> newHiveBeesBatches = new LinkedList<>(getBeesBatches());
        for (int i = 0; i < getBeesBatches().size(); i++) {
            int bees = getBeesBatches().get(i);
            int beesToTransfer = bees / 2;
            getBeesBatches().set(i, bees - beesToTransfer);
            newHiveBeesBatches.add(beesToTransfer);
        }
        return  newHiveBeesBatches;
    }

    public void removeBeesBatches() {
        LinkedList<Integer> beesBatches = getBeesBatches();
        for (int i = 0; i < 2 && !beesBatches.isEmpty(); i++) {
            beesBatches.removeLast();
        }
    }
}
