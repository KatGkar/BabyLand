package com.example.babyland;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Development {
    float weight , length, headCircumference;
    Date measurementDate;
    int age;
    String ageType;
    ArrayList<sustenanceItems> sustenance;
    ArrayList<examinationItems> examination;
    ArrayList<developmentalItems> developmentalMonitoring;
    Boolean hearing;
    String observations;
    String doctor;

    public Development(float weight, float length, float headCircumference, Date measurementDate, int age, String ageType, ArrayList<sustenanceItems> sustenance, ArrayList<examinationItems> examination, ArrayList<developmentalItems> developmentalMonitoring, Boolean hearing, String observations, String doctor) {
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

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public float getLength() {
        return length;
    }

    public void setLength(float length) {
        this.length = length;
    }

    public float getHeadCircumference() {
        return headCircumference;
    }

    public void setHeadCircumference(float headCircumference) {
        this.headCircumference = headCircumference;
    }

    public Date getMeasurementDate() {
        return measurementDate;
    }

    public void setMeasurementDate(Date measurementDate) {
        this.measurementDate = measurementDate;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
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
