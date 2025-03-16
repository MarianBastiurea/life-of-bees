package com.marianbastiurea.lifeofbees.action;

import com.marianbastiurea.lifeofbees.bees.Hive;
import com.marianbastiurea.lifeofbees.bees.Hives;
import com.marianbastiurea.lifeofbees.bees.HoneyFrame;
import com.marianbastiurea.lifeofbees.bees.HoneyFrames;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static com.marianbastiurea.lifeofbees.bees.ApiaryParameters.MAX_KG_OF_HONEY_PER_FRAME;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AddHoneyFramesProducerTest {

    @Test
    void cantAddHoneyFrameToHiveWithMaxNumberOfHoneyFrames() {
        Optional<List<Integer>> result = new AddHoneyFramesProducer().produce(
                new Hives(new Hive(1, new HoneyFrames(List.of(
                        new HoneyFrame(1.0), new HoneyFrame(2.0), new HoneyFrame(3.5),
                        new HoneyFrame(4.0), new HoneyFrame(2.5), new HoneyFrame(2.5)
                ))))
        );

        assertTrue(result.isEmpty(), "Should not add honey frame when at max capacity.");
    }

    @Test
    void cantAddHoneyFrameToHiveWhenHoneyFramesAreNotFull() {
        Optional<List<Integer>> result = new AddHoneyFramesProducer().produce(
                new Hives(new Hive(1, new HoneyFrames(List.of(
                        new HoneyFrame(2.0), new HoneyFrame(3.5),
                        new HoneyFrame(2.0), new HoneyFrame(2.5), new HoneyFrame(2.5)
                ))))
        );
        assertTrue(result.isEmpty(), "Should not add honey frame when honey frames are not full.");
    }

    @Test
    void canAddHoneyFrameToHiveWhenHoneyFramesAreFull() {
        Optional<List<Integer>> result = new AddHoneyFramesProducer().produce(
                new Hives(new Hive(1, new HoneyFrames(List.of(
                        new HoneyFrame(MAX_KG_OF_HONEY_PER_FRAME), new HoneyFrame(4.1),
                        new HoneyFrame(4.1), new HoneyFrame(4.5), new HoneyFrame(4.5)
                ))))
        );
        assertEquals(Optional.of(List.of(1)), result);
    }


}