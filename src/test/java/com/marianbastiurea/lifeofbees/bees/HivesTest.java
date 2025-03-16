package com.marianbastiurea.lifeofbees.bees;

import com.marianbastiurea.lifeofbees.time.BeeTime;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


class HivesTest {

    @Test
    void testHibernate() {
        RandomParameters randomParameters = mock(RandomParameters.class);
        when(randomParameters.hiveIndexToRemove(2)).thenReturn(1);
        Hives hives = new Hives(
                new BeeTime(LocalDate.of(2024, 9, 30)),
                randomParameters,
                new Hive(3,
                        true,
                        new EggFrames(6, 1.0),
                        new HoneyFrames(6, 4),
                        new BeesBatches(200),
                        new ArrayList<>(List.of(new HoneyBatch(1, 10.0, HoneyType.ACACIA, false))),
                        new Queen(2)),
                new Hive(5));

        Integer removedHiveId = hives.hibernate();

        assertEquals(5, removedHiveId);
        assertEquals(new Hives(
                new BeeTime(LocalDate.of(2025, 3, 1)),
                randomParameters,
                new Hive(3,
                        false,
                        new EggFrames(5, 1.0),
                        new HoneyFrames(4, 4),
                        new BeesBatches(140),
                        new ArrayList<>(),
                        new Queen(3))), hives);
    }
}
