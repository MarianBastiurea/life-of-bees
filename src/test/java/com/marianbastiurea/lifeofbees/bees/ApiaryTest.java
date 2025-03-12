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
                        new HoneyBatch(1, 5.5, HoneyType.SunFlower, false),
                        new HoneyBatch(1, 5.5, HoneyType.Acacia, false),
                        new HoneyBatch(1, 4.5, HoneyType.Rapeseed, false),
                        new HoneyBatch(1, 5.5, HoneyType.Linden, false),
                        new HoneyBatch(1, 4.5, HoneyType.WildFlower, false),
                        new HoneyBatch(1, 4.5, HoneyType.FalseIndigo, false)
                ))),
                new Hive(2, new ArrayList<>(Arrays.asList(
                        new HoneyBatch(1, 5.5, HoneyType.SunFlower, false),
                        new HoneyBatch(1, 5.5, HoneyType.Acacia, false),
                        new HoneyBatch(1, 4.5, HoneyType.Rapeseed, false),
                        new HoneyBatch(1, 5.5, HoneyType.Linden, false),
                        new HoneyBatch(1, 4.5, HoneyType.WildFlower, false),
                        new HoneyBatch(1, 4.5, HoneyType.FalseIndigo, false)

                ))),
                new Hive(3, new ArrayList<>(Arrays.asList(
                        new HoneyBatch(1, 5.5, HoneyType.SunFlower, false),
                        new HoneyBatch(1, 5.5, HoneyType.Acacia, false),
                        new HoneyBatch(1, 4.5, HoneyType.Rapeseed, false),
                        new HoneyBatch(1, 5.5, HoneyType.Linden, false),
                        new HoneyBatch(1, 4.5, HoneyType.WildFlower, false),
                        new HoneyBatch(1, 4.5, HoneyType.FalseIndigo, false)

                )))
        );
        Hives hives = new Hives(hiveList);
        Apiary apiary = new Apiary(hives);
        apiary.honeyHarvestedByHoneyType();
        HarvestHoney totalHarvestedHoney = apiary.getTotalHarvestedHoney();

        assertEquals(16.5, totalHarvestedHoney.getHoneyAmount(HoneyType.SunFlower), 0.01);
        assertEquals(16.5, totalHarvestedHoney.getHoneyAmount(HoneyType.Acacia), 0.01);
        assertEquals(13.5, totalHarvestedHoney.getHoneyAmount(HoneyType.Rapeseed), 0.01);
        assertEquals(16.5, totalHarvestedHoney.getHoneyAmount(HoneyType.Linden), 0.01);
        assertEquals(13.5, totalHarvestedHoney.getHoneyAmount(HoneyType.WildFlower), 0.01);
        assertEquals(13.5, totalHarvestedHoney.getHoneyAmount(HoneyType.FalseIndigo), 0.01);
    }

    @Test
    void testUpdateHoneyStockSuccessfulSale() {

        HarvestHoney initialHarvest = new HarvestHoney(10.0, 5.0, 8.0, 12.0, 6.0, 4.0);
        Apiary apiary = new Apiary(initialHarvest);

        HarvestHoney soldHoney = new HarvestHoney(3.0, 1.0, 0.0, 2.0, 0.0, 0.0);
        apiary.updateHoneyStock(soldHoney);

        assertEquals(7.0, apiary.getTotalHarvestedHoney().getHoneyAmount(HoneyType.Acacia));
        assertEquals(4.0, apiary.getTotalHarvestedHoney().getHoneyAmount(HoneyType.Rapeseed));
        assertEquals(8.0, apiary.getTotalHarvestedHoney().getHoneyAmount(HoneyType.WildFlower));
        assertEquals(10.0, apiary.getTotalHarvestedHoney().getHoneyAmount(HoneyType.Linden));
        assertEquals(6.0, apiary.getTotalHarvestedHoney().getHoneyAmount(HoneyType.SunFlower));
        assertEquals(4.0, apiary.getTotalHarvestedHoney().getHoneyAmount(HoneyType.FalseIndigo));
    }
}

