package com.marianbastiurea.lifeofbees.time;

import java.time.LocalDate;
import java.time.Month;


public class BeeTime {

    private LocalDate currentDate;

    public BeeTime(LocalDate currentDate) {
        this.currentDate = currentDate;
    }

    public LocalDate getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(LocalDate currentDate) {
        this.currentDate = currentDate;
    }

    public static boolean timeToSplitHive(LocalDate currentDate) {
        Month month = currentDate.getMonth();
        int dayOfMonth = currentDate.getDayOfMonth();
        return (month == Month.APRIL || month == Month.MAY) &&
                (dayOfMonth == 1 || dayOfMonth == 10);
    }


}
