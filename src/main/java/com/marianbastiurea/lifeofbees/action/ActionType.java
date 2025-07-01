package com.marianbastiurea.lifeofbees.action;

public enum ActionType {
    ADD_EGGS_FRAME(new AddEggsFramesProducer(), new AddEggsFramesConsumerAbstract()),
    ADD_HONEY_FRAME(new AddHoneyFramesProducer(), new AddHoneyFramesConsumerAbstract()),
    MOVE_EGGS_FRAME(new MoveAnEggsFrameProducer(), new MoveAnEggsFrameConsumerAbstract()),
    FEED_BEES(new FeedBeesProducerAbstract(), new FeedBeesConsumer()),
    SPLIT_HIVE(new SplitHiveProducer(), new SplitHiveConsumerAbstract()),
    INSECT_CONTROL(new InsectControlProducerAbstract(), new InsectControlConsumer()),
    HARVEST_HONEY(new HarvestHoneyProducer(), new HarvestHoneyConsumerAbstract());

    private final ActionOfTheWeekProducer producer;
    private final ActionOfTheWeekConsumer biConsumer;

    ActionType(ActionOfTheWeekProducer producer, ActionOfTheWeekConsumer biConsumer) {
        this.producer = producer;
        this.biConsumer = biConsumer;
    }

    public ActionOfTheWeekProducer getProducer() {
        return producer;
    }

    public ActionOfTheWeekConsumer getBiConsumer() {
        return biConsumer;
    }
}
