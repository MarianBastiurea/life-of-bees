package com.marianbastiurea.lifeofbees.action;

import com.marianbastiurea.lifeofbees.bees.Apiary;
import com.marianbastiurea.lifeofbees.bees.Hive;
import com.marianbastiurea.lifeofbees.game.LifeOfBees;

import java.util.ArrayList;
import java.util.List;

public class HarvestHoneyProducer implements ActionOfTheWeekProducer {

    @Override
    public Object produce(LifeOfBees lifeOfBees) {
        List<Integer> hiveIds = new ArrayList<>();
        Apiary apiary = lifeOfBees.getApiary();

        for (Hive hive : apiary.getHives()) {
            boolean hasUnprocessedBatch = hive.getHoneyBatches().stream()
                    .anyMatch(batch -> !batch.isProcessed());
            if (hasUnprocessedBatch) {
                hiveIds.add(hive.getId());
            }
        }

        return hiveIds;
    }
}
