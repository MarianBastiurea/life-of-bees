package com.marianbastiurea.lifeofbees.action;

import com.marianbastiurea.lifeofbees.bees.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SplitHiveConsumerAbstractTest {

    private static BeesBatches createBeesBatches(int x, int e) {
        BeesBatches beesBatches = new BeesBatches();
        for (int i = 0; i < x; i++) {
            beesBatches.add(e);
        }
        return beesBatches;
    }

    @Test
    void testSplitHiveWithValidHiveId() {

        EggFrames eggFrames = new EggFrames(6, 20, false);
        EggFrames halfEggFrames = new EggFrames(3, 10, false);
        HoneyFrames honeyFrames = new HoneyFrames(4, 3);
        HoneyFrames halfHoneyFrames = new HoneyFrames(2, 3);
        BeesBatches beesBatches = createBeesBatches(10, 100);
        BeesBatches halfBeesBatches = createBeesBatches(10, 50);
        Hives hives = new Hives(new Hive(1, false, eggFrames, honeyFrames, beesBatches, new ArrayList<>(), new Queen(1)));
        hives.splitHive(1);

        Hives actual = new Hives(
                new Hive(1, true, halfEggFrames, halfHoneyFrames, halfBeesBatches, new ArrayList<>(), new Queen(1)),
                new Hive(2, true, halfEggFrames, halfHoneyFrames, halfBeesBatches, new ArrayList<>(), new Queen(0)));

        assertEquals(actual, hives);
    }

    @Test
    void testSplitHiveWithNoValidHiveId() {

        Hives hives = new Hives();
        SplitHiveConsumerAbstract splitHiveConsumer = new SplitHiveConsumerAbstract();
        splitHiveConsumer.accept(hives, Collections.emptyList());
        assertEquals(0, hives.getHives().size(), "Hives should remain empty when no valid IDs are provided.");

    }
}