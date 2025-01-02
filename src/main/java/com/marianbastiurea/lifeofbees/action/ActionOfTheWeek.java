package com.marianbastiurea.lifeofbees.action;

import java.util.Map;

public class ActionOfTheWeek {
    private String actionType;
    private Map<String, Object> data;

    public ActionOfTheWeek(String actionType, Map<String, Object> data) {
        this.actionType = actionType;
        this.data = data;
    }

    public ActionOfTheWeek() {
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    @Override
    public String toString() {
        return "ActionOfTheWeek{" +
                "actionType='" + actionType + '\'' +
                ", data=" + data +
                '}';
    }
}
