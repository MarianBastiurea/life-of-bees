package com.marianbastiurea.lifeofbees.action;

import com.marianbastiurea.lifeofbees.bees.Hives;

import java.util.List;


public class MoveAnEggsFrameConsumerAbstract extends HiveConsumerAbstract<List<List<Integer>>> {

    @Override
    public void accept(Hives hives, List<List<Integer>> hiveIdPair) {
        hives.moveAnEggsFrame(hiveIdPair);
    }
}