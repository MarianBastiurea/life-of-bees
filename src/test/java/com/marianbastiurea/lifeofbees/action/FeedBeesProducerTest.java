package com.marianbastiurea.lifeofbees.action;

import com.marianbastiurea.lifeofbees.time.BeeTime;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FeedBeesProducerTest {

    @Test
    void cantFeedBeesIfMonthIsNotSeptember() {
        Optional<Boolean> result = new FeedBeesProducerAbstract().produce(
                new BeeTime(2023, 8, 22)
        );
        assertTrue(result.isEmpty(), "Month is not September");
    }


    @Test
    void canFeedBeesIfMonthSeptember() {
        Optional<Boolean> result = new FeedBeesProducerAbstract().produce(
                new BeeTime(2023, 9, 22) // September
        );
        assertEquals(Optional.of(true), result, "Month is September");
    }
}