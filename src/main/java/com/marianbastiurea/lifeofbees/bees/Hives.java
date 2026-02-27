package com.marianbastiurea.lifeofbees.bees;

import com.marianbastiurea.lifeofbees.game.LifeOfBees;
import com.marianbastiurea.lifeofbees.time.BeeTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.marianbastiurea.lifeofbees.bees.ApiaryParameters.DAYS_TO_LIVE_FOR_A_BEE;
import static com.marianbastiurea.lifeofbees.bees.ApiaryParameters.MAX_NUMBER_OF_EGG_FRAMES;

public class Hives {
    private static final Logger logger = LoggerFactory.getLogger(Hives.class);
    private RandomParameters randomParameters = new RandomParameters();
    private List<Hive> hives;
    private BeeTime currentDate;

    public Hives() {
        this.hives = new ArrayList<>();
        this.randomParameters = new RandomParameters();
    }

    public Hives(BeeTime currentDate, RandomParameters randomParameters, Hive... hives) {
        this.hives = new ArrayList<>(Arrays.asList(hives));
        this.currentDate = currentDate;
        this.randomParameters = randomParameters;
    }


    public Hives(List<Hive> hives, BeeTime currentDate) {
        this.hives = hives;
        this.currentDate = currentDate;
        this.randomParameters = new RandomParameters();
    }

    public Hives(List<Hive> hives) {
        this.hives = hives;
    }

    public Hives(Hive... hives) {
        this.hives = new ArrayList<>(Arrays.asList(hives));
    }

    public List<Hive> getHives() {
        return hives;
    }

    public void setHives(List<Hive> hives) {
        this.hives = hives;
    }

    public void splitHive(Integer hiveId) {
        logger.debug("Starting splitHive for hiveId: {}", hiveId);
        Hive hive = getHiveById(hiveId);

        if (!hive.getEggFrames().isMaxNumberOfEggFrames() || hive.isItWasSplit()) {
            return;
        }
        hive.setItWasSplit(true);
        EggFrames newEggFrames = hive.getEggFrames().splitEggFrames();
        HoneyFrames newHoneyFrames = hive.getHoneyFrames().splitHoneyFrames();
        hives.add(new Hive(
                hives.size() + 1,
                true,
                newEggFrames,
                newHoneyFrames,
                hive.getBeesBatches().splitBeesBatches(),
                new ArrayList<>(),
                new Queen(0)));
        logger.debug("Completed splitHive for hiveId: {}", hiveId);

    }

    public Hive getHiveById(Integer hiveId) {
        for (Hive hive : hives) {
            if (hive.getId() == hiveId) {
                return hive;
            }
        }
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Hives hives1 = (Hives) o;
        return Objects.equals(hives, hives1.hives) && Objects.equals(currentDate, hives1.currentDate) && Objects.equals(randomParameters, hives1.randomParameters);
    }

    @Override
    public String toString() {
        return "Hives{" +
                "hives=" + hives +
                ", randomParameters=" + randomParameters +
                ", currentDate=" + currentDate +
                '}';
    }

    public boolean isEmpty() {
        return hives.isEmpty();
    }

    public List<MoveEggFramePairHives> checkIfCanMoveAnEggsFrame() {
        logger.debug("Starting checkIfCanMoveAnEggsFrame...");
        List<MoveEggFramePairHives> result = hives.stream()
                .filter(sourceHive -> {
                    boolean isEggFrameFull = sourceHive.getEggFrames().checkIfAll6EggsFrameAre80PercentFull();
                    boolean isNotSplit = !sourceHive.isItWasSplit();
                    boolean wasNotMoved = !sourceHive.getEggFrames().isWasMovedAnEggsFrame();
                    return isEggFrameFull && isNotSplit && wasNotMoved;
                })
                .flatMap(sourceHive -> hives.stream()
                        .filter(targetHive -> targetHive.isItWasSplit()
                                && targetHive.getEggFrames().getNumberOfEggFrames() < MAX_NUMBER_OF_EGG_FRAMES)
                        .map(targetHive ->
                                new MoveEggFramePairHives(sourceHive.getId(), targetHive.getId())
                        )
                )
                .collect(Collectors.toList());
        logger.debug("Finishing checkIfCanMoveAnEggsFrame...");
        return result;
    }


