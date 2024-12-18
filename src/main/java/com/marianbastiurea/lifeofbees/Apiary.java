package com.marianbastiurea.lifeofbees;

import java.time.LocalDate;
import java.util.*;

public class Apiary {

    private List<Hive> hives;
    private HarvestHoney totalHarvestedHoney;

    public HarvestHoney getTotalHarvestedHoney() {
        return totalHarvestedHoney;
    }

    public List<Hive> getHives() {
        return hives;
    }

    public Apiary(List<Hive> hives) {
        this.hives = hives;
        this.totalHarvestedHoney = new HarvestHoney(0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
    }

    @Override
    public String toString() {
        return "Apiary{" +
                "hives=" + hives +
                ", totalHarvestedHoney=" + totalHarvestedHoney +
                '}';
    }

    public Hive getHiveById(Integer hiveId) {
        for (Hive hive : hives) {
            if (hive.getId() == hiveId) {
                return hive;
            }
        }
        return null;
    }


    public void splitHive(Hive hive) {
        List<Hive> newHives = new ArrayList<>();
        EggFrames eggFrames = hive.getEggFrames();
        if (eggFrames.isFullEggFrames() && !hive.isItWasSplit()) {
            hive.setItWasSplit(true);
            Hive newHive = new Hive(this.getHives().size() + 1, true, new Queen(0));
            newHive.setWasMovedAnEggsFrame(false);
            EggFrames newHiveEggFrames = hive.getEggFrames().splitEggFrames();
            newHive.setEggFrames(newHiveEggFrames);

            List<HoneyFrame> newHiveHoneyFrames = new ArrayList<>();
            for (int i = 0; i < 3; i++) {
                HoneyFrame frameToMove = hive.getHoneyFrames().remove(hive.getHoneyFrames().size() - 1);
                newHiveHoneyFrames.add(frameToMove);
            }
            newHive.setHoneyFrames(newHiveHoneyFrames);

            LinkedList<Integer> hiveBeesBatches = hive.getBeesBatches();
            LinkedList<Integer> newHiveBeesBatches = new LinkedList<>(hiveBeesBatches);
            for (int i = 0; i < hiveBeesBatches.size(); i++) {
                int bees = hiveBeesBatches.get(i);
                int beesToTransfer = bees / 2;
                hiveBeesBatches.set(i, bees - beesToTransfer);
                newHiveBeesBatches.add(beesToTransfer);
            }
            newHive.setBeesBatches(newHiveBeesBatches);
            newHive.setHoneyBatches(new ArrayList<>());
            newHives.add(newHive);
            System.out.println("acesta e stupul vechi: " + hive);
            System.out.println("acesta e stupul nou: " + newHive);
        }
        hives.addAll(newHives);
    }


    public List<ActionOfTheWeek> hibernate(LifeOfBees lifeOfBeesGame, List<ActionOfTheWeek> actionsOfTheWeek) {
        LocalDate date = lifeOfBeesGame.getCurrentDate();
        Apiary apiary = lifeOfBeesGame.getApiary();
        System.out.println("aceasta e apiary inainte de hibernate: " + apiary);
        List<Hive> hives = lifeOfBeesGame.getApiary().getHives();
        for (Hive hive : hives) {
            hive.getQueen().setAgeOfQueen(hive.getQueen().getAgeOfQueen() + 1);
            hive.setItWasSplit(false);
            hive.setWasMovedAnEggsFrame(false);
            hive.getHoneyBatches().clear();
            hive.getEggFrames().extractEggBatchesForFrame();
            hive.getHoneyFrames().remove(hive.getHoneyFrames().size() - 1);
            hive.getHoneyFrames().remove(hive.getHoneyFrames().size() - 1);
            hive.getBeesBatches().removeLast();
            hive.getBeesBatches().removeLast();
        }
        Random random = new Random();
        int indexToRemove = random.nextInt(hives.size());
        Hive hiveToRemove = hives.get(indexToRemove);
        int hiveIdRemoved = hiveToRemove.getId();
        hives.remove(hiveToRemove);
        Map<String, Object> data = new HashMap<>();
        data.put("totalHives", apiary.getHives().size());
        data.put("hibernateStartDate", date.toString());
        data.put("hiveIds", List.of(hiveIdRemoved));
        ActionOfTheWeek actionInstance = new ActionOfTheWeek();
        actionInstance.addOrUpdateAction("HIBERNATE", hiveIdRemoved, data, actionsOfTheWeek);
        System.out.println("aceasta e apiary dupa hibernate: " + apiary);

        return actionsOfTheWeek;
    }

    public List<ActionOfTheWeek> checkInsectControl(HarvestingMonths month, int dayOfMonth, List<ActionOfTheWeek> actionsOfTheWeek) {
        if ((month.equals(HarvestingMonths.APRIL) || month.equals(HarvestingMonths.MAY) ||
                month.equals(HarvestingMonths.JUNE) || month.equals(HarvestingMonths.JULY) || month.equals(HarvestingMonths.AUGUST) &&
                (dayOfMonth == 11 || dayOfMonth == 21))) {
            Map<String, Object> data = ActionOfTheWeek.findOrCreateAction("INSECT_CONTROL", actionsOfTheWeek).getData();
            ActionOfTheWeek actionInstance = new ActionOfTheWeek();
            actionInstance.addOrUpdateAction("INSECT_CONTROL", this.getHives().size(), data, actionsOfTheWeek);
        }
        return actionsOfTheWeek;
    }

    public void doInsectControl(String answer, LifeOfBees lifeOfBeesGame) {
        if ("yes".equals(answer)) {
            lifeOfBeesGame.setMoneyInTheBank(lifeOfBeesGame.getMoneyInTheBank() - (lifeOfBeesGame.getApiary().getHives().size() * 10));
        } else {
            for (Hive hive : hives) {
                hive.getBeesBatches().removeLast();
                hive.getBeesBatches().removeLast();
            }
        }
    }

    public List<ActionOfTheWeek> checkFeedBees(HarvestingMonths month, int dayOfMonth, List<ActionOfTheWeek> actionsOfTheWeek) {
        if (month.equals(HarvestingMonths.SEPTEMBER) &&
                (dayOfMonth == 1)) {
            Map<String, Object> data = ActionOfTheWeek.findOrCreateAction("FEED_BEES", actionsOfTheWeek).getData();
            ActionOfTheWeek actionInstance = new ActionOfTheWeek();
            actionInstance.addOrUpdateAction("FEED_BEES", this.getHives().size(), data, actionsOfTheWeek);

        }
        return actionsOfTheWeek;
    }

    public void doFeedBees(String answer, LifeOfBees lifeOfBeesGame) {
        if ("yes".equals(answer)) {
            lifeOfBeesGame.setMoneyInTheBank(lifeOfBeesGame.getMoneyInTheBank() - lifeOfBeesGame.getApiary().getHives().size() * 7);
        } else {
            for (Hive hive : hives) {
                for (int day = 0; day < 7; day++) {
                    hive.getBeesBatches().removeLast();
                    hive.getBeesBatches().removeLast();
                }
            }
        }
    }

    public Map<HoneyType, Double> honeyHarvestedByHoneyType() {
        Map<HoneyType, Double> honeyHarvested = new HashMap<>();
        for (Hive hive : hives) {
            for (HoneyBatch honeyBatch : hive.getHoneyBatches()) {
                if (!honeyBatch.isProcessed()) {
                    HoneyType honeyType = honeyBatch.getHoneyType();
                    double kgOfHoney = honeyBatch.getKgOfHoney();
                    honeyHarvested.put(honeyType, honeyHarvested.getOrDefault(honeyType, 0.0) + kgOfHoney);
                    honeyBatch.setProcessed(true);
                }
            }
        }
        totalHarvestedHoney.setAcacia(totalHarvestedHoney.getAcacia() + honeyHarvested.getOrDefault(HoneyType.Acacia, 0.0));
        totalHarvestedHoney.setRapeseed(totalHarvestedHoney.getRapeseed() + honeyHarvested.getOrDefault(HoneyType.Rapeseed, 0.0));
        totalHarvestedHoney.setWildFlower(totalHarvestedHoney.getWildFlower() + honeyHarvested.getOrDefault(HoneyType.WildFlower, 0.0));
        totalHarvestedHoney.setLinden(totalHarvestedHoney.getLinden() + honeyHarvested.getOrDefault(HoneyType.Linden, 0.0));
        totalHarvestedHoney.setSunFlower(totalHarvestedHoney.getSunFlower() + honeyHarvested.getOrDefault(HoneyType.SunFlower, 0.0));
        totalHarvestedHoney.setFalseIndigo(totalHarvestedHoney.getFalseIndigo() + honeyHarvested.getOrDefault(HoneyType.FalseIndigo, 0.0));

        return honeyHarvested;
    }

    public double getTotalKgHoneyHarvested() {
        return totalHarvestedHoney.getAcacia()
                + totalHarvestedHoney.getRapeseed()
                + totalHarvestedHoney.getWildFlower()
                + totalHarvestedHoney.getLinden()
                + totalHarvestedHoney.getSunFlower()
                + totalHarvestedHoney.getFalseIndigo();
    }

    public void updateHoneyStock(HarvestHoney soldHoneyData) {
        totalHarvestedHoney.setAcacia(totalHarvestedHoney.getAcacia() - soldHoneyData.Acacia);
        totalHarvestedHoney.setRapeseed(totalHarvestedHoney.getRapeseed() - soldHoneyData.Rapeseed);
        totalHarvestedHoney.setWildFlower(totalHarvestedHoney.getWildFlower() - soldHoneyData.WildFlower);
        totalHarvestedHoney.setLinden(totalHarvestedHoney.getLinden() - soldHoneyData.Linden);
        totalHarvestedHoney.setSunFlower(totalHarvestedHoney.getSunFlower() - soldHoneyData.SunFlower);
        totalHarvestedHoney.setFalseIndigo(totalHarvestedHoney.getFalseIndigo() - soldHoneyData.FalseIndigo);
    }

    public List<Hive> createHive(int numberOfHives, LocalDate date) {
        Random random = new Random();
        List<Hive> newHives = new ArrayList<>();
        for (int i = 1; i <= numberOfHives; i++) {
            int ageOfQueen = random.nextInt(1, 6);
            EggFrames eggFrames = EggFrames.getRandomEggFrames();
            LinkedList<Integer> beesBatches = new LinkedList<>();
            for (int k = 0; k < 30; k++) {
                beesBatches.add(random.nextInt(600, 700));
            }
            List<HoneyFrame> honeyFrames = new ArrayList<>();
            for (int k = 0; k < random.nextInt(3, 5); k++) {
                honeyFrames.add(new HoneyFrame(random.nextDouble(2.5, 3)));
            }
            Hive hive = new Hive(
                    newHives.size() + 1,
                    false,
                    false,
                    eggFrames,
                    honeyFrames,
                    beesBatches,
                    new ArrayList<>(),
                    new Queen(ageOfQueen)
            );
            newHives.add(hive);
        }
        return newHives;

    }

    public void moveAnEggsFrame(List<List<Integer>> hiveIdPair) {
        for (List<Integer> hiveIds : hiveIdPair) {
            int sourceHiveId = hiveIds.get(0);
            int destinationHiveId = hiveIds.get(1);
            Hive sourceHive = this.getHiveById(sourceHiveId);
            Hive destinationHive = this.getHiveById(destinationHiveId);
            EggFrames sourceEggFrames = sourceHive.getEggFrames();
            EggFrames destinationEggFrames = destinationHive.getEggFrames();
            List<Integer> eggBatchesToMove = sourceEggFrames.extractEggBatchesForFrame();
            destinationEggFrames.addEggBatches(eggBatchesToMove);
            sourceHive.setWasMovedAnEggsFrame(true);
            System.out.println("acesta e stupul sursa "+sourceEggFrames);
            System.out.println("acestea sunt ramele destinatie"+destinationEggFrames);
        }
    }
}

