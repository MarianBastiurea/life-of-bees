package com.marianbastiurea.lifeofbees.action;

import com.marianbastiurea.lifeofbees.bees.Apiary;
import com.marianbastiurea.lifeofbees.bees.Hive;
import com.marianbastiurea.lifeofbees.game.LifeOfBees;

import java.util.List;

public class AddHoneyFramesConsumer implements ActionOfTheWeekConsumer<List<Integer>> {


    @Override
    public void accept(LifeOfBees lifeOfBees, List<Integer> honeyHiveIds) {
        Apiary apiary = lifeOfBees.getApiary();
        if (honeyHiveIds != null) {
            honeyHiveIds.forEach(hiveId -> {
                Hive hive = apiary.getHiveById(hiveId);
                if (hive != null) {
                    hive.getHoneyFrames().addNewHoneyFrameInHive();
                }
            });
        }
    }
}