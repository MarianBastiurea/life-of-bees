package com.marianbastiurea.lifeofbees;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Honey {
    private String honeyType;

    public Honey(String honeyType) {
        this.honeyType = honeyType;
    }

    public Honey() {
    }

    public String getHoneyType() {
        return honeyType;
    }

    public void setHoneyType(String honeyType) {
        this.honeyType = honeyType;
    }

    @Override
    public String toString() {
        return "Honey{" +
                "honeyType='" + honeyType + '\'' +
                // ", honeyKg=" + honeyKg +
                '}';
    }

    public static HarvestingMonths getHarvestingMonth(LocalDate date) {
        int monthValue = date.getMonthValue();
        if (monthValue < 3 || monthValue > 9) {
            throw new IllegalArgumentException("Luna nu este în intervalul de recoltare (martie-septembrie).");
        }
        return HarvestingMonths.values()[monthValue - 3];  // Martie corespunde indexului 0
    }


    public String honeyType(HarvestingMonths month, int dayOfMonth) {
        switch (month) {
            case MARCH, AUGUST, SEPTEMBER:
                    return  "WildFlower";
            case APRIL:
                if (dayOfMonth >= 1 && dayOfMonth <= 20) {
                   return "Rapeseed";
                } else if (dayOfMonth >= 21 && dayOfMonth <= 30) {
                    return "WildFlower";
                }
                break;
            case MAY:
                if (dayOfMonth >= 1 && dayOfMonth <= 20) {
                    return "Acacia";
                } else if (dayOfMonth >= 21 && dayOfMonth <= 31) {
                    return "FalseIndigo";
                }
                break;
            case JUNE:
                if (dayOfMonth >= 1 && dayOfMonth <= 20) {
                    return "Linden";
                } else if (dayOfMonth >= 21 && dayOfMonth <= 30) {
                    return "WildFlower";
                }
                break;
            case JULY:
                return "SunFlower";
            default:
                return "No honey type available";
        }
        return "No honey type available";
    }

    public double honeyProductivity() {
        int indexHoneyProductivity = 0;
        return switch (honeyType) {
            case "Acacia" -> 1;//kgOnHa=1600
            case "Rapeseed" -> 0.8; //kgOnHa=50
            case "WildFlower" -> 0.75;//kgOnHa=40
            case "Linden" -> 1;//kgOnHa=1200
            case "SunFlower" -> 0.8;//kgOnHa=60
            case "FalseIndigo" -> 0.7;
            default ->//kgOnHa=70
                    indexHoneyProductivity;
        };
    }

    public List<HoneyBatch> makeHoneyBatch(Hive hive, LocalDate currentDate) {
        List<HoneyBatch> honeyBatches = new ArrayList<>();

        double kgOfHoney = 0;
        int frameCounter = 0;

        if (hive.isAnswerIfWantToSplit() && hive.checkIfAll6EggsFrameAre80PercentFull()) {
            List<HoneyFrame> hiveHoneyFrames = hive.getHoneyFrames();
            for (HoneyFrame honeyFrame : hiveHoneyFrames) {
                if (honeyFrame.getKgOfHoney() > 3) {
                    frameCounter++;
                    kgOfHoney += honeyFrame.getKgOfHoney();
                    honeyFrame.setKgOfHoney(0);
                }
            }

            if (kgOfHoney > 0) {
                HoneyBatch honeyBatch = new HoneyBatch(hive.getId(), currentDate, kgOfHoney,
                        getHoneyType(), frameCounter);
                honeyBatches.add(honeyBatch);
            }
        }
        return honeyBatches;
    }
}
