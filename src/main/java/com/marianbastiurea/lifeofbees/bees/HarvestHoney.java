package com.marianbastiurea.lifeofbees.bees;

import java.util.EnumMap;
import java.util.Map;

public class HarvestHoney {
    private Map<HoneyType, Double> honeyTypeToAmount;

    public HarvestHoney() {
        honeyTypeToAmount = new EnumMap<>(HoneyType.class);
        for (HoneyType type : HoneyType.values()) {
            honeyTypeToAmount.put(type, 0.0);
        }
    }

    public HarvestHoney(double acacia, double rapeseed, double wildFlower, double linden, double sunFlower, double falseIndigo) {
        this();
        honeyTypeToAmount.put(HoneyType.ACACIA, acacia);
        honeyTypeToAmount.put(HoneyType.RAPESEED, rapeseed);
        honeyTypeToAmount.put(HoneyType.WILD_FLOWER, wildFlower);
        honeyTypeToAmount.put(HoneyType.LINDEN, linden);
        honeyTypeToAmount.put(HoneyType.SUNFLOWER, sunFlower);
        honeyTypeToAmount.put(HoneyType.FALSE_INDIGO, falseIndigo);
    }

    public double getHoneyAmount(HoneyType type) {
        return honeyTypeToAmount.getOrDefault(type, 0.0);
    }

    public void setHoneyAmount(HoneyType type, double amount) {
        honeyTypeToAmount.put(type, amount);
    }

    public Map<HoneyType, Double> getHoneyTypeToAmount() {
        return honeyTypeToAmount;
    }

    public void setHoneyTypeToAmount(Map<HoneyType, Double> honeyTypeToAmount) {
        this.honeyTypeToAmount = honeyTypeToAmount;
    }

    @Override
    public String toString() {
        return "HarvestHoney{" +
                "honeyTypeToAmount=" + honeyTypeToAmount +
                '}';
    }
}
