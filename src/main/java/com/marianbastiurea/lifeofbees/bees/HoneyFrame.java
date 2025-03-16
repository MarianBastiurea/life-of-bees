package com.marianbastiurea.lifeofbees.bees;

import java.util.Objects;

import static com.marianbastiurea.lifeofbees.bees.ApiaryParameters.MAX_KG_OF_HONEY_PER_FRAME;
import static com.marianbastiurea.lifeofbees.bees.ApiaryParameters.MIN_KG_OF_HONEY_TO_HARVEST_A_HONEY_FRAME;

public class HoneyFrame {


    private double kgOfHoney;

    public HoneyFrame(double kgOfHoney) {
        this.kgOfHoney = kgOfHoney;
    }

    public void fill(double kgOfHoneyToAdd) {
        if (kgOfHoney < MAX_KG_OF_HONEY_PER_FRAME) {
            kgOfHoney = Math.min(MAX_KG_OF_HONEY_PER_FRAME, kgOfHoney + kgOfHoneyToAdd);
        }
    }

    public boolean isHarvestable() {
        return kgOfHoney >= MIN_KG_OF_HONEY_TO_HARVEST_A_HONEY_FRAME;
    }

    public boolean isFull() {
        return kgOfHoney == MAX_KG_OF_HONEY_PER_FRAME;
    }

    public double harvest() {
        double harvest = kgOfHoney;
        kgOfHoney = 0;
        return harvest;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HoneyFrame that = (HoneyFrame) o;
        return Double.compare(kgOfHoney, that.kgOfHoney) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(MAX_KG_OF_HONEY_PER_FRAME, kgOfHoney);
    }

    @Override
    public String toString() {
        return "HoneyFrame{" +
                "maxKgOfHoneyPerFrame=" + MAX_KG_OF_HONEY_PER_FRAME +
                ", kgOfHoney=" + kgOfHoney +
                '}';
    }
}
