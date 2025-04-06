package com.marianbastiurea.lifeofbees.bees;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.marianbastiurea.lifeofbees.bees.ApiaryParameters.MAX_NUMBER_OF_HONEY_FRAMES;

public class HoneyFrames {

    private static RandomParameters randomParameters = new RandomParameters();
    private List<HoneyFrame> honeyFrame;

    public HoneyFrames() {
    }

    public HoneyFrames(List<HoneyFrame> honeyFrame) {
        this.honeyFrame = new ArrayList<>(honeyFrame);
    }


    public HoneyFrames(int numberOfHoneyFrames, int honeyPerFrame) {
        this.honeyFrame = new ArrayList<>();
        for (int i = 0; i < numberOfHoneyFrames; i++) {
            this.honeyFrame.add(new HoneyFrame(honeyPerFrame));
        }
    }

    public static HoneyFrames getRandomHoneyFrames() {
        HoneyFrames honeyFrames = new HoneyFrames(new ArrayList<>());
        for (int k = 0; k < randomParameters.numberOfHoneyFrames(); k++) {
            honeyFrames.getHoneyFrame().add(new HoneyFrame(randomParameters.kgOfHoney()));
        }
        return honeyFrames;
    }

    public List<HoneyFrame> getHoneyFrame() {
        return honeyFrame;
    }

    public boolean canAddANewHoneyFrameInHive() {
        long honeyFrameFull = getHoneyFrame().stream()
                .filter(HoneyFrame::isHarvestable)
                .count();
        return getHoneyFrame().size() < MAX_NUMBER_OF_HONEY_FRAMES && honeyFrameFull == getHoneyFrame().size();
    }

    public void addNewHoneyFrameInHive() {
        if (honeyFrame.size() < 6) {
            honeyFrame.add(new HoneyFrame(0));
        }
    }

    public double harvestHoneyFromHoneyFrames() {
        return this.getHoneyFrame().stream()
                .filter(HoneyFrame::isHarvestable)
                .mapToDouble(HoneyFrame::harvest)
                .sum();
    }


    public int getNumberOfFullHoneyFrame() {
        return (int) this.getHoneyFrame().stream()
                .filter(HoneyFrame::isFull)
                .count();
    }


    public HoneyFrames splitHoneyFrames() {
        HoneyFrames newHiveHoneyFrames = new HoneyFrames(new ArrayList<>());
        for (int i = 0; i < 2; i++) {
            HoneyFrame frameToMove = getHoneyFrame().removeLast();
            newHiveHoneyFrames.getHoneyFrame().add(frameToMove);
        }
        return newHiveHoneyFrames;
    }

    public void hibernateHoneyFrames() {
        if (honeyFrame.size() > 1) {
            honeyFrame.removeLast();
            honeyFrame.removeLast();
        }
    }

    public void fillUpAHoneyFrame(double kgOfHoneyToAdd) {
        int numberOfHoneyFrameNotFull = getHoneyFrame().size() - getNumberOfFullHoneyFrame();
        for (HoneyFrame honeyFrame : getHoneyFrame()) {
            honeyFrame.fill(kgOfHoneyToAdd / numberOfHoneyFrameNotFull);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HoneyFrames that = (HoneyFrames) o;
        return Objects.equals(honeyFrame, that.honeyFrame);
    }

    @Override
    public int hashCode() {
        return Objects.hash(honeyFrame);
    }

    @Override
    public String toString() {
        return "HoneyFrames{" +
                "honeyFrame=" + honeyFrame +
                '}';
    }
}


