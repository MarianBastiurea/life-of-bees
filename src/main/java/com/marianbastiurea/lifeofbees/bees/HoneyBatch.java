package com.marianbastiurea.lifeofbees.bees;


import java.util.Objects;

public class HoneyBatch {
    private final int hiveId;
    private final double kgOfHoney;
    private final HoneyType honeyType;
    private boolean isProcessed;

    public HoneyBatch(int hiveId, double kgOfHoney, HoneyType honeyType, boolean isProcessed) {
        this.hiveId = hiveId;
        this.kgOfHoney = kgOfHoney;
        this.honeyType = honeyType;
        this.isProcessed = isProcessed;
    }

    public double getKgOfHoney() {
        return kgOfHoney;
    }

    public HoneyType getHoneyType() {
        return honeyType;
    }

    public boolean isProcessed() {
        return isProcessed;
    }

    public void setProcessed(boolean processed) {
        isProcessed = processed;
    }

    @Override
    public String toString() {
        return "HoneyBatch{" +
                "hiveId=" + hiveId +
                ", kgOfHoney=" + kgOfHoney +
                ", honeyType='" + honeyType + '\'' +
                ", isProcessed=" + isProcessed +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HoneyBatch that = (HoneyBatch) o;
        return hiveId == that.hiveId && Double.compare(kgOfHoney, that.kgOfHoney) == 0 && isProcessed == that.isProcessed && honeyType == that.honeyType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(hiveId, kgOfHoney, honeyType, isProcessed);
    }
}