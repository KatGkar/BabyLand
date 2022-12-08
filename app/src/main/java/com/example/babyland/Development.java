package com.example.babyland;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Development {
    String weight , length, headCircumference, age;
    String ageType, observations, doctor, amka;
    String measurementDate;
    ArrayList<sustenanceItems> sustenance;
    ArrayList<examinationItems> examination;
    ArrayList<developmentalItems> developmentalMonitoring;
    Boolean hearing;

    public Development(){}

    public Development(String amka, String weight, String length, String headCircumference, String measurementDate, String age, String ageType, ArrayList<sustenanceItems> sustenance, ArrayList<examinationItems> examination, ArrayList<developmentalItems> developmentalMonitoring, Boolean hearing, String observations, String doctor) {
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

    public void setHeadCircumference(String headCircumference) {
        this.headCircumference = headCircumference;
    }

    public String getMeasurementDate() {
        return measurementDate;
    }

    public void setMeasurementDate(String measurementDate) {
        this.measurementDate = measurementDate;
    }

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

    public ArrayList<sustenanceItems> getSustenance() {
        return sustenance;
    }

    public void setSustenance(ArrayList<sustenanceItems> sustenance) {
        this.sustenance = sustenance;
    }

    public ArrayList<examinationItems> getExamination() {
        return examination;
    }

    public void setExamination(ArrayList<examinationItems> examination) {
        this.examination = examination;
    }

    public ArrayList<developmentalItems> getDevelopmentalMonitoring() {
        return developmentalMonitoring;
    }

    public void setDevelopmentalMonitoring(ArrayList<developmentalItems> developmentalMonitoring) {
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
