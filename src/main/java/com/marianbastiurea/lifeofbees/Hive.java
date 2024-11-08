package com.marianbastiurea.lifeofbees;

import com.marianbastiurea.lifeofbees.eggframe.EggFrame;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;


public class Hive {
    private int id;
    private boolean itWasSplit;
    private boolean wasMovedAnEggsFrame;
    private List<EggFrame> eggFrames;
    //todo: remove numberOfBees field
    private int numberOfBees;
    private Queen queen;


    //todo:
    //replace  private List<EggBatch> beesBatches;
    //with
    // private static final int MAX_AGE = 30;
    // private int[] beesBatches = new int[MAX_AGE];
    private List<BeesBatch> beesBatches;
    private List<HoneyFrame> honeyFrames;
    private List<HoneyBatch> honeyBatches;
    //todo: remove apiary field
    private Apiary apiary; // Add an Apiary field to store the associated apiary
    //todo: remove kgOfHoney field
    private double kgOfHoney;
    private ActionOfTheWeek actionOfTheWeek;


    public Hive(Apiary apiary, List<EggFrame> eggFrames, List<BeesBatch> beesBatches,
                List<HoneyFrame> honeyFrames, List<HoneyBatch> honeyBatches, ActionOfTheWeek actionOfTheWeek) {
        this.apiary = apiary;
        this.eggFrames = new ArrayList<>(eggFrames);
        this.beesBatches = new ArrayList<>(beesBatches);
        this.honeyFrames = new ArrayList<>(honeyFrames);
        this.honeyBatches = new ArrayList<>(honeyBatches);
        this.actionOfTheWeek = new ActionOfTheWeek();

    }

    public Hive() {
        this.actionOfTheWeek = new ActionOfTheWeek();
    }

    public Hive(Apiary apiary, int id, boolean itWasSplit, int numberOfBees, Queen queen) {
        this.apiary = apiary;
        this.id = id;
        this.itWasSplit = itWasSplit;
        this.numberOfBees = numberOfBees;
        this.queen = queen;
    }


    public Hive(boolean wasMovedAnEggsFrame) {
        this.wasMovedAnEggsFrame = wasMovedAnEggsFrame;
    }

    public Hive(
            Apiary apiary,
            int hiveIdCounter,
            boolean itWasSplit,
            boolean wasMovedAnEggsFrame,
            List<EggFrame> eggFrames,
            List<HoneyFrame> honeyFrames,
            List<BeesBatch> beesBatches,
            List<HoneyBatch> honeyBatches,
            Queen queen,
            int numberOfBees,
            double kgOfHoney) {
        this.apiary = apiary;
        this.id = hiveIdCounter;
        this.itWasSplit = itWasSplit;
        this.wasMovedAnEggsFrame = wasMovedAnEggsFrame;
        this.eggFrames = eggFrames;
        this.honeyFrames = honeyFrames;
        this.beesBatches = beesBatches;
        this.honeyBatches = honeyBatches;
        this.queen = queen;
        this.numberOfBees = numberOfBees;
        this.kgOfHoney = kgOfHoney;
    }

    public boolean isItWasSplit() {
        return itWasSplit;
    }

    public boolean isWasMovedAnEggsFrame() {
        return wasMovedAnEggsFrame;
    }

    public void setWasMovedAnEggsFrame(boolean wasMovedAnEggsFrame) {
        this.wasMovedAnEggsFrame = wasMovedAnEggsFrame;
    }

    public void setItWasSplit(boolean itWasSplit) {
        this.itWasSplit = itWasSplit;
    }

    public void setApiary(Apiary apiary) {
        this.apiary = apiary;
    }

    public Apiary getApiary() {
        return apiary;
    }

