package com.marianbastiurea.lifeofbees.action;

import com.marianbastiurea.lifeofbees.time.BeeTime;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

class InsectControlProducerTest {
    @Test
    void cantDoInsectControlIfMonthIsMarch() {
        Optional<Boolean> result = new InsectControlProducerAbstract().produce(
                new BeeTime(2023, 3, 22)
        );
        assertTrue(result.isEmpty(), "Insect control should not be allowed in March.");
    }

    @Test
    void cantDoInsectControlIfMonthIsAprilAndDayIs1() {
        Optional<Boolean> result = new InsectControlProducerAbstract().produce(
                new BeeTime(2023, 4, 1)
        );
        assertTrue(result.isEmpty(), "Insect control should not be allowed in 1st April");
    }

    @Test
    void canDoInsectControlIfMonthIsAprilAndDayIs10() {
        Optional<Boolean> result = new InsectControlProducerAbstract().produce(
                new BeeTime(2023, 4, 10)
        );
        assertTrue(result.isPresent(), "Insect control should be allowed on the last valid day.");
        assertTrue(result.get(), "The result should contain true for the last valid day.");

    }

}