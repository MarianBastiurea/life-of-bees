package com.marianbastiurea.lifeofbees.action;

import com.marianbastiurea.lifeofbees.time.BeeTime;

import java.util.Optional;

public class FeedBeesProducerAbstract extends BeeTimeProducerAbstract<Boolean> {
    @Override
    public Optional<Boolean> produce(BeeTime beeTime) {
        boolean canFeed = beeTime.canFeedBees();
        return canFeed ? Optional.of(true) : Optional.empty();
    }

}
