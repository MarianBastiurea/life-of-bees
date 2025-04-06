package com.marianbastiurea.lifeofbees.action;

import com.marianbastiurea.lifeofbees.bees.Hive;
import com.marianbastiurea.lifeofbees.bees.Hives;
import com.marianbastiurea.lifeofbees.bees.HoneyFrame;
import com.marianbastiurea.lifeofbees.time.BeeTime;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class HarvestHoneyProducer implements ActionOfTheWeekProducer<List<Integer>> {

    @Override
    public Optional<List<Integer>> produce(Hives hives) {
        List<Integer> hiveIds = new ArrayList<>();
        BeeTime currentDate = hives.getCurrentDate();
        if (currentDate.timeToHarvestHive()) {
            for (Hive hive : hives.getHives()) {
                boolean hasHarvestableFrame = hive.getHoneyFrames().getHoneyFrame().stream()
                        .anyMatch(HoneyFrame::isHarvestable);

                if (hasHarvestableFrame) {
                    hiveIds.add(hive.getId());
                }
            }
        }

        return hiveIds.isEmpty() ? Optional.empty() : Optional.of(hiveIds);
    }
}


