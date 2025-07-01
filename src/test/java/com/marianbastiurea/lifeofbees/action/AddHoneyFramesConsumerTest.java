package com.marianbastiurea.lifeofbees.action;

import com.marianbastiurea.lifeofbees.bees.Hive;
import com.marianbastiurea.lifeofbees.bees.Hives;
import com.marianbastiurea.lifeofbees.bees.HoneyFrames;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AddHoneyFramesConsumerTest {


    @Test
    void addsFramesToSpecifiedHives() {
        AddHoneyFramesConsumerAbstract consumer = new AddHoneyFramesConsumerAbstract();
        Hives hives = new Hives(
                new Hive(1, new HoneyFrames(3, 0)),
                new Hive(2, new HoneyFrames(4, 0)),
                new Hive(3, new HoneyFrames(5, 4)));


        consumer.accept(hives, List.of(1, 2));

        assertEquals(new Hives(
                        new Hive(1, new HoneyFrames(4, 0)),
                        new Hive(2, new HoneyFrames(5, 0)),
                        new Hive(3, new HoneyFrames(5, 4))),
                hives);
    }

    @Test
    void doesNothingWhenHoneyHiveIdsIsEmpty() {
        AddHoneyFramesConsumerAbstract consumer = new AddHoneyFramesConsumerAbstract();
        Hives hives = new Hives(
                new Hive(1, new HoneyFrames(3, 0)),
                new Hive(2, new HoneyFrames(4, 0)),
                new Hive(3, new HoneyFrames(5, 4)));


        consumer.accept(hives, List.of());
        assertEquals(new Hives(
                        new Hive(1, new HoneyFrames(3, 0)),
                        new Hive(2, new HoneyFrames(4, 0)),
                        new Hive(3, new HoneyFrames(5, 4))),
                hives);
    }

}