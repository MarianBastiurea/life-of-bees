package com.marianbastiurea.lifeofbees.action;

import com.marianbastiurea.lifeofbees.bees.Hives;
import com.marianbastiurea.lifeofbees.time.BeeTime;

import java.util.Optional;

public abstract class BeeTimeProducerAbstract<T> implements ActionOfTheWeekProducer<T> {

    @Override
    public Optional<T> produce(Hives hives) {
        return produce(hives.getCurrentDate());
    }

    public abstract Optional<T> produce(BeeTime beetime);

}
