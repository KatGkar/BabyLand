package com.example.babyland;

public class DevelopmentalItems {
    private String name, ageGap, details;

    public DevelopmentalItems(String name, String ageGap, String details) {
        this.name = name;
        this.ageGap = ageGap;
        this.details = details;
    }

    public DevelopmentalItems(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAgeGap() {return ageGap;}

    public void setAgeGap(String ageGap) {
        this.ageGap = ageGap;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
