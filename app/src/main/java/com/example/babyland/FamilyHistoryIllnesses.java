package com.example.babyland;

public class FamilyHistoryIllnesses {
    String illness;
    Boolean switches;
    String details;

    public FamilyHistoryIllnesses(String illness, Boolean switches, String details) {
        this.illness = illness;
        this.switches = switches;
        this.details = details;
    }

    public FamilyHistoryIllnesses() {
    }

    public String getIllness() {
        return illness;
    }

    public void setIllness(String illness) {
        this.illness = illness;
    }

    public Boolean getSwitches() {
        return switches;
    }

    public void setSwitches(Boolean switches) {
        this.switches = switches;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
