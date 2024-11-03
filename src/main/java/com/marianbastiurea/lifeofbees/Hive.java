package com.marianbastiurea.lifeofbees;


//import com.marianbastiurea.lifeofbees.eggframe.EggBatch;

import com.marianbastiurea.lifeofbees.eggframe.EggFrame;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;


public class Hive {
    private int id;
    private boolean itWasSplit;
    private boolean answerIfWantToSplit;
    private boolean wasMovedAnEggsFrame;
    private List<EggFrame> eggFrames;
    private int numberOfBees;
    private Queen queen;
    private Honey honey;
    private List<BeesBatch> beesBatches;
    private List<HoneyFrame> honeyFrames;
    private List<HoneyBatch> honeyBatches;
    private Apiary apiary; // Add an Apiary field to store the associated apiary
    private double kgOfHoney;
    private ActionOfTheWeek actionOfTheWeek;


    public Hive(Apiary apiary, List<EggFrame> eggFrames, List<BeesBatch> beesBatches,
                List<HoneyFrame> honeyFrames, List<HoneyBatch> honeyBatches, ActionOfTheWeek actionOfTheWeek) {
        this.apiary = apiary;
        this.eggFrames = new ArrayList<>(eggFrames);
        this.beesBatches = new ArrayList<>(beesBatches);
        this.honeyFrames = new ArrayList<>(honeyFrames);
        this.honeyBatches = new ArrayList<>(honeyBatches);

    }


    public Hive(Apiary apiary, int id, boolean itWasSplit, boolean answerIfWantToSplit, int numberOfBees, Queen queen) {
        this.apiary = apiary;
        this.id = id;
        this.itWasSplit = itWasSplit;
        this.answerIfWantToSplit = answerIfWantToSplit;
        this.numberOfBees = numberOfBees;
        this.queen = queen;
    }

    public Hive(Honey honey) {
        this.honey = honey;
    }

    public Hive(boolean wasMovedAnEggsFrame) {
        this.wasMovedAnEggsFrame = wasMovedAnEggsFrame;
    }

