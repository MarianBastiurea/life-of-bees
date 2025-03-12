package com.marianbastiurea.lifeofbees.bees;

import com.marianbastiurea.lifeofbees.time.BeeTime;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BeeTimeTest {

    @Test
    void returnProductivity1ForHoneyTypeAcacia() {
        BeeTime currentDate = new BeeTime(2023, 6, 1);
        double productivity = currentDate.honeyType().getProductivity();
        assertEquals(1, productivity);
    }
}
