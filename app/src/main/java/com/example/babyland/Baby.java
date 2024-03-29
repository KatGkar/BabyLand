package com.example.babyland;

import androidx.annotation.NonNull;

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

public class Baby {
    private String name, amka, parentOneAmka, parentTwoAmka, placeOfBirth, bloodType,  sex;
    private String dateOfBirth;
    private ArrayList<FamilyHistoryIllnesses> iln;
    private ArrayList<Vaccination> vaccinations;

    @NonNull
    @Override
    public String toString() {
        return getAmka();
    }

    public Baby() {
    }

    public Baby(String name, String dateOfBirth, String amka, String placeOfBirth, String bloodType, String sex,
                String parentOneAmka, String parentTwoAmka, ArrayList<FamilyHistoryIllnesses> iln,
                ArrayList<Vaccination> vaccinations) {
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.amka = amka;
        this.placeOfBirth = placeOfBirth;
        this.bloodType = bloodType;
        this.sex = sex;
        this.parentOneAmka = parentOneAmka;
        this.parentTwoAmka = parentTwoAmka;
        this.iln = iln;
        this.vaccinations = vaccinations;
    }


    public Baby(Baby baby) {
        this.name = baby.getName();
        this.dateOfBirth = baby.getDateOfBirth();
        this.amka = baby.getAmka();
        this.placeOfBirth = baby.getPlaceOfBirth();
        this.sex = baby.getSex();
        this.parentOneAmka = baby.getParentOneAmka();
        this.parentTwoAmka = baby.getParentTwoAmka();
        this.iln = baby.getIln();
        this.vaccinations = baby.getVaccinations();
    }

    public ArrayList<Vaccination> getVaccinations() {
        return vaccinations;
    }

    public void setVaccinations(ArrayList<Vaccination> vaccinations) {
        this.vaccinations = vaccinations;
    }

    public String getParentOneAmka() {
        return parentOneAmka;
    }

    public void setParentOneAmka(String parentOneAmka) {
        this.parentOneAmka = parentOneAmka;
    }

    public String getParentTwoAmka() {
        return parentTwoAmka;
    }

    public void setParentTwoAmka(String parentTwoAmka) {
        this.parentTwoAmka = parentTwoAmka;
    }

    public String getPlaceOfBirth() {
        return placeOfBirth;
    }

    public void setPlaceOfBirth(String placeOfBirth) {
        this.placeOfBirth = placeOfBirth;
    }

    public String getBloodType() {
        return bloodType;
    }

    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAmka() {
        return amka;
    }

    public void setAmka(String amka) {
        this.amka = amka;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<FamilyHistoryIllnesses> getIln() {
        return iln;
    }

    public void setIln(ArrayList<FamilyHistoryIllnesses> iln) {
        this.iln = iln;
    }
}
