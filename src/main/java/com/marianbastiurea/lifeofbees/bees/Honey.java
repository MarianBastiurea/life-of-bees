package com.marianbastiurea.lifeofbees.bees;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

public class Honey {

    public Honey() {
    }

    public static HoneyType honeyType(LocalDate currentDate) {
        Month month = currentDate.getMonth();
        int dayOfMonth = currentDate.getDayOfMonth();

        return switch (month) {
            case MARCH, AUGUST, SEPTEMBER -> HoneyType.WildFlower;
            case APRIL -> (dayOfMonth >= 1 && dayOfMonth <= 20)
                    ? HoneyType.Rapeseed
                    : HoneyType.WildFlower;
            case MAY -> (dayOfMonth >= 1 && dayOfMonth <= 20)
                    ? HoneyType.Acacia
                    : HoneyType.FalseIndigo;
            case JUNE -> (dayOfMonth >= 1 && dayOfMonth <= 20)
                    ? HoneyType.Linden
                    : HoneyType.WildFlower;
            case JULY -> HoneyType.SunFlower;
            default -> null;
        };
    }


    public static double honeyProductivity(HoneyType honeyType) {
        return switch (honeyType) {
            case Acacia -> 1;
            case Rapeseed -> 0.8;
            case WildFlower -> 0.75;
            case Linden -> 1;
            case SunFlower -> 0.8;
            case FalseIndigo -> 0.7;
        };
    }

    public static List<HoneyBatch> harvestHoney(Hive hive, LocalDate currentDate) {
        List<HoneyBatch> honeyBatches = new ArrayList<>();
        int dayOfMonth = currentDate.getDayOfMonth();
        if ((currentDate.getMonthValue() >= 4 && currentDate.getMonthValue() <= 8)
                && (dayOfMonth == 10 || dayOfMonth == 20) && !hive.isItWasSplit()) {
            HoneyFrames honeyFrames = hive.getHoneyFrames();
            double harvestedHoney = honeyFrames.harvestHoneyFromHoneyFrames();
            if (harvestedHoney > 0) {
                HoneyBatch honeyBatch = new HoneyBatch(
                        hive.getId(),
                        harvestedHoney,
                        honeyType(currentDate),
                        false
                );
                honeyBatches.add(honeyBatch);
            }
        }
        return honeyBatches;
    }

}

