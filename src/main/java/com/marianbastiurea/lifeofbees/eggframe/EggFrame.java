package com.marianbastiurea.lifeofbees.eggframe;


import com.marianbastiurea.lifeofbees.BeesBatch;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.*;

public class EggFrame {

    private final int maxEggPerFrame = 6400;

    private List<EggBatch> eggBatches;

    public EggFrame() {
        this.eggBatches = new ArrayList<>();
    }

    public int getNumberOfEggs() {
        return eggBatches.stream().mapToInt(EggBatch::getNumberOfEggs).sum();
    }

    public int getMaxEggPerFrame() {
        return maxEggPerFrame;
    }

    @Override
    public String toString() {
        return "EggsFrame{" +
                "numberOfEggs=" + getNumberOfEggs() +
                " , eggsBatches=" + eggBatches +
                '}' + "\n";
    }

    public void addEggs(int numberOfEggs, LocalDate date) {
        //  maxEggPerFrame is 6400. A frame have around 8500 cells. 75% more or less are used by the queen to lay eggs.
        // Remaining cells are fill up with honey or are damaged


        if (getNumberOfEggs() + numberOfEggs <= maxEggPerFrame) {
            eggBatches.add(new EggBatch(numberOfEggs, date));
        }
    }

    public List<BeesBatch> checkAndHatchEggs(LocalDate currentDate) {
        List<BeesBatch> hatchedBatches = new ArrayList<>();
        Iterator<EggBatch> iterator = eggBatches.iterator();

        while (iterator.hasNext()) {
            EggBatch eggBatch = iterator.next();
            LocalDate creationDate = eggBatch.getCreationDate();
            long differenceInDays = ChronoUnit.DAYS.between(creationDate, currentDate);

            if (differenceInDays > 21) {
                hatchedBatches.add(new BeesBatch(eggBatch.getNumberOfEggs(), currentDate));
                iterator.remove();
            }
        }
        return hatchedBatches;
    }
    public boolean isEggFrameFull(){
        return getNumberOfEggs() == maxEggPerFrame;
    }
    public boolean is80PercentFull(){
        return  getNumberOfEggs() <= 0.8 * maxEggPerFrame;
    }
}



