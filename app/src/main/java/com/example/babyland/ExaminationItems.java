package com.example.babyland;

public class ExaminationItems {
    String name, ageGap;
    int details;

    public ExaminationItems() {
    }

    public ExaminationItems(String name, String ageGap, Integer details) {
        this.name = name;
        this.ageGap = ageGap;
        this.details = details;
    }

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

    public int getDetails() {
        return details;
    }

    public void setDetails(int details) {
        this.details = details;
    }
}
