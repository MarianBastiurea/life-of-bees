package com.marianbastiurea.lifeofbees.action;

import com.marianbastiurea.lifeofbees.bees.EggFrames;
import com.marianbastiurea.lifeofbees.bees.Hive;
import com.marianbastiurea.lifeofbees.bees.Hives;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static com.marianbastiurea.lifeofbees.bees.ApiaryParameters.FULLNESS_FACTOR;
import static com.marianbastiurea.lifeofbees.bees.ApiaryParameters.MAX_NUMBER_OF_EGG_FRAMES;
import static org.junit.jupiter.api.Assertions.assertEquals;

class AddEggsFramesProducerTest {

    private AddEggsFramesProducer addEggsFramesProducer;

    @BeforeEach
    void setUp() {
        addEggsFramesProducer = new AddEggsFramesProducer();
    }

    @Test
    void cantAddEggFrameToHiveWithMaxNumberOfEggFramesAndFullnessOverFullnessFactor() {
        Hives hives = new Hives(new Hive(1, new EggFrames(MAX_NUMBER_OF_EGG_FRAMES, FULLNESS_FACTOR + 0.1)));

        Optional<List<Integer>> result = addEggsFramesProducer.produce(hives);

        assertEquals(Optional.empty(), result);
    }

    @Test
    void addEggsFramesProducer_mixed_cases() {
        Hives hives = new Hives(
                new Hive(1, new EggFrames(MAX_NUMBER_OF_EGG_FRAMES, FULLNESS_FACTOR + 0.1)),
                new Hive(2, new EggFrames(MAX_NUMBER_OF_EGG_FRAMES, FULLNESS_FACTOR - 0.1)),
                new Hive(3, new EggFrames(MAX_NUMBER_OF_EGG_FRAMES - 1, FULLNESS_FACTOR)),
                new Hive(4, new EggFrames(MAX_NUMBER_OF_EGG_FRAMES - 2, FULLNESS_FACTOR + 0.1)),
                new Hive(5, new EggFrames(MAX_NUMBER_OF_EGG_FRAMES - 1, FULLNESS_FACTOR - 0.1)
                ));

        Optional<List<Integer>> result = addEggsFramesProducer.produce(hives);

        assertEquals(Optional.of(List.of(3, 4)), result);
    }
}