package com.marianbastiurea.lifeofbees.action;

import com.marianbastiurea.lifeofbees.bees.EggFrames;
import com.marianbastiurea.lifeofbees.bees.Hive;
import com.marianbastiurea.lifeofbees.bees.Hives;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MoveAnEggsFrameConsumerTest {
    @Test
    void moveAnEggFrame() {
        EggFrames initialSourceEggFrames = new EggFrames(6, 120, false);
        EggFrames finalSourceEggFrames = new EggFrames(5, 100, true);
        EggFrames initialDestinationEggFrames = new EggFrames(4, 100, false);
        EggFrames finalDestinationEggFrames = new EggFrames(5, 120, false);
        Hives hives = new Hives(new Hive(1, initialSourceEggFrames, false),
                new Hive(2, initialDestinationEggFrames, true));
        List<Integer> hiveIdPair = List.of(1, 2);


        MoveAnEggsFrameConsumerAbstract moveAnEggsFrameConsumer = new MoveAnEggsFrameConsumerAbstract();
        moveAnEggsFrameConsumer.accept(hives, List.of(hiveIdPair));

        Hives actual = new Hives(new Hive(1, finalSourceEggFrames, false),
                new Hive(2, finalDestinationEggFrames, true));
        assertEquals(actual, hives);

    }

}