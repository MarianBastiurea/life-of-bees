package com.marianbastiurea.lifeofbees;

class HoneyFrame {
    private double kgOfHoney;
    //todo : remove honeyType field.
    private String honeyType;

    public HoneyFrame(double kgOfHoney, String honeyType) {
        this.kgOfHoney = kgOfHoney;
        this.honeyType = honeyType;
    }

    public HoneyFrame() {

    }

    public double getKgOfHoney() {
        return kgOfHoney;
    }

    public void setKgOfHoney(double kgOfHoney) {
        this.kgOfHoney = kgOfHoney;
    }

    public String getHoneyType() {
        return honeyType;
    }

    public void setHoneyType(String honeyType) {
        this.honeyType = honeyType;
    }

    @Override
    public String toString() {
        return "{" +
                "kgOfHoney=" + kgOfHoney +
                ", honeyType='" + honeyType + '\'' +
                '}';
    }
}
