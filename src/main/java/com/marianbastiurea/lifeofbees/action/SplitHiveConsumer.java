package com.marianbastiurea.lifeofbees.action;

import com.marianbastiurea.lifeofbees.bees.Hive;
import com.marianbastiurea.lifeofbees.bees.Hives;

import java.util.List;


public class SplitHiveConsumer extends HiveConsumer<List<Integer>> {
    @Override
    public void accept(Hives hives, List<Integer> hiveIds) {
        if (hiveIds != null) {
            hiveIds.forEach(hiveId -> {
                Hive hive = hives.getHiveById(hiveId);
                if (hive != null) {
                    hives.splitHive(hive.getId());
                }
            });
        }
    }
}

