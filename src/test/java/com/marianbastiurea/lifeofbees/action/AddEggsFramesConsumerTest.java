package com.marianbastiurea.lifeofbees.action;

import com.marianbastiurea.lifeofbees.bees.EggFrames;
import com.marianbastiurea.lifeofbees.bees.Hive;
import com.marianbastiurea.lifeofbees.bees.Hives;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AddEggsFramesConsumerTest {

    @Test
    void addsFramesToSpecifiedHives() {
        AddEggsFramesConsumerAbstract consumer = new AddEggsFramesConsumerAbstract();

        Hives hives = new Hives(
                new Hive(1, new EggFrames(4, new LinkedList<>(Arrays.asList(6400, 6400, 6400, 6400)), false)),
                new Hive(2, new EggFrames(3, new LinkedList<>(Arrays.asList(6400, 6400, 6400)), false)),
                new Hive(3, new EggFrames(6, new LinkedList<>(Arrays.asList(6400, 6400, 6400, 6400, 6400, 6400)), false)));

        consumer.accept(hives, List.of(1, 2));

        assertEquals(new Hives(
                        new Hive(1, new EggFrames(5, new LinkedList<>(Arrays.asList(6400, 6400, 6400, 6400)), false)),
                        new Hive(2, new EggFrames(4, new LinkedList<>(Arrays.asList(6400, 6400, 6400)), false)),
                        new Hive(3, new EggFrames(6, new LinkedList<>(Arrays.asList(6400, 6400, 6400, 6400, 6400, 6400)), false))),
                hives);
    }

    @Test
    void doesNothingWhenEggHiveIdsIsEmpty() {
        AddEggsFramesConsumerAbstract consumer = new AddEggsFramesConsumerAbstract();

        Hives hives = new Hives(
                new Hive(1, new EggFrames(2, 0.8)),
                new Hive(2, new EggFrames(3, 0.8)));

        consumer.accept(hives, List.of());

        assertEquals(new Hives(
                        new Hive(1, new EggFrames(2, 0.8)),
                        new Hive(2, new EggFrames(3, 0.8))),
                hives);
    }

}