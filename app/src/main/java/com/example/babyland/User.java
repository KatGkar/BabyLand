package com.example.babyland;

import java.util.ArrayList;

public class User {
    public String name, surname, amka, phoneNumber, email, dateOfBirth, bloodType, partenersAmka, UID;
    public Boolean partener;
    public ArrayList<Baby> kids;

    public User(String name, String surname, String amka, String phoneNumber, String email, String dateOfBirth, String bloodType, String partenersAmka, Boolean partener, ArrayList<Baby> kids, String UID) {
        this.name = name;
        this.surname = surname;
        this.amka = amka;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.dateOfBirth = dateOfBirth;
        this.bloodType = bloodType;
        this.partenersAmka = partenersAmka;
        this.partener = partener;
        this.kids = kids;
        this.UID = UID;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public ArrayList<Baby> getKids() {
        return kids;
    }

    public void setKids(ArrayList<Baby> kids) {
        this.kids = kids;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getAmka() {
        return amka;
    }

    public void setAmka(String amka) {
        this.amka = amka;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getBloodType() {
        return bloodType;
    }

    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    }

    public String getPartenersAmka() {
        return partenersAmka;
    }

    public void setPartenersAmka(String partenersAmka) {
        this.partenersAmka = partenersAmka;
    }

    public Boolean getPartener() {
        return partener;
    }

    public void setPartener(Boolean partener) {
        this.partener = partener;
    }
}
