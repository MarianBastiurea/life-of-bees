package com.marianbastiurea.lifeofbees;

import com.marianbastiurea.lifeofbees.eggframe.EggFrame;

import java.util.ArrayList;
import java.util.List;

public class Apiary {
    /* An apiary is a location where beehives of honey bees are kept.
    Our apiary will start with 1 beehive and we limit this number at 100.
    Our apiary will be in it's first year of existence and we will stop our iteration at year 100.
    At few years acacia or linden forest because of harsh weather will not have flower for bees
    and no honey from this two forest.

     */
    private int numberOfHives;
    private List<Hive> hives;
    private List<HarvestedHoney> harvestedHoneys;


    public Apiary(List<Hive> hives, List<HarvestedHoney> harvestedHoneys) {
        this.hives = hives;
        this.harvestedHoneys = harvestedHoneys;
    }

    public int getNumberOfHives() {
        return numberOfHives;
    }

    public void setNumberOfHives(int numberOfHives) {
        this.numberOfHives = numberOfHives;
    }

    public List<HarvestedHoney> getHarvestedHoneys() {
        return harvestedHoneys;
    }

    public void setHarvestedHoneys(List<HarvestedHoney> harvestedHoneys) {
        this.harvestedHoneys = harvestedHoneys;
    }

    public List<Hive> getHives() {
        return hives;
    }

    public void addHoneyHarvested(List<HarvestedHoney> harvestedHoneys) {
        this.harvestedHoneys.addAll(harvestedHoneys);
    }

    @Override
    public String toString() {
        return "{" +
                "numberOfHives=" + this.getHives().size() +
                ", hives=" + this.hives +
                ", honeys harvested=" + this.harvestedHoneys +
                '}';
    }


    public Hive getHiveById(Integer hiveId) {
        for (Hive hive : hives) {
            if (hive.getId() == hiveId){
                return hive;
            }
        }
        return null;  // Dacă nu găsește stupul, returnează null
    }

