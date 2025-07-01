package com.marianbastiurea.lifeofbees.action;

import com.marianbastiurea.lifeofbees.bees.Hive;
import com.marianbastiurea.lifeofbees.bees.Hives;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


public class HarvestHoneyConsumerAbstract extends HiveConsumerAbstract<List<Integer>> {
    private static final Logger logger = LoggerFactory.getLogger(HarvestHoneyConsumerAbstract.class);


    @Override
    public void accept(Hives hives, List<Integer> hiveIds) {
        logger.info("starting method  HarvestHoneyConsumer with list: {}", hiveIds);

        if (hiveIds != null) {
            hiveIds.forEach(hiveId -> {
                Hive hive = hives.getHiveById(hiveId);
                if (hive != null) {
                    hive.harvestHoney(hives.getCurrentDate());
                    logger.info("this is honeyBatch {} from hive {}", hive.getHoneyBatches(), hiveId);
                }
            });
        }

    }
}


