package com.example.babyland;

import java.util.ArrayList;

public class Baby {
    public String name, amka, parentOneName, parentTwoName, placeOfBirth, phoneNumber,
            bloodType,  sex,dateOfBirth;
    public ArrayList<FamilyHistoryIllnesses> iln;

    public Baby(String name, String dateOfBirth, String amka,String placeOfBirth,String bloodType, String sex,
                String parentOneName, String parentTwoName,  String phoneNumber, ArrayList<FamilyHistoryIllnesses> iln) {
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.amka = amka;
        this.placeOfBirth = placeOfBirth;
        this.bloodType = bloodType;
        this.sex = sex;
        this.parentOneName = parentOneName;
        this.parentTwoName = parentTwoName;
        this.phoneNumber = phoneNumber;
        this.iln = iln;
    }

    public String getParentOneName() {
        return parentOneName;
    }

    public void setParentOneName(String parentOneName) {
        this.parentOneName = parentOneName;
    }

    public String getParentTwoName() {
        return parentTwoName;
    }

    public void setParentTwoName(String parentTwoName) {
        this.parentTwoName = parentTwoName;
    }

    public String getPlaceOfBirth() {
        return placeOfBirth;
    }

    public void setPlaceOfBirth(String placeOfBirth) {
        this.placeOfBirth = placeOfBirth;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
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
