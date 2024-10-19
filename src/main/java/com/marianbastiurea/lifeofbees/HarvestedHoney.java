package com.marianbastiurea.lifeofbees;

import java.time.LocalDate;
import java.util.Date;

public class HarvestedHoney {
    private int hiveId;
    private String honeyType;
    private double kgOfHoney;
    private final LocalDate date;

    public HarvestedHoney(int hiveId,
                          String honeyType, double kgOfHoney, LocalDate date) {

        this.hiveId = hiveId;
        this.honeyType = honeyType;
        this.kgOfHoney = kgOfHoney;
        this.date=date;
    }


    public int getHiveId() {
        return hiveId;
    }

    public void setHiveId(int hiveId) {
        this.hiveId = hiveId;
    }


    public String getHoneyType() {
        return honeyType;
    }

    public void setHoneyType(String honeyType) {
        this.honeyType = honeyType;
    }

    public double getKgOfHoney() {
        return kgOfHoney;
    }

    public void setKgOfHoney(double kgOfHoney) {
        this.kgOfHoney = kgOfHoney;
    }

    public LocalDate getDate() {
        return date;
    }

    @Override
    public String toString() {
        return "HarvestedHoney{" +
                "hiveID=" + hiveId +
                ", honeyType='" + honeyType + '\'' +
                ", kgOfHoney=" + kgOfHoney +
                ", date=" + date +
                '}';
    }

}

