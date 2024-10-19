package com.marianbastiurea.lifeofbees;

import java.util.List;

public class ActionOfTheWeek {
    private String actionOfTheWeekMessage;
    private String actionOfTheWeekMarker;
    private List<Integer> hiveIds;

    public ActionOfTheWeek(String actionOfTheWeekMessage, String actionOfTheWeekMarker, List<Integer> hiveIds) {
        this.actionOfTheWeekMessage = actionOfTheWeekMessage;
        this.actionOfTheWeekMarker = actionOfTheWeekMarker;
        this.hiveIds = hiveIds;
    }

    public String getActionOfTheWeekMessage() {
        return actionOfTheWeekMessage;
    }

    public void setActionOfTheWeekMessage(String actionOfTheWeekMessage) {
        this.actionOfTheWeekMessage = actionOfTheWeekMessage;
    }

    public String getActionOfTheWeekMarker() {
        return actionOfTheWeekMarker;
    }

    public void setActionOfTheWeekMarker(String actionOfTheWeekMarker) {
        this.actionOfTheWeekMarker = actionOfTheWeekMarker;
    }

    public List<Integer> getHiveIds() {
        return hiveIds;
    }

    public void setHiveIds(List<Integer> hiveIds) {
        this.hiveIds = hiveIds;
    }

    @Override
    public String toString() {
        return "ActionOfTheWeek{" +
                "actionOfTheWeekMessage='" + actionOfTheWeekMessage + '\'' +
                ", actionOfTheWeekMarker='" + actionOfTheWeekMarker + '\'' +
                ", hiveIds=" + hiveIds +
                '}';
    }
}