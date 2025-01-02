package com.marianbastiurea.lifeofbees.action;

import com.marianbastiurea.lifeofbees.bees.Apiary;
import com.marianbastiurea.lifeofbees.game.LifeOfBees;

import java.time.LocalDate;

public class HibernateProducer implements ActionOfTheWeekProducer {
    @Override
    public Object produce(LifeOfBees lifeOfBees) {
        Apiary apiary=lifeOfBees.getApiary();
        return  apiary.hibernate();
    }
}
