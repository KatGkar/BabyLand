package com.example.babyland;

public class FamilyHistoryIlnesses {
    String ilness;
    Boolean switches;
    String details;

    public FamilyHistoryIlnesses(String ilness, Boolean switches, String details) {
        this.ilness = ilness;
        this.switches = switches;
        this.details = details;
    }

    public String getIlness() {
        return ilness;
    }

    public void setIlness(String ilness) {
        this.ilness = ilness;
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
