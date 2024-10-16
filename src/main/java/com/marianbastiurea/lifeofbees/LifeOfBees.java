package com.marianbastiurea.lifeofbees;

import java.util.*;
import java.util.Date;
import java.util.List;


public class LifeOfBees {
    private Apiary apiary;// apiary is the place where it will be stored all hives
    private int hiveIdCounter = 1;
    private Action action;
    private Integer gameId;
    private String gameName;
    private String location;
    private String startingDate;
    private String currentDate;
    private int numberOfStartingHives;
    private double speedWind;// in km/h
    private double temperature;// in Celsius Degree
    private double   precipitation;
    private  String actionOfTheWeek;
    private double moneyInTheBank;

    public LifeOfBees(Apiary apiary, Integer gameId, String gameName, String location,String currentDate, double speedWind, double temperature, double precipitation, String actionOfTheWeek, double moneyInTheBank) {
        this.apiary = apiary;
        this.gameId = gameId;
        this.gameName = gameName;
        this.location = location;
        this.currentDate=currentDate;
        this.speedWind = speedWind;
        this.temperature = temperature;
        this.precipitation = precipitation;
        this.moneyInTheBank = moneyInTheBank;
        this.action = new Action();

        String[] dateParts = currentDate.split("-");
        int month = Integer.parseInt(dateParts[1]);
        int dayOfMonth = Integer.parseInt(dateParts[2]);

        HarvestingMonths harvestingMonth = HarvestingMonths.values()[month - 1];
        this.actionOfTheWeek = this.action.actionType(harvestingMonth, dayOfMonth);
    }

    @Override
    public String toString() {
        return "LifeOfBees{" +
                "apiary=" + apiary +
                ", hiveIdCounter=" + hiveIdCounter +
                ", action=" + action +
                ", gameId=" + gameId +
                ", gameName='" + gameName + '\'' +
                ", location='" + location + '\'' +
                ", currentDate='" + currentDate + '\'' +
                ", numberOfStartingHives=" + numberOfStartingHives +
                ", speedWind=" + speedWind +
                ", temperature=" + temperature +
                ", precipitation=" + precipitation +
                ", actionOfTheWeek='" + actionOfTheWeek + '\'' +
                ", moneyInTheBank=" + moneyInTheBank +
                '}';
    }

    // Method to iterate over 2 years and execute daily operations
    public void iterateOneWeek(IWeather whether) {

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) - 1); // Set the calendar to one year ago
        calendar.set(Calendar.MONTH, Calendar.MARCH); // Start the year on March 1st
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        System.out.println("apiary at beginning of game is: " + apiary);
        // Iterate over 2 years
        for (int year = 0; year < 2; year++) {
            while (calendar.get(Calendar.MONTH) != Calendar.OCTOBER) {
                // Iterate until OCTOBER
                Date currentDate = calendar.getTime();
                System.out.println("Date: " + currentDate);
                // Extract day of month and month from the calendar
                int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                int monthValue = calendar.get(Calendar.MONTH);
                HarvestingMonths harvestingMonth = HarvestingMonths.values()[monthValue];


                List<Hive> hives = apiary.getHives();
                apiary.setNumberOfHives(apiary.getHives().size());

                ArrayList<Hive> oldHives = new ArrayList<>(hives);
                for (Hive hive : oldHives) {
                    Queen queen = hive.getQueen();
                    hive.getHoney().honeyType(harvestingMonth, dayOfMonth);
                    double numberRandom = Math.random();
                    if (numberRandom < 0.5 && harvestingMonth.equals(HarvestingMonths.MAY) && dayOfMonth > 1 && dayOfMonth < 20 || queen.getAgeOfQueen() == 5) {
                        hive.changeQueen();
                    }
                    Honey honey = hive.getHoney();

                    double whetherIndex = whether.whetherIndex(harvestingMonth, dayOfMonth);

                    int numberOfEggs = queen.makeEggs(honey, whetherIndex);
                    hive.fillUpEggsFrame(currentDate, numberOfEggs);
                    hive.addNewEggsFrameInHive(year);
                    hive.moveAnEggsFrameFromUnsplitHiveToASplitOne();
                    hive.checkAndAddEggsToBees(currentDate);
                    hive.fillUpExistingHoneyFrameFromHive(currentDate);
                    hive.addNewHoneyFrameInHive();
                    hive.addHoneyBatches(honey.makeHoneyBatch(hive, currentDate));
                    hive.beesDie(currentDate);


                    System.out.println();
                }

                calendar.add(Calendar.DAY_OF_MONTH, 1); // Move to the next day
            }

            apiary.honeyHarvestedByHoneyType();
            System.out.println("your apiary at the end of the year " + year + " is: " + apiary);

            List<Hive> hives = apiary.getHives();// have to build a hibernate method

            for (Hive hive : hives) {
                apiary.hibernate(hive);
            }

            calendar.set(Calendar.MONTH, Calendar.MARCH);// Reset month for the next year

            System.out.println("your apiary at the beginning of new  year is: " + apiary);
        }
    }

    public Integer getGameId() {
        return gameId;
    }


    public Apiary getApiary() {
        return apiary;
    }

    public double getTemperature() {
        return temperature;
    }

    public Action getAction() {
        return action;
    }

    public double getSpeedWind() {
        return speedWind;
    }

    public double getPrecipitation() {
        return precipitation;
    }

    public String getActionOfTheWeek() {
        return actionOfTheWeek;
    }

    public double getMoneyInTheBank() {
        return moneyInTheBank;
    }

    public void setMoneyInTheBank(double moneyInTheBank) {
        this.moneyInTheBank = moneyInTheBank;
    }

    public int getHiveIdCounter() {
        return hiveIdCounter;
    }

    public String getGameName() {
        return gameName;
    }

    public String getLocation() {
        return location;
    }

    public int getNumberOfStartingHives() {
        return numberOfStartingHives;
    }

    public String getCurrentDate() {
        return currentDate;
    }
}