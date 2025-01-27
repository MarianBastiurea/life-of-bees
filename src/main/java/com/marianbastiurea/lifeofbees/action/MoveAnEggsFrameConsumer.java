package com.marianbastiurea.lifeofbees.action;

import com.marianbastiurea.lifeofbees.bees.Hives;
import com.marianbastiurea.lifeofbees.game.LifeOfBees;


import java.util.List;


public class MoveAnEggsFrameConsumer implements ActionOfTheWeekConsumer<List<List<Integer>>> {

    @Override
    public void accept(LifeOfBees lifeOfBees , List<List<Integer>> hiveIdPair) {
        hives.moveAnEggsFrame(hiveIdPair);
    }
}