package com.example.babyland;

import java.util.ArrayList;


public class Doctor {
    String name, medicalID, phoneNumber, email, surname;
    ArrayList<Baby> kids;

    public Doctor(String name, String medicalID, String phoneNumber, String email, ArrayList<Baby> kids, String surname) {
        this.name = name;
        this.medicalID = medicalID;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.kids = kids;
        this.surname = surname;
    }

    public Doctor() {
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMedicalID() {
        return medicalID;
    }

    public void setMedicalID(String medicalID) {
        this.medicalID = medicalID;
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

    public ArrayList<Baby> getKids() {
        return kids;
    }

    public void setKids(ArrayList<Baby> kids) {
        this.kids = kids;
    }
}
