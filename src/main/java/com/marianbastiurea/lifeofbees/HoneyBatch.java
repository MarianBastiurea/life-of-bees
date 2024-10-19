package com.marianbastiurea.lifeofbees;
import java.time.LocalDate;
import java.util.Date;


class HoneyBatch {
    private final int hiveId;
    private final LocalDate creationDate;
    private final double kgOfHoney;
    private final String  honeyType;
    private final int frameCounter;

    public HoneyBatch(int hiveId, LocalDate creationDate, double kgOfHoney, String honeyType, int frameCounter) {
        this.hiveId = hiveId;
        this.creationDate = creationDate;
        this.kgOfHoney = kgOfHoney;
        this.honeyType = honeyType;
        this.frameCounter=frameCounter;
    }

    public int getHiveId() {
        return hiveId;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public double getKgOfHoney() {
        return kgOfHoney;
    }

    public String getHoneyType() {
        return honeyType;
    }

    @Override
    public String toString() {
        return "HoneyBatch{" +
                "hiveId=" + hiveId +
                ", creationDate=" + creationDate +
                ", kgOfHoney=" + kgOfHoney +
                ", honeyType='" + honeyType + '\'' +
                ", frameCounter=" + frameCounter +
                '}';
    }
}
