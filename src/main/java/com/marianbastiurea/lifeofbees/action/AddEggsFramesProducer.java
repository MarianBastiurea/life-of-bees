package com.marianbastiurea.lifeofbees.action;

import com.marianbastiurea.lifeofbees.bees.Apiary;
import com.marianbastiurea.lifeofbees.bees.Hive;
import com.marianbastiurea.lifeofbees.game.LifeOfBees;

import java.util.ArrayList;
import java.util.List;

public class AddEggsFramesProducer implements ActionOfTheWeekProducer {

    @Override
    public Object produce(LifeOfBees lifeOfBees) {
        List<Integer> hiveIds = new ArrayList<>();
        Apiary apiary=lifeOfBees.getApiary();
        for (Hive hive : apiary.getHives()) {
            if(hive.getEggFrames().canAddNewEggsFrame())
                hiveIds.add(hive.getId());
        }
        return hiveIds;
    }
}
