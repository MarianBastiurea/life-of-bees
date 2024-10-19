package com.marianbastiurea.lifeofbees;

import java.util.List;

public class HiveActionsRequest {
    private List<Integer> newEggsFrameHives;
    private List<Integer> splitEggsFrameHives;
    private List<Integer> newHoneyFrameHives;
    private List<Integer> splitHives;


    public List<Integer> getNewEggsFrameHives() {
        return newEggsFrameHives;
    }

    public void setNewEggsFrameHives(List<Integer> newEggsFrameHives) {
        this.newEggsFrameHives = newEggsFrameHives;
    }

    public List<Integer> getSplitEggsFrameHives() {
        return splitEggsFrameHives;
    }

    public void setSplitEggsFrameHives(List<Integer> splitEggsFrameHives) {
        this.splitEggsFrameHives = splitEggsFrameHives;
    }

    public List<Integer> getNewHoneyFrameHives() {
        return newHoneyFrameHives;
    }

    public void setNewHoneyFrameHives(List<Integer> newHoneyFrameHives) {
        this.newHoneyFrameHives = newHoneyFrameHives;
    }

    public List<Integer> getSplitHives() {
        return splitHives;
    }

    public void setSplitHives(List<Integer> splitHives) {
        this.splitHives = splitHives;
    }
}
