package com.marianbastiurea.lifeofbees.bees;

import com.marianbastiurea.lifeofbees.time.BeeTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class QueenTest {

    private RandomParameters randomParameters;
    private Queen initialQueen;

    @BeforeEach
    void setUp() {
        randomParameters = mock(RandomParameters.class);
        initialQueen = new Queen(3);
    }

    private void QueenChange(double chance, BeeTime beeTime, boolean shouldChange) {
        when(randomParameters.chanceToChangeQueen()).thenReturn(chance);
        Hive hive = new Hive(initialQueen);
        hive.maybeChangeQueen(beeTime, randomParameters);
        assertEquals(shouldChange ? 0 : initialQueen.getAgeOfQueen(),
                hive.getQueen().getAgeOfQueen(),
                shouldChange ? "New queen should have age 0" : "Queen should not be changed");
    }

    @Test
    void wrongTimeToChangeQueen() {
        QueenChange(0.2, new BeeTime(2023, 5, 2), false);
    }

    @Test
    void wrongAgeToChangeQueen() {
        QueenChange(0.5, new BeeTime(2023, 5, 1), false);
    }

    @Test
    void wrightTimeChangeQueen() {
        QueenChange(0.2, new BeeTime(2023, 5, 1), true);
    }


    @Test
    void whenQueensAgeIs5ThanChangeQueen() {
        when(randomParameters.chanceToChangeQueen()).thenReturn(0.9);
        Hive hive = new Hive(new Queen(5));
        hive.maybeChangeQueen(new BeeTime(2023, 7, 1), randomParameters);
        assertEquals(0, hive.getQueen().getAgeOfQueen(), "New queen should have age 0");
    }

    @Test
    void ifProductivityIs1AndWeatherIndexIs1QueenShouldLaid2000Eggs() {
        Queen queen = new Queen(1, 1);
        int numberOfEggs = queen.makeEggs(1, 1);
        assertEquals(numberOfEggs, 2000);
    }
}
