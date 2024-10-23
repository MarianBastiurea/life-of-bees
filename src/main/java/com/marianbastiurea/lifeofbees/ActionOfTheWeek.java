package com.marianbastiurea.lifeofbees;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ActionOfTheWeek {
    private String actionOfTheWeekMessage;
    private String actionOfTheWeekMarker;
    private List<Integer> hiveIds;
    private List<List<Integer>> hiveIdPair;

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

    public List<List<Integer>> getHiveIdPair() {
        return hiveIdPair;
    }

    public void setHiveIdPair(List<List<Integer>> hiveIdPair) {
        this.hiveIdPair = hiveIdPair;
    }

    @Override
    public String toString() {
        return "ActionOfTheWeek{" +
                "actionOfTheWeekMessage='" + actionOfTheWeekMessage + '\'' +
                ", actionOfTheWeekMarker='" + actionOfTheWeekMarker + '\'' +
                ", hiveIds=" + hiveIds +
                '}';
    }

    public static void addOrUpdateAction(List<ActionOfTheWeek> actionsOfTheWeek, String newAction, String actionMarker, int hiveId) {

        Optional<ActionOfTheWeek> existingAction = actionsOfTheWeek.stream()
                .filter(action -> action.getActionOfTheWeekMarker().equals(actionMarker))
                .findFirst();


        if (existingAction.isPresent()) {
            if (!existingAction.get().getHiveIds().contains(hiveId)) {
                existingAction.get().getHiveIds().add(hiveId);
            }
        } else {

            List<Integer> newHiveIds = new ArrayList<>();
            newHiveIds.add(hiveId);
            ActionOfTheWeek actionOfTheWeek = new ActionOfTheWeek(newAction, actionMarker, newHiveIds);
            actionsOfTheWeek.add(actionOfTheWeek);
        }
    }

    public static void addOrUpdateActionForEggsFrameMove(List<ActionOfTheWeek> actionsOfTheWeek, String newAction, String actionMarker, List<List<Integer>> hiveIdPair) {

        Optional<ActionOfTheWeek> existingAction = actionsOfTheWeek.stream()
                .filter(action -> action.getActionOfTheWeekMarker().equals(actionMarker))
                .findFirst();

        if (existingAction.isPresent()) {
            for (List<Integer> newPair : hiveIdPair) {

                boolean pairExists = existingAction.get().getHiveIds().containsAll(newPair);
                if (!pairExists) {
                    existingAction.get().getHiveIds().addAll(newPair);  // Adaugăm întreaga pereche
                }
            }

        } else {
            List<Integer> newHiveIds = new ArrayList<>();
            for (List<Integer> pair : hiveIdPair) {
                newHiveIds.addAll(pair);  // Adăugăm toate perechile în lista nouă
            }
            ActionOfTheWeek actionOfTheWeek = new ActionOfTheWeek(newAction, actionMarker, newHiveIds);
            actionsOfTheWeek.add(actionOfTheWeek);
        }
    }




}
