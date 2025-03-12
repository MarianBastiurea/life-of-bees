package com.marianbastiurea.lifeofbees.bees;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HoneyFramesTest {


    @Test
    void ifKgOfHoneyToAddIs5AndNumberOfHoneyFramesIs5EachHoneyFrameHave1KgMore() {
        HoneyFrames initialHoneyFrames = new HoneyFrames(5, 3);
        initialHoneyFrames.fillUpAHoneyFrame(5);
        HoneyFrames actualHoneyFrames = new HoneyFrames(5, 4);
        assertEquals(initialHoneyFrames, actualHoneyFrames);

    }

    @Test
    void ifKgOfHoneyToAddIs0AndNumberOfHoneyFramesIs5EachHoneyFrameHaveSameQuantity() {
        HoneyFrames initialHoneyFrames = new HoneyFrames(5, 3);
        initialHoneyFrames.fillUpAHoneyFrame(0);
        HoneyFrames actualHoneyFrames = new HoneyFrames(5, 3);
        assertEquals(initialHoneyFrames, actualHoneyFrames);

    }
}
