package com.example.babyland;

public class Vaccination {
    String name, date, doctorName;
    Boolean vaccinated;


    public Vaccination(String name, String date, String doctorName, Boolean vaccinated) {
        this.name = name;
        this.date = date;
        this.doctorName = doctorName;
        this.vaccinated = vaccinated;
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

    public Boolean getVaccinated() {
        return vaccinated;
    }

    public void setVaccinated(Boolean vaccinated) {
        this.vaccinated = vaccinated;
    }
}
