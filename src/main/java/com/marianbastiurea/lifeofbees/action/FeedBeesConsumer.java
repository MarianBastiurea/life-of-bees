package com.marianbastiurea.lifeofbees.action;

import com.marianbastiurea.lifeofbees.bees.Hive;
import com.marianbastiurea.lifeofbees.game.LifeOfBees;

import static com.marianbastiurea.lifeofbees.bees.ApiaryParameters.PRICE_PER_HIVE_TO_FEED_BEES;

public class FeedBeesConsumer implements ActionOfTheWeekConsumer<String> {

    @Override
    public void accept(LifeOfBees lifeOfBees, String answer) {
        if ("yes".equals(answer)) {
            int cost = lifeOfBees.getApiary().getHives().getHives().size() * PRICE_PER_HIVE_TO_FEED_BEES;
            if (cost <= lifeOfBees.getMoneyInTheBank()) {
                lifeOfBees.setMoneyInTheBank(lifeOfBees.getMoneyInTheBank() - cost);
                for (Hive hive : lifeOfBees.getApiary().getHives().getHives()) {
                    hive.getQueen().setFeedBeesIndex(1);
                }
            }
        } else {
            for (Hive hive : lifeOfBees.getApiary().getHives().getHives()) {
                hive.getQueen().setFeedBeesIndex(0.7);
            }
        }
    }
}
