package com.example.babyland;

import java.io.Serializable;
import java.util.ArrayList;

public class Parent implements Serializable {
    private String name, surname, amka, phoneNumber, email, dateOfBirth, bloodType, partnersAmka;
    private Boolean partner;
    private ArrayList<Baby> kids;

    public Parent(String name, String surname, String amka, String phoneNumber, String email,
                  String dateOfBirth, String bloodType, String partnersAmka, Boolean partner,
                  ArrayList<Baby> kids){
        this.name = name;
        this.surname = surname;
        this.amka = amka;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.dateOfBirth = dateOfBirth;
        this.bloodType = bloodType;
        this.partnersAmka = partnersAmka;
        this.partner = partner;
        this.kids = kids;
    }

    public Parent(Parent u){
        this.name = u.getName();
        this.surname = u.getSurname();
        this.amka = u.getAmka();
        this.phoneNumber = u.getPhoneNumber();
        this.email = u.getEmail();
        this.dateOfBirth = u.getDateOfBirth();
        this.bloodType = u.getBloodType();
        this.partnersAmka = u.getPartnersAmka();
        this.partner = u.getPartner();
        this.kids = u.getKids();
    }

    public Parent() {}

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

    public String getPartnersAmka() {
        return partnersAmka;
    }

    public void setPartnersAmka(String partnersAmka) {
        this.partnersAmka = partnersAmka;
    }

    public Boolean getPartner() {
        return partner;
    }

    public void setPartner(Boolean partner) {
        this.partner = partner;
    }
}
