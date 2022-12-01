package com.example.babyland;

import java.util.ArrayList;

public class Baby {
    public String name, amka, parentOneAmka, parentTwoAmka, placeOfBirth, bloodType,  sex,dateOfBirth;
    public ArrayList<FamilyHistoryIllnesses> iln;


    public Baby() {
    }

    @Override
    public String toString() {
        return getName(); // You can add anything else like maybe getDrinkType()
    }

    public Baby(String name, String dateOfBirth, String amka, String placeOfBirth, String bloodType, String sex,
                String parentOneAmka, String parentTwoAmka, ArrayList<FamilyHistoryIllnesses> iln) {
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.amka = amka;
        this.placeOfBirth = placeOfBirth;
        this.bloodType = bloodType;
        this.sex = sex;
        this.parentOneAmka = parentOneAmka;
        this.parentTwoAmka = parentTwoAmka;
        this.iln = iln;
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
