package com.marianbastiurea.lifeofbees.bees;

public enum HoneyType {
    ACACIA(1),
    RAPESEED(0.8),
    WILD_FLOWER(0.75),
    LINDEN(1),
    SUNFLOWER(0.8),
    FALSE_INDIGO(0.7);

    private final double productivity;

    HoneyType(double productivity) {
        this.productivity = productivity;
    }

    public double getProductivity() {
        return productivity;
    }

}
