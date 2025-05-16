package com.doctor.entity;

import com.doctor.enums.DoctorCity;
import com.doctor.enums.Speciality;

public class SimpleDoctor {
    private Long id;
    private String name;
    private DoctorCity city;
    private String email;
    private String phoneNumber;
    private Speciality speciality;

    // Default constructor
    public SimpleDoctor() {
    }

    // Parameterized constructor
    public SimpleDoctor(String name, DoctorCity city, String email, String phoneNumber, Speciality speciality) {
        this.name = name;
        this.city = city;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.speciality = speciality;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DoctorCity getCity() {
        return city;
    }

    public void setCity(DoctorCity city) {
        this.city = city;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Speciality getSpeciality() {
        return speciality;
    }

    public void setSpeciality(Speciality speciality) {
        this.speciality = speciality;
    }
} 