package com.example.babyland;

public class SustenanceItems {
    String name, ageGap;
    Boolean checked;

    public SustenanceItems(String name, String ageGap, Boolean checked) {
        this.name = name;
        this.ageGap = ageGap;
        this.checked = checked;
    }

    public SustenanceItems(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAgeGap() {
        return ageGap;
    }

    public void setAgeGap(String ageGap) {
        this.ageGap = ageGap;
    }

    public Boolean getChecked() {
        return checked;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }
}
