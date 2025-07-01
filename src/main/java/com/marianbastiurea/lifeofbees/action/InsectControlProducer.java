package com.marianbastiurea.lifeofbees.action;

import com.marianbastiurea.lifeofbees.time.BeeTime;

import java.util.Optional;


public class InsectControlProducer extends BeeTimeProducer<Boolean> {

    @Override
    public Optional<Boolean> produce(BeeTime beeTime) {
        boolean canInsectControl = beeTime.checkInsectControl();
        return canInsectControl ? Optional.of(true) : Optional.empty();
    }
}
