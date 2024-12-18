package com.marianbastiurea.lifeofbees;

public class HarvestHoney {
    double Acacia = 0.0;
    double Rapeseed = 0.0;
    double WildFlower = 0.0;
    double Linden = 0.0;
    double SunFlower=0.0;
    double FalseIndigo=0;

    public double getAcacia() {
        return Acacia;
    }

    public void setAcacia(double acacia) {
        Acacia = acacia;
    }

    public double getRapeseed() {
        return Rapeseed;
    }

    public void setRapeseed(double rapeseed) {
        Rapeseed = rapeseed;
    }

    public double getWildFlower() {
        return WildFlower;
    }

    public void setWildFlower(double wildFlower) {
        WildFlower = wildFlower;
    }

    public double getLinden() {
        return Linden;
    }

    public void setLinden(double linden) {
        Linden = linden;
    }

    public double getSunFlower() {
        return SunFlower;
    }

    public void setSunFlower(double sunFlower) {
        SunFlower = sunFlower;
    }

    public double getFalseIndigo() {
        return FalseIndigo;
    }

    public void setFalseIndigo(double falseIndigo) {
        FalseIndigo = falseIndigo;
    }

    public HarvestHoney(double acacia, double rapeseed, double wildFlower, double linden, double sunFlower, double falseIndigo) {
        Acacia = acacia;
        Rapeseed = rapeseed;
        WildFlower = wildFlower;
        Linden = linden;
        SunFlower = sunFlower;
        FalseIndigo = falseIndigo;
    }

    public HarvestHoney() {
    }

    @Override
    public String toString() {
        return "HarvestHoney{" +
                "Acacia=" + Acacia +
                ", Rapeseed=" + Rapeseed +
                ", WildFlower=" + WildFlower +
                ", Linden=" + Linden +
                ", SunFlower=" + SunFlower +
                ", FalseIndigo=" + FalseIndigo +
                '}';
    }
}
