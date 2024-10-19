package com.marianbastiurea.lifeofbees;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

import static com.marianbastiurea.lifeofbees.Honey.getHarvestingMonth;


public class LifeOfBees {
    private Apiary apiary;// apiary is the place where it will be stored all hives
    private int hiveIdCounter = 1;
    private List<ActionOfTheWeek> actionOfTheWeek;
    private Integer gameId;
    private String gameName;
    private String location;
    private String startingDate;
    private String currentDate;
    private int numberOfStartingHives;
    private double speedWind;// in km/h
    private double temperature;// in Celsius Degree
    private double precipitation;
    private double moneyInTheBank;
    private double totalKgOfHoney;

    public LifeOfBees(Apiary apiary,Integer gameId,
                      String gameName, String location,  String currentDate,
                       double speedWind, double temperature, double precipitation, double moneyInTheBank, double totalKgOfHoney,List<ActionOfTheWeek> actionOfTheWeek) {
        this.apiary = apiary;
        this.gameId = gameId;
        this.gameName = gameName;
        this.location = location;
        this.currentDate = currentDate;
        this.speedWind = speedWind;
        this.temperature = temperature;
        this.precipitation = precipitation;
        this.moneyInTheBank = moneyInTheBank;
        this.totalKgOfHoney = totalKgOfHoney;
        this.actionOfTheWeek = actionOfTheWeek;
    }

    @Override
    public String toString() {
        return "LifeOfBees{" +
                "apiary=" + apiary +
                ", hiveIdCounter=" + hiveIdCounter +
                ", action=" + actionOfTheWeek +
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
                ", totalKgOfHoney=" + totalKgOfHoney +
                '}';
    }



