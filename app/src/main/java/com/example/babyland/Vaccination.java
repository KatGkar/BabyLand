package com.example.babyland;

public class Vaccination {
    private String name, date, doctorName;
    private int uniqueID;

    public Vaccination() {}

    public Vaccination(String name, String date, String doctorName, int uniqueID) {
        this.name = name;
        this.date = date;
        this.doctorName = doctorName;
        this.uniqueID = uniqueID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public int getUniqueID() {
        return uniqueID;
    }

    public void setUniqueID(int uniqueID) {
        this.uniqueID = uniqueID;
    }
}