    public void moveAnEggsFrame(List<List<Integer>> hiveIdPair) {
        logger.debug("Starting moveAnEggsFrame method with hiveIdPair: {}", hiveIdPair);
        hiveIdPair.forEach(hiveIds -> {
            Hive sourceHive = this.getHiveById(hiveIds.get(0));
            Hive destinationHive = this.getHiveById(hiveIds.get(1));
            EggFrames sourceEggFrames = sourceHive.getEggFrames();
            EggFrames destinationEggFrame = destinationHive.getEggFrames();
            sourceEggFrames.moveAnEggFrame(destinationEggFrame);
        });
        logger.debug("Finished moveAnEggsFrame method.");
    }


    public void addNewHivesToHives(Hives newHives, LifeOfBees lifeOfBeesGame) {
        logger.debug("Starting addHivesToApiary with list of hives {} and game {} ", newHives, lifeOfBeesGame);
        List<Hive> existingHives = lifeOfBeesGame.getApiary().getHives().getHives();
        Set<Integer> existingHiveIds = existingHives.stream()
                .map(Hive::getId)
                .collect(Collectors.toSet());
        for (Hive hive : newHives.getHives()) {
            int newHiveId = 1;
            while (existingHiveIds.contains(newHiveId)) {
                newHiveId++;
            }
            hive.setId(newHiveId);
            existingHiveIds.add(newHiveId);
            existingHives.add(hive);
        }
        logger.debug("Finished addHivesToApiary method");
    }

    public Integer randomRemoveAHive() {
        if (hives.isEmpty()) {
            return 0;
        }
        int indexToRemove = randomParameters.hiveIndexToRemove(hives.size());
        Hive hiveToRemove = hives.remove(indexToRemove);
        logger.debug("Hive removed with hiveId: {}. Remaining hives: {}", hiveToRemove.getId(), hives.size());
        return hiveToRemove.getId();
    }

    public Integer hibernate() {
        Integer removedHive = null;
        if (currentDate.isEndOfSeason()) {
            removedHive = randomRemoveAHive();
            this.getHives().forEach(hive -> {
                hive.getQueen().setAgeOfQueen(hive.getQueen().getAgeOfQueen() + 1);
                hive.setItWasSplit(false);
                hive.getEggFrames().resetEggFrameMovement();
                hive.getEggFrames().hibernateEggFrames();
                hive.getHoneyFrames().hibernateHoneyFrames();
                hive.getBeesBatches().hibernateBeesBatches();
                hive.getHoneyBatches().clear();
            });
            logger.debug("Completed hibernate method.");
            currentDate.changeYear();
        }
        return removedHive;
    }


    public Hives createHives(int numberOfHives, BeeTime date) {
        List<Hive> hiveList = IntStream.rangeClosed(1, numberOfHives).mapToObj(i -> {
            logger.debug("Creating hive with id = {}", i);
            BeesBatches beesBatches = new BeesBatches(
                    IntStream.range(0, DAYS_TO_LIVE_FOR_A_BEE)
                            .mapToObj(key-> randomParameters.numberOfBees())
                            .collect(Collectors.toCollection(LinkedList::new))
            );
            Hive hive = new Hive(
                    i,
                    false,
                    EggFrames.getRandomEggFrames(),
                    HoneyFrames.getRandomHoneyFrames(),
                    beesBatches,
                    new ArrayList<>(),
                    new Queen(randomParameters.ageOfQueen())
            );

            logger.debug("Hive created: {}", hive);
            return hive;
        }).collect(Collectors.toList());

        logger.debug("Finished creating {} hives", hiveList.size());

        return new Hives(hiveList, date);
    }


    public void addAll(List<Hive> newHives) {
        this.hives.addAll(newHives);
    }

    public void iterateOneDay(double weatherIndex) {
        for (Hive hive : hives) {
            hive.iterateOneDay(currentDate, weatherIndex);
        }
        currentDate.addDay();
    }

    public BeeTime getCurrentDate() {
        return currentDate;
    }


}
