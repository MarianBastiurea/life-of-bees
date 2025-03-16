package com.marianbastiurea.lifeofbees.bees;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ApiaryTest {

    @Test
    void harvestingHoneyFromHives() {

        List<Hive> hiveList = Arrays.asList(
                new Hive(1, new ArrayList<>(Arrays.asList(
                        new HoneyBatch(1, 5.5, HoneyType.SUNFLOWER, false),
                        new HoneyBatch(1, 5.5, HoneyType.ACACIA, false),
                        new HoneyBatch(1, 4.5, HoneyType.RAPESEED, false),
                        new HoneyBatch(1, 5.5, HoneyType.LINDEN, false),
                        new HoneyBatch(1, 4.5, HoneyType.WILD_FLOWER, false),
                        new HoneyBatch(1, 4.5, HoneyType.FALSE_INDIGO, false)
                ))),
                new Hive(2, new ArrayList<>(Arrays.asList(
                        new HoneyBatch(1, 5.5, HoneyType.SUNFLOWER, false),
                        new HoneyBatch(1, 5.5, HoneyType.ACACIA, false),
                        new HoneyBatch(1, 4.5, HoneyType.RAPESEED, false),
                        new HoneyBatch(1, 5.5, HoneyType.LINDEN, false),
                        new HoneyBatch(1, 4.5, HoneyType.WILD_FLOWER, false),
                        new HoneyBatch(1, 4.5, HoneyType.FALSE_INDIGO, false)

                ))),
                new Hive(3, new ArrayList<>(Arrays.asList(
                        new HoneyBatch(1, 5.5, HoneyType.SUNFLOWER, false),
                        new HoneyBatch(1, 5.5, HoneyType.ACACIA, false),
                        new HoneyBatch(1, 4.5, HoneyType.RAPESEED, false),
                        new HoneyBatch(1, 5.5, HoneyType.LINDEN, false),
                        new HoneyBatch(1, 4.5, HoneyType.WILD_FLOWER, false),
                        new HoneyBatch(1, 4.5, HoneyType.FALSE_INDIGO, false)

                )))
        );
        Hives hives = new Hives(hiveList);
        Apiary apiary = new Apiary(hives);
        apiary.honeyHarvestedByHoneyType();
        HarvestHoney totalHarvestedHoney = apiary.getTotalHarvestedHoney();

        assertEquals(16.5, totalHarvestedHoney.getHoneyAmount(HoneyType.SUNFLOWER), 0.01);
        assertEquals(16.5, totalHarvestedHoney.getHoneyAmount(HoneyType.ACACIA), 0.01);
        assertEquals(13.5, totalHarvestedHoney.getHoneyAmount(HoneyType.RAPESEED), 0.01);
        assertEquals(16.5, totalHarvestedHoney.getHoneyAmount(HoneyType.LINDEN), 0.01);
        assertEquals(13.5, totalHarvestedHoney.getHoneyAmount(HoneyType.WILD_FLOWER), 0.01);
        assertEquals(13.5, totalHarvestedHoney.getHoneyAmount(HoneyType.FALSE_INDIGO), 0.01);
    }

    @Test
    void testUpdateHoneyStockSuccessfulSale() {

        HarvestHoney initialHarvest = new HarvestHoney(10.0, 5.0, 8.0, 12.0, 6.0, 4.0);
        Apiary apiary = new Apiary(initialHarvest);

        HarvestHoney soldHoney = new HarvestHoney(3.0, 1.0, 0.0, 2.0, 0.0, 0.0);
        apiary.updateHoneyStock(soldHoney);

        assertEquals(7.0, apiary.getTotalHarvestedHoney().getHoneyAmount(HoneyType.ACACIA));
        assertEquals(4.0, apiary.getTotalHarvestedHoney().getHoneyAmount(HoneyType.RAPESEED));
        assertEquals(8.0, apiary.getTotalHarvestedHoney().getHoneyAmount(HoneyType.WILD_FLOWER));
        assertEquals(10.0, apiary.getTotalHarvestedHoney().getHoneyAmount(HoneyType.LINDEN));
        assertEquals(6.0, apiary.getTotalHarvestedHoney().getHoneyAmount(HoneyType.SUNFLOWER));
        assertEquals(4.0, apiary.getTotalHarvestedHoney().getHoneyAmount(HoneyType.FALSE_INDIGO));
    }
}

