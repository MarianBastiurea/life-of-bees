package com.marianbastiurea.lifeofbees.action;

import com.marianbastiurea.lifeofbees.bees.Apiary;

import com.marianbastiurea.lifeofbees.game.LifeOfBees;

import java.util.List;

public class FeedBeesConsumer implements ActionOfTheWeekConsumer {


    @Override
    public void accept(LifeOfBees lifeOfBees, Object data) {
        String answer = (String) data;
        Apiary apiary = lifeOfBees.getApiary();
        if ("yes".equals(answer)) {
            int cost = apiary.getHives().size() * 7;
            lifeOfBees.setMoneyInTheBank(lifeOfBees.getMoneyInTheBank() - cost);
        } else {
            apiary.doFeedBees();
        }
    }

}
