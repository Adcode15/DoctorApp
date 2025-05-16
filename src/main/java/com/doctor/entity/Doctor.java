package com.doctor.entity;

import com.doctor.enums.DoctorCity;
import com.doctor.enums.Speciality;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "doctors")
public class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is required")
    @Size(min = 3, message = "Name must be at least 3 characters long")
    private String name;

    @NotNull(message = "City is required")
    @Enumerated(EnumType.STRING)
    private DoctorCity city;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Column(unique = true)
    private String email;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^\\d{10,}$", message = "Phone number must be at least 10 digits")
    private String phoneNumber;

    @NotNull(message = "Speciality is required")
    @Enumerated(EnumType.STRING)
    private Speciality speciality;

    // Default constructor
    public Doctor() {
    }

    // Parameterized constructor
    public Doctor(String name, DoctorCity city, String email, String phoneNumber, Speciality speciality) {
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