package com.example.babyland;

public class Vaccination {
    String name, date, doctorName;
    int timesVaccinated;


    public Vaccination(String name, String date, String doctorName, int timesVaccinated) {
        this.name = name;
        this.date = date;
        this.doctorName = doctorName;
        this.timesVaccinated = timesVaccinated;
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

    public int getTimesVaccinated() {
        return timesVaccinated;
    }

    public void setTimesVaccinated(int timesVaccinated) {
        this.timesVaccinated = timesVaccinated;
    }
}