    public LifeOfBees iterateOneWeek(LifeOfBees lifeOfBeesGame) {
        LocalDate date = LocalDate.parse(lifeOfBeesGame.getCurrentDate());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM", Locale.ENGLISH);
        HarvestingMonths month = getHarvestingMonth(date);
        Whether whether = new Whether();

        List<ActionOfTheWeek> actionsOfTheWeek = new ArrayList<>();

        Whether todayWeather = null;
        for (int dailyIterator = 0; dailyIterator < 7; dailyIterator++) {
            todayWeather = whether.whetherToday(month, date.getDayOfMonth());
            List<Hive> hives = apiary.getHives();
            apiary.setNumberOfHives(hives.size());
            ArrayList<Hive> oldHives = new ArrayList<>(hives);
            List<Integer> hiveIds = new ArrayList<>();
            for (Hive hive : oldHives) {
                Queen queen = hive.getQueen();
                hive.getHoney().honeyType(month, date.getDayOfMonth());
                double numberRandom = Math.random();

                if ((numberRandom < 0.5 && month.equals(HarvestingMonths.MAY) && date.getDayOfMonth() > 1 && date.getDayOfMonth() < 20) || queen.getAgeOfQueen() == 5) {
                    hive.changeQueen();
                }

                Honey honey = hive.getHoney();
                double whetherIndex = whether.whetherIndex(month, date.getDayOfMonth());

                int numberOfEggs = queen.makeEggs(honey, whetherIndex);
                hive.fillUpEggsFrame(date, numberOfEggs);

                hive.checkIfCanAddNAewEggsFrameInHive();
                boolean checkIfCanAddNAewEggsFrameInHive = hive.checkIfCanAddNAewEggsFrameInHive();
                if (checkIfCanAddNAewEggsFrameInHive) {
                    String newAction = "Add new eggs frame in Hive " + hive.getId();
                    String actionMarker = "ADD_NEW_EGGS_FRAME";

                    Optional<ActionOfTheWeek> existingAction = actionsOfTheWeek.stream()
                            .filter(action -> action.getActionOfTheWeekMarker().equals(actionMarker))
                            .findFirst();

                    if (existingAction.isPresent()) {
                        if (!existingAction.get().getHiveIds().contains(hive.getId())) {
                            existingAction.get().getHiveIds().add(hive.getId());
                        }
                    } else {
                        List<Integer> newHiveIds = new ArrayList<>();
                        newHiveIds.add(hive.getId());
                        ActionOfTheWeek actionOfTheWeek = new ActionOfTheWeek(newAction, actionMarker, newHiveIds);
                        actionsOfTheWeek.add(actionOfTheWeek);
                    }

                    System.out.println(newAction);
                    System.out.println(actionMarker);

                    lifeOfBeesGame.setActionOfTheWeek(actionsOfTheWeek);
                }


                hive.moveAnEggsFrameFromUnsplitHiveToASplitOne();

                hive.checkIfCanAddANewHoneyFrameInHive();
                boolean checkIfCanAddANewHoneyFrameInHive = hive.checkIfCanAddANewHoneyFrameInHive();
                if (checkIfCanAddANewHoneyFrameInHive) {
                    String newAction = "Add new honey frame in Hive ";
                    String actionMarker = "ADD_NEW_HONEY_FRAME";

                    Optional<ActionOfTheWeek> existingAction = actionsOfTheWeek.stream()
                            .filter(action -> action.getActionOfTheWeekMarker().equals(actionMarker))
                            .findFirst();

                    if (existingAction.isPresent()) {
                        if (!existingAction.get().getHiveIds().contains(hive.getId())) {
                            existingAction.get().getHiveIds().add(hive.getId());
                        }
                    } else {
                        List<Integer> newHiveIds = new ArrayList<>();
                        newHiveIds.add(hive.getId());
                        ActionOfTheWeek actionOfTheWeek = new ActionOfTheWeek(newAction, actionMarker, newHiveIds);
                        actionsOfTheWeek.add(actionOfTheWeek);
                    }

                    lifeOfBeesGame.setActionOfTheWeek(actionsOfTheWeek);
                }
//                    if ((month.equals(HarvestingMonths.APRIL) || month.equals(HarvestingMonths.MAY) || month.equals(HarvestingMonths.JUNE)) && date.getDayOfMonth() == 20) {
//                        actionsOfTheWeek.add("Do you want to collect honey from the hives?");
//                    } else {
//                        hive.addHoneyBatches(honey.makeHoneyBatch(hive, date));
//                    }

                hive.checkAndAddEggsToBees(date);
                hive.fillUpExistingHoneyFrameFromHive(date);
                hive.beesDie(date);
            }

            date = date.plusDays(1); // Actualizează data
            System.out.println("day is: " + date.getDayOfMonth());
            System.out.println("daily iterator is: " + dailyIterator);

            month = getHarvestingMonth(date);
        }

        apiary.honeyHarvestedByHoneyType();
        List<Hive> hives = apiary.getHives();

        if (date.isEqual(LocalDate.of(date.getYear(), 9, 30))) {
            for (Hive hive : hives) {
                apiary.hibernate(hive);
            }
            System.out.println("your apiary at the beginning of the new year is: " + apiary);

        }

        String newCurrentDate = date.format(DateTimeFormatter.ISO_LOCAL_DATE);
        lifeOfBeesGame.setCurrentDate(newCurrentDate);

        System.out.println("new date is: " + newCurrentDate);

        return new LifeOfBees(apiary, gameId, gameName, location, newCurrentDate, todayWeather.getSpeedWind(), todayWeather.getTemperature(), todayWeather.getPrecipitation(), moneyInTheBank, totalKgOfHoney, actionOfTheWeek);
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



    public double getSpeedWind() {
        return speedWind;
    }

    public double getPrecipitation() {
        return precipitation;
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

    public double getTotalKgOfHoney() {
        return totalKgOfHoney;
    }

    public void setTotalKgOfHoney(double totalKgOfHoney) {
        this.totalKgOfHoney = totalKgOfHoney;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }

    public List<ActionOfTheWeek> getActionOfTheWeek() {
        return actionOfTheWeek;
    }

    public void setActionOfTheWeek(List<ActionOfTheWeek> actionOfTheWeek) {
        this.actionOfTheWeek = actionOfTheWeek;
    }
}