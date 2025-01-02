package com.marianbastiurea.lifeofbees.action;

import com.marianbastiurea.lifeofbees.bees.Apiary;
import com.marianbastiurea.lifeofbees.game.LifeOfBees;

public interface ActionOfTheWeekConsumer {
    void accept(LifeOfBees lifeOfBees, Object data);
}
