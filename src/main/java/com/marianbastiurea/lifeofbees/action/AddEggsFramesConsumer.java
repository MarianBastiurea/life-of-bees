package com.marianbastiurea.lifeofbees.action;

import com.marianbastiurea.lifeofbees.bees.Hive;
import com.marianbastiurea.lifeofbees.bees.Hives;

import java.util.List;


public class AddEggsFramesConsumer extends HiveConsumer<List<Integer>> {

    @Override
    public void accept(Hives hives, List<Integer> eggHiveIds) {
        if (eggHiveIds != null) {
            eggHiveIds.forEach(hiveId -> {
                Hive hive = hives.getHiveById(hiveId);
                if (hive != null) {
                    hive.addNewEggsFrameInHive();
                }
            });
        }
    }
}
