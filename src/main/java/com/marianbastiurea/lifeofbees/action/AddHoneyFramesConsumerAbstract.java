package com.marianbastiurea.lifeofbees.action;

import com.marianbastiurea.lifeofbees.bees.Hive;
import com.marianbastiurea.lifeofbees.bees.Hives;

import java.util.List;

public class AddHoneyFramesConsumerAbstract extends HiveConsumerAbstract<List<Integer>> {


    @Override
    public void accept(Hives hives, List<Integer> honeyHiveIds) {
        if (honeyHiveIds != null) {
            honeyHiveIds.forEach(hiveId -> {
                Hive hive = hives.getHiveById(hiveId);
                if (hive != null) {
                    hive.getHoneyFrames().addNewHoneyFrameInHive();
                }
            });
        }
    }
}