    public void splitHive(Hive hive) {

        /*
        this method will split a hive in two equal half. First will check boolean itWasSplit. If this is false
        hive will be split. A hive could be split only once in a year.
         */

        List<Hive> newHives = new ArrayList<>();

        if (hive.getEggsFrames().size() == 6 && !hive.isItWasSplit()) {
            System.out.println("Now old and new frames are full. Hive will be split in two hives.");
            hive.setNumberOfBees(hive.getNumberOfBees() / 2);
            hive.setItWasSplit(true);
            hive.setAnswerIfWantToSplit(true);


            Hive newHive=new Hive(this,this.getHives().size()+1,true,true,hive.getNumberOfBees()/2, new Queen());
            newHive.getQueen().setAgeOfQueen(0);
            newHive.setHoney(hive.getHoney());
            newHive.setApiary(this);
            newHive.setWasMovedAnEggsFrame(false);
            newHive.setBeesBatches(hive.getBeesBatches().subList(0, 0));

            List<EggFrame> newHiveEggFrames = new ArrayList<>();
            for (int i = 0; i < 3; i++) {
                EggFrame frameToMove = hive.getEggsFrames().remove(hive.getEggsFrames().size() - 1);
                newHiveEggFrames.add(frameToMove);

            }
            newHive.setEggsFrames(newHiveEggFrames);

            List<HoneyFrame> newHiveHoneyFrames = new ArrayList<>();
            for (int i = 0; i < 3; i++) {
                HoneyFrame frameToMove = hive.getHoneyFrames().remove(hive.getHoneyFrames().size() - 1);
                newHiveHoneyFrames.add(frameToMove);
            }
            newHive.setHoneyFrames(newHiveHoneyFrames);


            List<BeesBatch> hiveBeesBatches = hive.getBeesBatches();
            List<BeesBatch> newHiveBeesBatches = new ArrayList<>();
            for (BeesBatch beesBatch : hiveBeesBatches) {
                int beesToTransfer = beesBatch.getNumberOfBees() / 2;
                BeesBatch newHiveBatch = new BeesBatch(beesToTransfer, beesBatch.getCreationDate());
                newHiveBeesBatches.add(newHiveBatch);
                beesBatch.setNumberOfBees(beesBatch.getNumberOfBees() - beesToTransfer);
            }
            newHive.setBeesBatches(newHiveBeesBatches);

            newHive.setHoneyBatches(new ArrayList<>());

            newHives.add(newHive);
            this.setNumberOfHives(this.getNumberOfHives() + 1);

        }

        hives.addAll(newHives);
    }
    public double honeyHarvestedByHoneyType() {
        List<HarvestedHoney> rapeseedHoney = new ArrayList<>();
        List<HarvestedHoney> acaciaHoney = new ArrayList<>();
        List<HarvestedHoney> wildFlowerHoney = new ArrayList<>();
        List<HarvestedHoney> lindenHoney = new ArrayList<>();
        List<HarvestedHoney> sunflowerHoney = new ArrayList<>();
        List<HarvestedHoney> falseIndigoHoney = new ArrayList<>();
        double annualKgOfRapeseedHoney = 0;
        double annualKgOfAcaciaHoney = 0;
        double annualKgOfLindenHoney = 0;
        double annualKgOfWildFlowerHoney = 0;
        double annualKgOfSunflowerHoney = 0;
        double annualKgOfFalseIndigoHoney = 0;
        double totalKgOfHoney = 0;

        for (Hive hive : hives) {
            List<HoneyBatch> hiveHoneyBatches = hive.getHoneyBatches();
            for (HoneyBatch honeyBatch : hiveHoneyBatches) {
                HarvestedHoney harvestedHoney = new HarvestedHoney(
                        hive.getId(),
                        honeyBatch.getHoneyType(),
                        honeyBatch.getKgOfHoney(),
                        honeyBatch.getCreationDate()
                );

                switch (honeyBatch.getHoneyType()) {
                    case "Acacia":
                        acaciaHoney.add(harvestedHoney);
                        annualKgOfAcaciaHoney += honeyBatch.getKgOfHoney();
                        break;
                    case "Rapeseed":
                        rapeseedHoney.add(harvestedHoney);
                        annualKgOfRapeseedHoney += harvestedHoney.getKgOfHoney();
                        break;
                    case "WildFlower":
                        wildFlowerHoney.add(harvestedHoney);
                        annualKgOfWildFlowerHoney += honeyBatch.getKgOfHoney();
                        break;
                    case "Linden":
                        lindenHoney.add(harvestedHoney);
                        annualKgOfLindenHoney += honeyBatch.getKgOfHoney();
                        break;
                    case "SunFlower":
                        sunflowerHoney.add(harvestedHoney);
                        annualKgOfSunflowerHoney += honeyBatch.getKgOfHoney();
                        break;
                    case "FalseIndigo":
                        falseIndigoHoney.add(harvestedHoney);
                        annualKgOfFalseIndigoHoney += honeyBatch.getKgOfHoney();
                        break;
                    default:
                        break;
                }
            }
        }

        // Calculează totalul de kg de miere culeasă pe parcursul sezonului
        totalKgOfHoney = annualKgOfAcaciaHoney + annualKgOfRapeseedHoney + annualKgOfWildFlowerHoney +
                annualKgOfLindenHoney + annualKgOfSunflowerHoney + annualKgOfFalseIndigoHoney;

        // Afișează detaliile pentru fiecare tip de miere
        if (!acaciaHoney.isEmpty()) {
            System.out.println("Acacia Honey: " + acaciaHoney);
            System.out.println("Annual quantity of acacia honey is: " + annualKgOfAcaciaHoney);
        }
        if (!rapeseedHoney.isEmpty()) {
            System.out.println("Rapeseed Honey: " + rapeseedHoney);
            System.out.println("Annual quantity of rapeseed Honey: " + annualKgOfRapeseedHoney);
        }
        if (!wildFlowerHoney.isEmpty()) {
            System.out.println("WildFlower Honey: " + wildFlowerHoney);
            System.out.println("Annual quantity of wildflower Honey: " + annualKgOfWildFlowerHoney);
        }
        if (!lindenHoney.isEmpty()) {
            System.out.println("Linden Honey: " + lindenHoney);
            System.out.println("Annual quantity of linden Honey: " + annualKgOfLindenHoney);
        }
        if (!sunflowerHoney.isEmpty()) {
            System.out.println("SunFlower Honey: " + sunflowerHoney);
            System.out.println("Annual quantity of sunflower Honey: " + annualKgOfSunflowerHoney);
        }
        if (!falseIndigoHoney.isEmpty()) {
            System.out.println("FalseIndigo Honey: " + falseIndigoHoney);
            System.out.println("Annual quantity of false indigo Honey: " + annualKgOfFalseIndigoHoney);
        }

        // Afișează totalul de miere culeasă
        System.out.println("Total quantity of honey harvested during the season: " + totalKgOfHoney + " kg");

        // Returnează totalul de miere culeasă
        return totalKgOfHoney;
    }


    public void hibernate(Hive hive) {
        hive.getQueen().setAgeOfQueen(hive.getQueen().getAgeOfQueen() + 1);
        hive.setItWasSplit(false);
        hive.setAnswerIfWantToSplit(false);
        hive.setWasMovedAnEggsFrame(false);
        hive.getHoneyBatches().clear();
        hive.getEggsFrames().remove(hive.getEggsFrames().size() - 1);
        hive.getEggsFrames().remove(hive.getEggsFrames().size() - 1);
        hive.getHoneyFrames().remove(hive.getHoneyFrames().size() - 1);
        hive.getHoneyFrames().remove(hive.getHoneyFrames().size() - 1);
    }
}

