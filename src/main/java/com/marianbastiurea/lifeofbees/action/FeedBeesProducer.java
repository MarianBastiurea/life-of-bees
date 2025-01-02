package com.marianbastiurea.lifeofbees.action;

import com.marianbastiurea.lifeofbees.bees.Apiary;
import com.marianbastiurea.lifeofbees.game.LifeOfBees;

import java.time.LocalDate;

public class FeedBeesProducer implements ActionOfTheWeekProducer {
    @Override
    public Object produce(LifeOfBees lifeOfBees) {
        Apiary apiary=lifeOfBees.getApiary();
        LocalDate currentDate=lifeOfBees.getCurrentDate();
        return  apiary.checkFeedBees(currentDate);
    }
}
