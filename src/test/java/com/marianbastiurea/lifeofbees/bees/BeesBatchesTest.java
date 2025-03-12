package com.marianbastiurea.lifeofbees.bees;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


class BeesBatchesTest {

    @Test
    void findKgOfHoneyIfNumberOfBeesIs5000AndProductivityIs1() {

        BeesBatches beesBatches = new BeesBatches(1000);
        double kgOfHoneyToAdd = beesBatches.makeHoney(1, 5000, 2);
        assertEquals(1.4, kgOfHoneyToAdd, 0.00001);
    }

    @Test
    void findKgOfHoneyIfNumberOfBeesIs40000AndProductivityIs08() {

        BeesBatches beesBatches = new BeesBatches(2000);
        double kgOfHoneyToAdd = beesBatches.makeHoney(0.7, 40000, 5);
        assertEquals(7, kgOfHoneyToAdd, 0.00001);
    }

    @Test
    void IfNumberOfFlightIs0NoHoneyHarvested() {
        BeesBatches beesBatches = new BeesBatches(1000);
        double kgOfHoneyToAdd = beesBatches.makeHoney(1, 2000, 0);
        assertEquals(0, kgOfHoneyToAdd, 0.0001);
    }
}
