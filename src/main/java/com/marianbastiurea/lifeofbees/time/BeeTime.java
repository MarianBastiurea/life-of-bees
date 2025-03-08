package com.marianbastiurea.lifeofbees.time;

import com.marianbastiurea.lifeofbees.bees.HoneyType;

import java.time.LocalDate;
import java.time.Month;
import java.util.Objects;


public class BeeTime {

    private LocalDate currentDate;

    public BeeTime(LocalDate currentDate) {
        this.currentDate = currentDate;
    }

    public BeeTime(int year, int month, int day) {
        this.currentDate = LocalDate.of(year, month, day);
    }

    public BeeTime() {
    }


    public BeeTime(String date) {
        this.currentDate = LocalDate.parse(date);
    }

    public boolean timeToSplitHive() {
        Month month = currentDate.getMonth();
        int dayOfMonth = currentDate.getDayOfMonth();
        return (month == Month.APRIL || month == Month.MAY)
                && ((dayOfMonth >= 9 && dayOfMonth <= 16) ||
                (dayOfMonth >= 19 && dayOfMonth <= 25));
    }

    public boolean timeToHarvestHive() {
        int month = currentDate.getMonthValue();
        int day = currentDate.getDayOfMonth();

        boolean isHarvestMonth = (month >= 4 && month <= 8);
        boolean isHarvestDay = (day >= 9 && day <= 17) || (day >= 19 && day <= 26);

        return isHarvestMonth && isHarvestDay;
    }


    public HoneyType honeyType() {
        Month month = currentDate.getMonth();
        int dayOfMonth = currentDate.getDayOfMonth();
        System.out.println("luna este: "+month+" si siua este: "+dayOfMonth);
        return switch (month) {
            case MARCH, AUGUST, SEPTEMBER -> HoneyType.WildFlower;
            case APRIL -> dayOfMonth <= 20
                    ? HoneyType.Rapeseed
                    : HoneyType.WildFlower;
            case MAY -> dayOfMonth <= 20
                    ? HoneyType.Acacia
                    : HoneyType.FalseIndigo;
            case JUNE -> dayOfMonth <= 20
                    ? HoneyType.Linden
                    : HoneyType.WildFlower;
            case JULY -> HoneyType.SunFlower;
            default -> HoneyType.WildFlower;
        };
    }

    public LocalDate getLocalDate() {
        return currentDate;
    }

    public void setCurrentDate(LocalDate currentDate) {
        this.currentDate = currentDate;
    }

    public Month getMonth() {
        return currentDate.getMonth();
    }

    public Integer getYear() {
        return currentDate.getYear();
    }

    public void addDay() {
        this.currentDate = this.currentDate.plusDays(1);
    }

    public BeeTime addingDays(long days) {
        return new BeeTime(this.currentDate.plusDays(days));
    }


    public boolean timeForInsectControl() {
        int dayOfMonth = currentDate.getDayOfMonth();
        int month = currentDate.getMonthValue();
        boolean isValidMonth = (month >= 4 && month <= 8);
        boolean isValidDay = (dayOfMonth >= 9 && dayOfMonth <= 16) || (dayOfMonth >= 19 && dayOfMonth <= 25);
        return isValidMonth && isValidDay;
    }


    public boolean isEndOfSeason() {
        return currentDate.isEqual(LocalDate.of(currentDate.getYear(), 9, 30));
    }

    public String toFormattedDate() {
        return currentDate.toString(); // Format implicit ISO-8601: YYYY-MM-DD
    }

    @Override
    public String toString() {
        return "{" +
                "currentDate=" + currentDate +
                '}';
    }

    public boolean canFeedBees() {
        return currentDate.getMonth() == Month.SEPTEMBER;
    }

    public boolean checkInsectControl() {

        return this.timeForInsectControl();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        BeeTime beeTime = (BeeTime) obj;
        return Objects.equals(currentDate, beeTime.currentDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(currentDate);
    }


    public boolean isTimeToChangeQueen() {
        Month month = currentDate.getMonth();
        int dayOfMonth = currentDate.getDayOfMonth();
        return month == Month.MAY && dayOfMonth == 1;
    }


    public void changeYear() {
        currentDate = currentDate.plusYears(1).withMonth(3).withDayOfMonth(1);
    }
}

