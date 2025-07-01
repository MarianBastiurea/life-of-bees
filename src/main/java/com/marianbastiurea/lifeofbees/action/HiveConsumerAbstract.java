package com.marianbastiurea.lifeofbees.action;

import com.marianbastiurea.lifeofbees.bees.Hives;
import com.marianbastiurea.lifeofbees.game.LifeOfBees;

public abstract class HiveConsumerAbstract<T> implements ActionOfTheWeekConsumer<T> {

    @Override
    public void accept(LifeOfBees lifeOfBees, T data) {
        accept(lifeOfBees.getApiary().getHives(), data);
    }

    public abstract void accept(Hives hives, T data);

}