    public Hive(
            Apiary apiary,
            int hiveIdCounter,
            boolean itWasSplit,
            boolean wasMovedAnEggsFrame,
            boolean answerIfWantToSplit,
            List<EggFrame> eggFrames,
            List<HoneyFrame> honeyFrames,
            List<BeesBatch> beesBatches,
            List<HoneyBatch> honeyBatches,
            Honey honey,
            Queen queen,
            int numberOfBees,
            double kgOfHoney) {
        this.apiary = apiary;
        this.id = hiveIdCounter;
        this.itWasSplit = itWasSplit;
        this.wasMovedAnEggsFrame = wasMovedAnEggsFrame;
        this.answerIfWantToSplit = answerIfWantToSplit;
        this.eggFrames = eggFrames;
        this.honeyFrames = honeyFrames;
        this.beesBatches = beesBatches;
        this.honeyBatches = honeyBatches;
        this.honey = honey;
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

    public boolean isAnswerIfWantToSplit() {
        return answerIfWantToSplit;
    }

    public void setAnswerIfWantToSplit(boolean answerIfWantToSplit) {
        this.answerIfWantToSplit = answerIfWantToSplit;
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
                ", answerIfWantToSplit=" + this.answerIfWantToSplit +
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


    public Hive() {
    }

    public Honey getHoney() {
        return honey;
    }

    public void setHoney(Honey honey) {
        this.honey = honey;
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

    public String getHoneyType() {
        return getHoney().getHoneyType();
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

    public void addHoneyBatches(List<HoneyBatch> honeyBatches) {
        this.honeyBatches.addAll(honeyBatches);
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
/* this method check creation date of each eggs batch and difference between current date and creation date is more
        than 20 days, eggs will hatch into bees. Eggs batch will be removed from list and number of eggs will be add to
        number of bees from hive

 */
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

    public boolean checkIfHiveCouldBeSplit(HarvestingMonths month, int dayOfMonth) {

    /*
       This method will add new empty eggs frame in hive. If total number of egg frames full in hive is equal to 6,
       it will call the method to split the hive in two hives.
       A frame has around 8500 cells. 75% more or less are used by the queen to lay eggs.
       Remaining cells are filled with honey or are damaged.
    */
        if (!this.itWasSplit) {
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
                        return true;
                    }
                }
            }
        }
        return false;
    }


    public boolean checkIfCanAddNewEggsFrameInHive() {
        int eggsFrameFull = 0;
        for (EggFrame eggFrame : this.eggFrames) {
            if (eggFrame.getNumberOfEggs() > 6000) {
                eggsFrameFull += 1;
            }
        }

        if (this.eggFrames.size() < 6) {
            if (eggsFrameFull == this.eggFrames.size()) {
                return true;
            }
        }
        return false;
    }

    public void addNewEggsFrameInHive() {
        if (this.eggFrames.size() < 6) {
            eggFrames.add(new EggFrame());
            System.out.println("New eggs frame added. Total: " + this.eggFrames.size());
        } else {
            System.out.println("Cannot add more eggs frames. Maximum reached.");
        }
    }


    public boolean checkIfCanAddANewHoneyFrameInHive() {
        int honeyFrameFull = 0;

        for (HoneyFrame honeyFrame : this.honeyFrames) {
            if (honeyFrame.getKgOfHoney() > 4) {
                honeyFrameFull += 1;
            }
        }


        if (this.honeyFrames.size() < 6) {
            if (honeyFrameFull == this.honeyFrames.size()) {
                return true;
            }
        }
        return false;
    }


    public void fillUpExistingHoneyFrameFromHive(LocalDate currentDate) {
        double maxKgOfHoneyPerFrame = 4.5;
        // a frame could be loaded with around  4.5Kg of honey
        Random random = new Random();
        int numberOfHoneyFrameNotFull = honeyFrames.size() - this.getNumberOfFullHoneyFrame();
        int numberOfFlight = random.nextInt(3, 6);
        double kgOfHoneyToAdd = this.numberOfBees * numberOfFlight * 0.00002 * getHoney().honeyProductivity();//0.02gr/flight/bee
        for (HoneyFrame honeyFrame : honeyFrames) {
            if (honeyFrame.getKgOfHoney() < maxKgOfHoneyPerFrame) {
                honeyFrame.setKgOfHoney(Math.min(maxKgOfHoneyPerFrame, honeyFrame.getKgOfHoney() + kgOfHoneyToAdd / numberOfHoneyFrameNotFull));
            }
        }
    }

    public void addNewHoneyFrameInHive() {
        if (honeyFrames.size() < 6) {
            honeyFrames.add(new HoneyFrame(0, honey.getHoneyType()));
            System.out.println("New honey frame added. Total: " + this.eggFrames.size());
        } else {
            System.out.println("Cannot add more eggs frames. Maximum reached.");
        }
    }


    public void beesDie(LocalDate currentDate) {
/*
this method will check the date when bees hatched and if difference between hatched date and current date is more than 31 days
they will die. bees number from each batch will subtract from total number of bees from hive
 */

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

    public List<List<Integer>> checkIfCanMoveAnEggsFrame() {
        List<List<Integer>> hiveIdPair = new ArrayList<>();

        if (this.checkIfAll6EggsFrameAre80PercentFull() && !this.itWasSplit && !this.wasMovedAnEggsFrame) {
            List<Hive> hives = this.getApiary().getHives();

            for (Hive hive : hives) {
                if (hive.itWasSplit && hive.getQueen().getAgeOfQueen() == 0) {
                    hiveIdPair.add(Arrays.asList(this.getId(), hive.getId()));
                }
            }
        }
        return hiveIdPair;
    }

    public void changeQueen() {
        queen = new Queen(0);
    }

    public void fillUpEggsFrame(LocalDate currentDate, int numberOfEggs) {

        //  maxEggPerFrame is 6400 a frame have around 8500 cells. 75% more or less are used by the queen to lay eggs.
        // Remaining cells are fill up with honey or are damaged

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
}
