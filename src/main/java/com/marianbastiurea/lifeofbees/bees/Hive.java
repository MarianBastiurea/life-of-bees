package com.marianbastiurea.lifeofbees.bees;

import com.marianbastiurea.lifeofbees.time.BeeTime;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Hive {
    public boolean itWasSplit;
    public List<HoneyBatch> honeyBatches;
    public boolean itWasHarvested;
    public BeesBatches beesBatches;
    private int id;
    private EggFrames eggFrames;
    private Queen queen;
    public HoneyFrames honeyFrames;
    public RandomParameters randomParameters;

    public Hive() {
    }

    public Hive(int id, EggFrames eggFrames) {
        this(
                id,
                false,
                eggFrames,
                new HoneyFrames(),
                new BeesBatches(),
                new ArrayList<>(),
                new Queen(),
                false);
    }

    public Hive(int id, BeesBatches beesBatches) {
        this(
                id,
                false,
                new EggFrames(),
                new HoneyFrames(),
                beesBatches,
                new ArrayList<>(),
                new Queen(),
                false);
    }

    public Hive(int id, HoneyFrames honeyFrames) {
        this(
                id,
                false,
                new EggFrames(),
                honeyFrames, new BeesBatches(),
                new ArrayList<>(),
                new Queen(), false);
    }

    public Hive(int id, HoneyFrames honeyFrames, List<HoneyBatch> honeyBatches, boolean itWasHarvested) {
        this(
                id,
                false,
                new EggFrames(),
                honeyFrames, new BeesBatches(),
                new ArrayList<>(honeyBatches),
                new Queen(),
                itWasHarvested);
    }

    public Hive(int id, List<HoneyBatch> honeyBatches) {
        this(
                id,
                false,
                new EggFrames(),
                new HoneyFrames(), new BeesBatches(),
                new ArrayList<>(honeyBatches),
                new Queen(),
                false);
    }


    public Hive(int id, EggFrames eggFrames, boolean itWasSplit) {
        this(
                id,
                itWasSplit,
                eggFrames,
                new HoneyFrames(), new BeesBatches(),
                new ArrayList<>(),
                new Queen(),
                false);
    }

    public Hive(Queen queen){
   this (0,
        false,
        new EggFrames(),
                new HoneyFrames(),
                new BeesBatches(),
                new ArrayList<>(),
                queen,
        false);

    }

    public Hive(
            int id,
            boolean itWasSplit,
            EggFrames eggFrames,
            HoneyFrames honeyFrames,
            BeesBatches beesBatches,
            List<HoneyBatch> honeyBatches,
            Queen queen,
            boolean itWasHarvested) {
        this.id = id;
        this.itWasSplit = itWasSplit;
        this.eggFrames = eggFrames;
        this.honeyFrames = honeyFrames;
        this.beesBatches = beesBatches;
        this.honeyBatches = honeyBatches;
        this.queen = queen;
        this.itWasHarvested = itWasHarvested;
    }

    public boolean isItWasSplit() {
        return itWasSplit;
    }

    public void setItWasSplit(boolean itWasSplit) {
        this.itWasSplit = itWasSplit;
    }

    public EggFrames getEggFrames() {
        return eggFrames;
    }

    public void setEggFrames(EggFrames eggFrames) {
        this.eggFrames = eggFrames;
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

    /* use for test only
public List<HoneyBatch> getHoneyBatches() {
    return new ArrayList<>(honeyBatches);
} */

    public void setHoneyBatches(List<HoneyBatch> honeyBatches) {
        this.honeyBatches = honeyBatches;
    }

    public HoneyFrames getHoneyFrames() {
        return honeyFrames;
    }

    public void setHoneyFrames(HoneyFrames honeyFrames1) {
        this.honeyFrames = honeyFrames1;
    }

    public BeesBatches getBeesBatches() {
        return beesBatches;
    }

    public void setBeesBatches(BeesBatches beesBatches) {
        this.beesBatches = beesBatches;
    }

    public boolean isItWasHarvested() {
        return itWasHarvested;
    }

    public void setItWasHarvested(boolean itWasHarvested) {
        this.itWasHarvested = itWasHarvested;
    }

    public boolean checkIfHiveCouldBeSplit(BeeTime currentDate) {
        return !this.itWasSplit &&
                currentDate.timeToSplitHive() &&
                this.eggFrames.isMaxNumberOfEggFrames() &&
                this.eggFrames.isFull();
    }


    public void addNewEggsFrameInHive() {
        if (this.eggFrames != null) {
            this.eggFrames.incrementNumberOfEggFrames();
        }
    }

    public void addHoneyBatches
            (List<HoneyBatch> honeyBatches) {
        if (honeyBatches != null && !honeyBatches.isEmpty())
            this.honeyBatches.addAll(honeyBatches);
    }

    public void maybeChangeQueen(BeeTime currentDate, RandomParameters randomParameters) {
        boolean isTimeToChangeQueen = currentDate.isTimeToChangeQueen();
        if ((randomParameters.chanceToChangeQueen() < 0.3 && isTimeToChangeQueen) || queen.getAgeOfQueen() == 5) {
            queen = new Queen(0);
        }
    }


    public void iterateOneDay(BeeTime currentDate, double weatherIndex) {
        maybeChangeQueen(currentDate, randomParameters);
        double productivity = currentDate.honeyType().getProductivity();
        int numberOfEggs = queen.makeEggs(productivity, weatherIndex);
        double kgOfHoneyToAdd = beesBatches.makeHoney(productivity, eggFrames.hatchBees(numberOfEggs), randomParameters.numberOfFlights());
        honeyFrames.fillUpAHoneyFrame(kgOfHoneyToAdd);
    }

    public void harvestHoney(BeeTime currentDate) {
        double harvestedHoney = getHoneyFrames().harvestHoneyFromHoneyFrames();
        if (harvestedHoney >= 0) {
            this.honeyBatches.add(new HoneyBatch(
                    getId(),
                    harvestedHoney,
                    currentDate.honeyType(),
                    false
            ));
            setItWasHarvested(true);
        }
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Hive hive = (Hive) o;
        return itWasSplit == hive.itWasSplit && itWasHarvested == hive.itWasHarvested && id == hive.id && Objects.equals(honeyBatches, hive.honeyBatches) && Objects.equals(beesBatches, hive.beesBatches) && Objects.equals(eggFrames, hive.eggFrames) && Objects.equals(queen, hive.queen) && Objects.equals(honeyFrames, hive.honeyFrames);
    }

    @Override
    public String toString() {
        return "Hive{" +
                "itWasSplit=" + itWasSplit +
                ", honeyBatches=" + honeyBatches +
                ", itWasHarvested=" + itWasHarvested +
                ", beesBatches=" + beesBatches +
                ", id=" + id +
                ", eggFrames=" + eggFrames +
                ", queen=" + queen +
                ", honeyFrames=" + honeyFrames +
                '}';
    }
}
