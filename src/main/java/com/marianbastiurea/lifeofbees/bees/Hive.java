package com.marianbastiurea.lifeofbees.bees;

import com.marianbastiurea.lifeofbees.time.BeeTime;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Hive {
    private static RandomParameters randomParameters = new RandomParameters();
    private boolean itWasSplit;
    private List<HoneyBatch> honeyBatches;
    private BeesBatches beesBatches;
    private HoneyFrames honeyFrames;
    private int id;
    private EggFrames eggFrames;
    private Queen queen;

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
                new Queen());
    }

    public Hive(int id, BeesBatches beesBatches) {
        this(
                id,
                false,
                new EggFrames(),
                new HoneyFrames(),
                beesBatches,
                new ArrayList<>(),
                new Queen());
    }

    public Hive(int id, BeesBatches beesBatches, Queen queen, EggFrames eggFrames) {
        this(
                id,
                false,
                eggFrames,
                new HoneyFrames(),
                beesBatches,
                new ArrayList<>(),
                queen);
    }

    public Hive(int id, HoneyFrames honeyFrames) {
        this(
                id,
                false,
                new EggFrames(),
                honeyFrames, new BeesBatches(),
                new ArrayList<>(),
                new Queen());
    }

    public Hive(int id, HoneyFrames honeyFrames, List<HoneyBatch> honeyBatches) {
        this(
                id,
                false,
                new EggFrames(),
                honeyFrames, new BeesBatches(),
                new ArrayList<>(honeyBatches),
                new Queen());
    }

    public Hive(int id, List<HoneyBatch> honeyBatches) {
        this(
                id,
                false,
                new EggFrames(),
                new HoneyFrames(), new BeesBatches(),
                new ArrayList<>(honeyBatches),
                new Queen());
    }


    public Hive(int id, EggFrames eggFrames, boolean itWasSplit) {
        this(
                id,
                itWasSplit,
                eggFrames,
                new HoneyFrames(), new BeesBatches(),
                new ArrayList<>(),
                new Queen());
    }

    public Hive(Queen queen) {
        this(0,
                false,
                new EggFrames(),
                new HoneyFrames(),
                new BeesBatches(),
                new ArrayList<>(),
                queen);

    }

    public Hive(
            int id,
            boolean itWasSplit,
            EggFrames eggFrames,
            HoneyFrames honeyFrames,
            BeesBatches beesBatches,
            List<HoneyBatch> honeyBatches,
            Queen queen) {
        this.id = id;
        this.itWasSplit = itWasSplit;
        this.eggFrames = eggFrames;
        this.honeyFrames = honeyFrames;
        this.beesBatches = beesBatches;
        this.honeyBatches = honeyBatches;
        this.queen = queen;
    }

    public Hive(int id) {
        this(
                id,
                false,
                new EggFrames(),
                new HoneyFrames(),
                new BeesBatches(),
                new ArrayList<>(),
                new Queen());
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
        }
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Hive hive = (Hive) o;
        return itWasSplit == hive.itWasSplit &&
                id == hive.id && Objects.equals(honeyBatches, hive.honeyBatches) &&
                Objects.equals(beesBatches, hive.beesBatches) && Objects.equals(eggFrames, hive.eggFrames)
                && Objects.equals(queen, hive.queen) && Objects.equals(honeyFrames, hive.honeyFrames) &&
                Objects.equals(randomParameters, hive.randomParameters);
    }

    @Override
    public String toString() {
        return "Hive{" +
                " id=" + id +
                ", itWasSplit=" + itWasSplit +
                ", honeyBatches=" + honeyBatches +
                ", beesBatches=" + beesBatches +
                ", eggFrames=" + eggFrames +
                ", queen=" + queen +
                ", honeyFrames=" + honeyFrames +
                '}';
    }
}
