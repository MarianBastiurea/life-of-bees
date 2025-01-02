package com.marianbastiurea.lifeofbees.action;

import com.marianbastiurea.lifeofbees.game.LifeOfBees;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActionsOfTheWeek {
    private Map<ActionType, Object> actions;

    public ActionsOfTheWeek() {
        this.actions = new HashMap<>();
    }

    @Override
    public String toString() {
        return "ActionsOfTheWeek{" +
                "actions=" + actions +
                '}';
    }

    public void put(ActionType value, Object data) {
        actions.put(value,data);
    }

    public Map<ActionType, Object> getActions() {
        return actions;
    }

    public void executeActions(LifeOfBees lifeOfBees, Object data) {
        for (Map.Entry<ActionType, Object> action : actions.entrySet()) {
            ActionType actionType = action.getKey();
            Object actionData = action.getValue();
            actionType.getBiConsumer().accept(lifeOfBees, actionData);
        }
    }

    public void addAllActions(LifeOfBees lifeOfBees) {
        for (ActionType actionType : ActionType.values()) {
            Object data = actionType.getProducer().produce(lifeOfBees);
            if (data != null) {
                actions.put(actionType, data);
            }
        }
    }



}

