package com.marianbastiurea.lifeofbees.action;

import com.marianbastiurea.lifeofbees.bees.Apiary;
import com.marianbastiurea.lifeofbees.bees.Hive;
import com.marianbastiurea.lifeofbees.game.LifeOfBees;

import java.util.List;

public class SplitHiveConsumer implements ActionOfTheWeekConsumer {

    @Override
    public void accept(LifeOfBees lifeOfBees, Object data) {
        List<Integer> hiveIds = (List<Integer>) data;
        Apiary apiary = lifeOfBees.getApiary();
        if (hiveIds != null) {
            hiveIds.forEach(hiveId -> {
                Hive hive = apiary.getHiveById(hiveId);
                if (hive != null) {
                    apiary.splitHive(hive);
                }
            });
        }
    }
}
