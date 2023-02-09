package com.example.babyland;

import java.util.ArrayList;

public class Development {
    private String weight , length, headCircumference, age, ageType, observations, doctor, amka, measurementDate;
    private ArrayList<SustenanceItems> sustenance;
    private ArrayList<ExaminationItems> examination;
    private ArrayList<DevelopmentalItems> developmentalMonitoring;
    private Boolean hearing;

    public Development(){}

    public Development(String amka, String weight, String length, String headCircumference, String measurementDate,
                       String age, String ageType, ArrayList<SustenanceItems> sustenance,
                       ArrayList<ExaminationItems> examination, ArrayList<DevelopmentalItems> developmentalMonitoring,
                       Boolean hearing, String observations, String doctor) {
        this.amka = amka;
        this.weight = weight;
        this.length = length;
        this.headCircumference = headCircumference;
        this.measurementDate = measurementDate;
        this.age = age;
        this.ageType = ageType;
        this.sustenance = sustenance;
        this.examination = examination;
        this.developmentalMonitoring = developmentalMonitoring;
        this.hearing = hearing;
        this.observations = observations;
        this.doctor = doctor;
    }

    public String getAmka() {
        return amka;
    }

    public void setAmka(String amka) {
        this.amka = amka;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getHeadCircumference() {
        return headCircumference;
    }

    public void setHeadCircumference(String headCircumference) {this.headCircumference = headCircumference;}

    public String getMeasurementDate() {
        return measurementDate;
    }

    public void setMeasurementDate(String measurementDate) {this.measurementDate = measurementDate;}

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getAgeType() {
        return ageType;
    }

    public void setAgeType(String ageType) {
        this.ageType = ageType;
    }

    public ArrayList<SustenanceItems> getSustenance() {
        return sustenance;
    }

    public void setSustenance(ArrayList<SustenanceItems> sustenance) {this.sustenance = sustenance;}

    public ArrayList<ExaminationItems> getExamination() {
        return examination;
    }

    public void setExamination(ArrayList<ExaminationItems> examination) {this.examination = examination;}

    public ArrayList<DevelopmentalItems> getDevelopmentalMonitoring() {return developmentalMonitoring;}

    public void setDevelopmentalMonitoring(ArrayList<DevelopmentalItems> developmentalMonitoring) {
        this.developmentalMonitoring = developmentalMonitoring;
    }

    public Boolean getHearing() {
        return hearing;
    }

    public void setHearing(Boolean hearing) {
        this.hearing = hearing;
    }

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    public String getDoctor() {
        return doctor;
    }

    public void setDoctor(String doctor) {
        this.doctor = doctor;
    }
}
