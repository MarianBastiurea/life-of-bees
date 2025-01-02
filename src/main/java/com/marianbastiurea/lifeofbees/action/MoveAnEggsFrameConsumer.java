package com.marianbastiurea.lifeofbees.action;

import com.marianbastiurea.lifeofbees.bees.Apiary;
import com.marianbastiurea.lifeofbees.game.LifeOfBees;

import java.util.List;

public class MoveAnEggsFrameConsumer implements ActionOfTheWeekConsumer {

    @Override
    public void accept(LifeOfBees lifeOfBees, Object data) {
        List<List<Integer>> hiveIdPair = (List<List<Integer>>) data;
        Apiary apiary = lifeOfBees.getApiary();
        apiary.moveAnEggsFrame(hiveIdPair);
    }
}
