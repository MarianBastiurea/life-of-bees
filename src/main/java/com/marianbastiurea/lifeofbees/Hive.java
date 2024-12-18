package com.marianbastiurea.lifeofbees;

import java.time.LocalDate;
import java.util.*;


public class Hive {
    private int id;
    private boolean itWasSplit;
    private boolean wasMovedAnEggsFrame;
    private EggFrames eggFrames;
    private Queen queen;
    LinkedList<Integer> beesBatches = new LinkedList<>();
    private List<HoneyFrame> honeyFrames;
    private List<HoneyBatch> honeyBatches;
    private ActionOfTheWeek actionOfTheWeek;

    public Hive(EggFrames eggFrames, LinkedList<Integer> beesBatches, List<HoneyFrame> honeyFrames, List<HoneyBatch> honeyBatches) {
        this.eggFrames = eggFrames;
        this.beesBatches = beesBatches;
        this.honeyFrames = honeyFrames;
        this.honeyBatches = honeyBatches;
    }

    public Hive() {
        this.actionOfTheWeek = new ActionOfTheWeek();
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
            List<HoneyFrame> honeyFrames,
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
                ", numberOfHoneyFrame=" + this.honeyFrames.size() +
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

    public void setHoneyFrames(List<HoneyFrame> honeyFrames) {
        this.honeyFrames = honeyFrames;
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

    public List<HoneyFrame> getHoneyFrames() {
        return honeyFrames;
    }


    public LinkedList<Integer> getBeesBatches() {
        return beesBatches;
    }

    public void setBeesBatches(LinkedList<Integer> beesBatches) {
        this.beesBatches = beesBatches;
    }

    public ActionOfTheWeek getAction() {
        return actionOfTheWeek;
    }

    public void setAction(ActionOfTheWeek actionOfTheWeek) {
        this.actionOfTheWeek = actionOfTheWeek;
    }

    public void checkAndAddEggsToBees(int bees) {
        this.getBeesBatches().add(bees);
    }

    public List<ActionOfTheWeek> checkIfHiveCouldBeSplit(HarvestingMonths month, int dayOfMonth, List<ActionOfTheWeek> actionsOfTheWeek, LifeOfBees lifeOfBeesGame) {
        if (!this.itWasSplit && lifeOfBeesGame.getApiary().getHives().size() < 10) {
            if ((month.equals(HarvestingMonths.APRIL) || month.equals(HarvestingMonths.MAY)) &&
                    (dayOfMonth == 1 || dayOfMonth == 10) && this.eggFrames.isFullEggFrames()) {
                if (this.eggFrames.is80PercentFull()) {
                    Map<String, Object> data = ActionOfTheWeek.findOrCreateAction("SPLIT_HIVE", actionsOfTheWeek).getData();
                    ActionOfTheWeek actionInstance = new ActionOfTheWeek();
                    actionInstance.addOrUpdateAction("SPLIT_HIVE", getId(), data, actionsOfTheWeek);
                }
            }
        }
        return actionsOfTheWeek;
    }

    public List<ActionOfTheWeek> checkIfCanAddNewEggsFrameInHive(List<ActionOfTheWeek> actionsOfTheWeek) {
        if (!this.eggFrames.isFullEggFrames() && this.eggFrames.is80PercentFull()) {
            Map<String, Object> data = ActionOfTheWeek.findOrCreateAction("ADD_EGGS_FRAME", actionsOfTheWeek).getData();
            ActionOfTheWeek actionInstance = new ActionOfTheWeek();
            actionInstance.addOrUpdateAction("ADD_EGGS_FRAME", getId(), data, actionsOfTheWeek);
        }
        return actionsOfTheWeek;
    }

    public void addNewEggsFrameInHive() {
        if (this.eggFrames != null) {
            this.eggFrames.incrementNumberOfEggFrames();
        }
    }

    public List<ActionOfTheWeek> checkIfCanAddANewHoneyFrameInHive(List<ActionOfTheWeek> actionsOfTheWeek) {
        long honeyFrameFull = this.honeyFrames.stream()
                .filter(HoneyFrame::isHarvestable)
                .count();

        if (this.honeyFrames.size() < 6) {
            if (honeyFrameFull == this.honeyFrames.size()) {
                Map<String, Object> data = ActionOfTheWeek.findOrCreateAction("ADD_HONEY_FRAME", actionsOfTheWeek).getData();
                ActionOfTheWeek actionInstance = new ActionOfTheWeek();
                actionInstance.addOrUpdateAction("ADD_HONEY_FRAME", getId(), data, actionsOfTheWeek);
            }
        }
        return actionsOfTheWeek;
    }


    public List<ActionOfTheWeek> addHoneyBatches
            (List<HoneyBatch> honeyBatches, List<ActionOfTheWeek> actionsOfTheWeek) {
        if (honeyBatches != null && !honeyBatches.isEmpty()) {
            this.honeyBatches.addAll(honeyBatches);
            Map<String, Object> data = ActionOfTheWeek.findOrCreateAction("HARVEST_HONEY", actionsOfTheWeek).getData();
            ActionOfTheWeek actionInstance = new ActionOfTheWeek();
            actionInstance.addOrUpdateAction("HARVEST_HONEY", getId(), data, actionsOfTheWeek);
        }
        return actionsOfTheWeek;
    }

    public void fillUpExistingHoneyFrameFromHive(LifeOfBees lifeOfBeesGame) {

        Random random = new Random();
        Honey honey = new Honey();
        LocalDate date = lifeOfBeesGame.getCurrentDate();
        int day = date.getDayOfMonth();
        int monthValue = date.getMonthValue();
        HarvestingMonths month = HarvestingMonths.values()[monthValue - 3];
        int numberOfHoneyFrameNotFull = honeyFrames.size() - this.getNumberOfFullHoneyFrame();
        int numberOfFlight = random.nextInt(3, 6);
        double kgOfHoneyToAdd = this.getBeesBatches().stream().mapToInt(Integer::intValue).sum() * numberOfFlight * 0.00002 * honey.honeyProductivity(honey.honeyType(month, day));//0.02gr/flight/bee
        for (HoneyFrame honeyFrame : honeyFrames) {
            honeyFrame.fill(kgOfHoneyToAdd / numberOfHoneyFrameNotFull);
        }
    }


    public void addNewHoneyFrameInHive() {
        if (honeyFrames.size() < 6) {
            honeyFrames.add(new HoneyFrame(0));
            System.out.println("New honey frame added. Total: " + this.honeyFrames.size());
        }
    }


    public int getNumberOfFullHoneyFrame() {
        int honeyFrameFull = 0;
        for (int i = 0; i < this.honeyFrames.size(); i++) {
            if (this.honeyFrames.get(i).isFull() && honeyFrameFull < this.honeyFrames.size()) {
                honeyFrameFull += 1;
            }
        }
        return honeyFrameFull;
    }


    public List<ActionOfTheWeek> checkIfCanMoveAnEggsFrame(List<ActionOfTheWeek> actionsOfTheWeek, LifeOfBees
            lifeOfBeesGame) {
        List<Integer> hiveIdPair;

        if (this.getEggFrames().checkIfAll6EggsFrameAre80PercentFull() && !this.itWasSplit && !this.wasMovedAnEggsFrame) {
            List<Hive> hives = lifeOfBeesGame.getApiary().getHives();
            for (Hive hive : hives) {
                if (hive.itWasSplit && hive.getQueen().getAgeOfQueen() == 0) {
                    hiveIdPair = Arrays.asList(this.getId(), hive.getId());
                    Map<String, Object> data = ActionOfTheWeek.findOrCreateAction("MOVE_EGGS_FRAME", actionsOfTheWeek).getData();
                    ActionOfTheWeek actionInstance = new ActionOfTheWeek();
                    actionInstance.addOrUpdateAction1("MOVE_EGGS_FRAME", hiveIdPair, data, actionsOfTheWeek);
                }
            }
        }
        return actionsOfTheWeek;
    }

    public void changeQueen() {
        queen = new Queen(0);
    }

    public static void addHivesToApiary(List<Hive> newHives, LifeOfBees lifeOfBeesgame) {
        List<Hive> existingHives = lifeOfBeesgame.getApiary().getHives();
        for (Hive hive : newHives) {
            hive.setId(existingHives.size() + 1);
            existingHives.add(hive);
        }
    }
}