    @Override
    public String toString() {
        return "Hive{" +
                "id=" + id +
                ", itWasSplit=" + this.itWasSplit +
                ",wasMovedAnEggsFrame=" + this.wasMovedAnEggsFrame +
                ", numberOfHoneyFrame=" + this.honeyFrames.size() +
                ", numberOfEggsFrame=" + this.eggFrames.size() + "\n" +
                ", eggsFrames=" + this.eggFrames + "\n" +
                ", numberOfBees=" + this.numberOfBees +
                ", age of queen=" + this.queen.getAgeOfQueen() +
                ", beesBatches=" + this.beesBatches +
                ", honeyFrames=" + this.honeyFrames +
                ", honeyBatches=" + this.honeyBatches +
                '}';
    }

    public void setEggsFrames(List<EggFrame> eggFrames) {
        this.eggFrames = eggFrames;
    }

    public void setBeesBatches(List<BeesBatch> beesBatches) {
        this.beesBatches = beesBatches;
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

    public double getKgOfHoney() {
        return kgOfHoney;
    }

    public void setKgOfHoney(double kgOfHoney) {
        this.kgOfHoney = kgOfHoney;
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

    public int getNumberOfBees() {
        return numberOfBees;
    }

    public void setNumberOfBees(int numberOfBees) {
        this.numberOfBees = numberOfBees;
    }

    public void addBeesBatches(List<BeesBatch> beesBatches) {
        this.beesBatches.addAll(beesBatches);
    }

    public void addHoneyFrames(List<HoneyFrame> honeyFrames) {
        this.honeyFrames.addAll(honeyFrames);
    }

    public void addEggsFrames(List<EggFrame> eggFrames) {
        this.eggFrames.addAll(eggFrames);
    }

    public List<HoneyBatch> getHoneyBatches() {
        return honeyBatches;
    }

    public List<HoneyFrame> getHoneyFrames() {
        return honeyFrames;
    }

    public List<EggFrame> getEggsFrames() {
        return eggFrames;
    }

    public List<BeesBatch> getBeesBatches() {
        return beesBatches;
    }

    public ActionOfTheWeek getAction() {
        return actionOfTheWeek;
    }

    public void setAction(ActionOfTheWeek actionOfTheWeek) {
        this.actionOfTheWeek = actionOfTheWeek;
    }

    public void checkAndAddEggsToBees(LocalDate currentDate) {
        int numberOfBeesFromEggsBatch = 0;
        for (EggFrame eggFrame : eggFrames) {
            List<BeesBatch> hatchedBatches = eggFrame.checkAndHatchEggs(currentDate);
            for (BeesBatch batch : hatchedBatches) {
                numberOfBeesFromEggsBatch += batch.getNumberOfBees();
            }
        }
        if (numberOfBeesFromEggsBatch > 0) {
            this.numberOfBees += numberOfBeesFromEggsBatch;
            BeesBatch newBeesBatch = new BeesBatch(numberOfBeesFromEggsBatch, currentDate);
            beesBatches.add(newBeesBatch);
        }
    }

    public List<ActionOfTheWeek> checkIfHiveCouldBeSplit(HarvestingMonths month, int dayOfMonth, List<ActionOfTheWeek> actionsOfTheWeek, LifeOfBees lifeOfBeesGame) {
        if (!this.itWasSplit && lifeOfBeesGame.getApiary().getHives().size() < 10) {
            if ((month.equals(HarvestingMonths.APRIL) || month.equals(HarvestingMonths.MAY)) &&
                    (dayOfMonth == 1 || dayOfMonth == 10)) {
                if (this.eggFrames.size() == 6) {
                    boolean allFramesAreFull = true;
                    for (EggFrame eggFrame : this.eggFrames) {
                        if (eggFrame.getNumberOfEggs() < eggFrame.getMaxEggPerFrame()) {
                            allFramesAreFull = false;
                            break;
                        }
                    }
                    if (allFramesAreFull) {
                        Map<String, Object> data = ActionOfTheWeek.findOrCreateAction("SPLIT_HIVE", actionsOfTheWeek).getData();
                        ActionOfTheWeek actionInstance = new ActionOfTheWeek();
                        actionInstance.addOrUpdateAction("SPLIT_HIVE", getId(), data, actionsOfTheWeek);
                    }
                }
            }
        }
        return actionsOfTheWeek;
    }

    public List<ActionOfTheWeek> checkIfCanAddNewEggsFrameInHive(List<ActionOfTheWeek> actionsOfTheWeek) {
        int eggsFrameFull = 0;
        for (EggFrame eggFrame : this.eggFrames) {
            if (eggFrame.getNumberOfEggs() > 6000) {
                eggsFrameFull += 1;
            }
        }
        if (this.eggFrames.size() < 6 && eggsFrameFull == this.eggFrames.size()) {
            Map<String, Object> data = ActionOfTheWeek.findOrCreateAction("ADD_EGGS_FRAME", actionsOfTheWeek).getData();
            ActionOfTheWeek actionInstance = new ActionOfTheWeek();
            actionInstance.addOrUpdateAction("ADD_EGGS_FRAME", getId(), data, actionsOfTheWeek);
        }
        return actionsOfTheWeek;
    }

    public void addNewEggsFrameInHive() {
        if (this.eggFrames.size() < 6) {
            eggFrames.add(new EggFrame());
            System.out.println("New eggs frame added. Total: " + this.eggFrames.size());
        } else {
            System.out.println("Cannot add more eggs frames. Maximum reached.");
        }
    }

    public List<ActionOfTheWeek> checkIfCanAddANewHoneyFrameInHive(List<ActionOfTheWeek> actionsOfTheWeek) {
        int honeyFrameFull = 0;

        for (HoneyFrame honeyFrame : this.honeyFrames) {
            if (honeyFrame.getKgOfHoney() > 4) {
                honeyFrameFull += 1;
            }
        }
        if (this.honeyFrames.size() < 6) {
            if (honeyFrameFull == this.honeyFrames.size()) {
                Map<String, Object> data = ActionOfTheWeek.findOrCreateAction("ADD_HONEY_FRAME", actionsOfTheWeek).getData();
                ActionOfTheWeek actionInstance = new ActionOfTheWeek();
                actionInstance.addOrUpdateAction("ADD_HONEY_FRAME", getId(), data, actionsOfTheWeek);
            }
        }
        return actionsOfTheWeek;
    }

    public List<ActionOfTheWeek> addHoneyBatches(List<HoneyBatch> honeyBatches, List<ActionOfTheWeek> actionsOfTheWeek) {
        if (honeyBatches != null && !honeyBatches.isEmpty()) {
            this.honeyBatches.addAll(honeyBatches);
            System.out.println("acesta e mierea culeasa in stupul" + this.getId() + " " + honeyBatches);
            Map<String, Object> data = ActionOfTheWeek.findOrCreateAction("HARVEST_HONEY", actionsOfTheWeek).getData();
            ActionOfTheWeek actionInstance = new ActionOfTheWeek();
            actionInstance.addOrUpdateAction("HARVEST_HONEY", getId(), data, actionsOfTheWeek);
        }
        return actionsOfTheWeek;
    }

    public void fillUpExistingHoneyFrameFromHive(LifeOfBees lifeOfBeesGame) {
        double maxKgOfHoneyPerFrame = 4.5;
        // a frame could be loaded with around  4.5Kg of honey
        Random random = new Random();
        Honey honey=new Honey();
        LocalDate date = lifeOfBeesGame.getCurrentDate();
        int day = date.getDayOfMonth();
        int monthValue = date.getMonthValue();
        HarvestingMonths month =HarvestingMonths.values()[monthValue - 3];

        int numberOfHoneyFrameNotFull = honeyFrames.size() - this.getNumberOfFullHoneyFrame();
        int numberOfFlight = random.nextInt(3, 6);
        double kgOfHoneyToAdd = this.numberOfBees * numberOfFlight * 0.00002 * honey.honeyProductivity(honey.honeyType(month,day));//0.02gr/flight/bee
        for (HoneyFrame honeyFrame : honeyFrames) {
            if (honeyFrame.getKgOfHoney() < maxKgOfHoneyPerFrame) {
                honeyFrame.setKgOfHoney(Math.min(maxKgOfHoneyPerFrame, honeyFrame.getKgOfHoney() + kgOfHoneyToAdd / numberOfHoneyFrameNotFull));
            }
        }
    }

    public void addNewHoneyFrameInHive() {
        if (honeyFrames.size() < 6) {
            honeyFrames.add(new HoneyFrame(0));
            System.out.println("New honey frame added. Total: " + this.eggFrames.size());
        } else {
            System.out.println("Cannot add more eggs frames. Maximum reached.");
        }
    }

    public void beesDie(LocalDate currentDate) {
        List<BeesBatch> beesBatches = this.getBeesBatches();
        Iterator<BeesBatch> iterator = beesBatches.iterator();
        while (iterator.hasNext()) {
            BeesBatch beesBatch = iterator.next();
            LocalDate creationDate = beesBatch.getCreationDate();
            long differenceInDays = ChronoUnit.DAYS.between(creationDate, currentDate);
            if (differenceInDays > 30) {
                this.numberOfBees -= beesBatch.getNumberOfBees(); // Subtract number of bees from each beesBatch from total number
                iterator.remove();
            }
        }
    }

    public boolean checkIfAll6EggsFrameAre80PercentFull() {
        if (eggFrames.size() != 6) {
            return false;
        }
        for (EggFrame eggFrame : eggFrames) {
            if (eggFrame.is80PercentFull()) {
                return false;
            }
        }
        return true;
    }

    public int getNumberOfFullEggsFrame() {
        int eggsFrameFull = 0;
        for (EggFrame eggFrame : eggFrames) {
            if (eggFrame.isEggFrameFull() && eggsFrameFull < eggFrames.size()) {
                eggsFrameFull += 1;
            }
        }
        return eggsFrameFull;
    }

    public int getNumberOfFullHoneyFrame() {
        int honeyFrameFull = 0;
        double maxKgOfHoneyPerFrame = 4.5;
        for (int i = 0; i < this.honeyFrames.size(); i++) {
            if (this.honeyFrames.get(i).getKgOfHoney() == maxKgOfHoneyPerFrame && honeyFrameFull < this.honeyFrames.size()) {
                honeyFrameFull += 1;
            }
        }
        return honeyFrameFull;
    }

    public List<ActionOfTheWeek> checkIfCanMoveAnEggsFrame(List<ActionOfTheWeek> actionsOfTheWeek, LifeOfBees lifeOfBeesGame) {
        List<Integer> hiveIdPair;

        if (this.checkIfAll6EggsFrameAre80PercentFull() && !this.itWasSplit && !this.wasMovedAnEggsFrame) {
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

    public void fillUpEggsFrame(LocalDate currentDate, int numberOfEggs) {
        int size = getEggsFrames().size() - this.getNumberOfFullEggsFrame();
        if (size != 0) {
            int numberOfEggsToPutInFrame = numberOfEggs / size;
            for (EggFrame eggFrame : getEggsFrames()) {
                if (eggFrame.getNumberOfEggs() + numberOfEggsToPutInFrame < eggFrame.getMaxEggPerFrame()) {
                    eggFrame.addEggs(numberOfEggsToPutInFrame, currentDate);
                } else {
                    eggFrame.addEggs(eggFrame.getMaxEggPerFrame() - eggFrame.getNumberOfEggs(), currentDate);
                }
            }
        }
    }

    public double findTotalKgOfHoney() {
        double totalKgOfHoney = 0.0;
        for (HoneyBatch honeyBatch : honeyBatches) {
            totalKgOfHoney += honeyBatch.getKgOfHoney();
        }
        return totalKgOfHoney;
    }

    public static void addHivesToApiary(Apiary apiary, List<Hive> newHives) {
        List<Hive> existingHives = apiary.getHives();
        for (Hive hive : newHives) {
            hive.setApiary(apiary);
            hive.setId(existingHives.size() + 1);
            existingHives.add(hive);
        }
    }
}
