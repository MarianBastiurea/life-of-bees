package com.marianbastiurea.lifeofbees.action;

import com.marianbastiurea.lifeofbees.bees.Apiary;
import com.marianbastiurea.lifeofbees.bees.Hive;
import com.marianbastiurea.lifeofbees.game.LifeOfBees;

import java.util.ArrayList;
import java.util.List;

public class MoveAnEggsFrameProducer implements ActionOfTheWeekProducer {
    @Override
    public Object produce(LifeOfBees lifeOfBees) {
        Apiary apiary=lifeOfBees.getApiary();
        return  apiary.checkIfCanMoveAnEggsFrame();
    }
}
