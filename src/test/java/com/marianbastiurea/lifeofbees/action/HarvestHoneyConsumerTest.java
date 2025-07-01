package com.marianbastiurea.lifeofbees.action;

import com.marianbastiurea.lifeofbees.bees.*;
import com.marianbastiurea.lifeofbees.time.BeeTime;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HarvestHoneyConsumerTest {


    @Test
    void createFirstHoneyBatch() {
        HarvestHoneyConsumerAbstract harvestHoneyConsumer = new HarvestHoneyConsumerAbstract();

        HoneyFrames beforeHarvestingHoneyFrames = new HoneyFrames(List.of(
                new HoneyFrame(4.0)));

        HoneyFrames afterHarvestingHoneyFrames = new HoneyFrames(List.of(
                new HoneyFrame(0)));


        Hives initialHive = new Hives(Collections.singletonList(new Hive(1, beforeHarvestingHoneyFrames)), new BeeTime(2023, 5, 15));
        System.out.println("Hives inainte de : " + initialHive);
        harvestHoneyConsumer.accept(initialHive, Collections.singletonList(1));
        System.out.println("Hives dupa: " + initialHive);
        assertEquals(initialHive, new Hives(Collections.singletonList(new Hive(1, afterHarvestingHoneyFrames,
                List.of(new HoneyBatch(1, 4.0, HoneyType.ACACIA, false)))), new BeeTime(2023, 5, 15)));

    }

    @Test
    void addSecondHoneyBatchSameHoneyType() {
        HarvestHoneyConsumerAbstract harvestHoneyConsumer = new HarvestHoneyConsumerAbstract();

        HoneyFrames beforeHarvestingHoneyFrames = new HoneyFrames(Arrays.asList(
                new HoneyFrame(3.0), new HoneyFrame(4.5), new HoneyFrame(3.8),
                new HoneyFrame(4.5), new HoneyFrame(3.8), new HoneyFrame(4.5),
                new HoneyFrame(3.8)));

        HoneyFrames afterHarvestingHoneyFrames = new HoneyFrames(Arrays.asList(
                new HoneyFrame(3), new HoneyFrame(0), new HoneyFrame(3.8),
                new HoneyFrame(0), new HoneyFrame(3.8), new HoneyFrame(0),
                new HoneyFrame(3.8)));

        List<HoneyBatch> acaciaHoneyBatches = List.of(
                new HoneyBatch(1, 17.5, HoneyType.ACACIA, false));

        List<HoneyBatch> doubleAcaciaHoneyBatches = List.of(
                new HoneyBatch(1, 17.5, HoneyType.ACACIA, false),
                new HoneyBatch(1, 13.5, HoneyType.ACACIA, false));

        Hives initialHive = new Hives(Collections.singletonList(new Hive(1, beforeHarvestingHoneyFrames, acaciaHoneyBatches)), new BeeTime(2023, 5, 15));
        harvestHoneyConsumer.accept(initialHive, Collections.singletonList(1));
        Hives afterHarvestingHive = new Hives(Collections.singletonList(new Hive(1, afterHarvestingHoneyFrames, doubleAcaciaHoneyBatches)), new BeeTime(2023, 5, 15));
        assertEquals(initialHive, afterHarvestingHive);

    }

    @Test
    void addSecondHoneyBatchDifferentHoneyType() {
        HarvestHoneyConsumerAbstract harvestHoneyConsumer = new HarvestHoneyConsumerAbstract();

        HoneyFrames beforeHarvestingHoneyFrames = new HoneyFrames(Arrays.asList(
                new HoneyFrame(3.0), new HoneyFrame(4.5), new HoneyFrame(3.8),
                new HoneyFrame(4.5), new HoneyFrame(3.8), new HoneyFrame(4.5),
                new HoneyFrame(3.8)));

        HoneyFrames afterHarvestingHoneyFrames = new HoneyFrames(Arrays.asList(
                new HoneyFrame(3), new HoneyFrame(0), new HoneyFrame(3.8),
                new HoneyFrame(0), new HoneyFrame(3.8), new HoneyFrame(0),
                new HoneyFrame(3.8)));

        List<HoneyBatch> acaciaHoneyBatches = List.of(
                new HoneyBatch(1, 17.5, HoneyType.ACACIA, false));

        List<HoneyBatch> doubleDifferentHoneyTypeHoneyBatches = List.of(
                new HoneyBatch(1, 17.5, HoneyType.ACACIA, false),
                new HoneyBatch(1, 13.5, HoneyType.RAPESEED, false));
        Hives initialHive = new Hives(Collections.singletonList(new Hive(1, beforeHarvestingHoneyFrames, acaciaHoneyBatches)), new BeeTime(2023, 4, 15));
        harvestHoneyConsumer.accept(initialHive, Collections.singletonList(1));
        Hives afterHarvestingHive = new Hives(Collections.singletonList(new Hive(1, afterHarvestingHoneyFrames, doubleDifferentHoneyTypeHoneyBatches)), new BeeTime(2023, 4, 15));
        assertEquals(initialHive, afterHarvestingHive);

    }
}