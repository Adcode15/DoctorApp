package com.doctor;

import com.doctor.entity.*;
import com.doctor.enums.*;
import com.doctor.service.*;
import java.util.*;

public class SimpleDemoApplication {
    public static void main(String[] args) {
        System.out.println("Doctor-Patient Management Platform Demo");
        System.out.println("=======================================");
        
        // Create services
        SymptomSpecialityService symptomSpecialityService = new SymptomSpecialityService();
        
        // Demo data - doctors
        System.out.println("\nCreating Demo Doctors:");
        System.out.println("---------------------");
        
        SimpleDoctor doctor1 = new SimpleDoctor(
            "Dr. John Smith", 
            DoctorCity.DELHI, 
            "john.smith@example.com", 
            "9876543210", 
            Speciality.ORTHOPEDIC
        );
        
        SimpleDoctor doctor2 = new SimpleDoctor(
            "Dr. Sarah Johnson", 
            DoctorCity.NOIDA, 
            "sarah.johnson@example.com", 
            "9876543211", 
            Speciality.GYNECOLOGY
        );
        
        SimpleDoctor doctor3 = new SimpleDoctor(
            "Dr. Mike Wilson", 
            DoctorCity.DELHI, 
            "mike.wilson@example.com", 
            "9876543212", 
            Speciality.DERMATOLOGY
        );
        
        SimpleDoctor doctor4 = new SimpleDoctor(
            "Dr. Emily Brown", 
            DoctorCity.FARIDABAD, 
            "emily.brown@example.com", 
            "9876543213", 
            Speciality.ENT
        );
        
        System.out.println(doctor1.getName() + " - " + doctor1.getSpeciality() + " - " + doctor1.getCity());
        System.out.println(doctor2.getName() + " - " + doctor2.getSpeciality() + " - " + doctor2.getCity());
        System.out.println(doctor3.getName() + " - " + doctor3.getSpeciality() + " - " + doctor3.getCity());
        System.out.println(doctor4.getName() + " - " + doctor4.getSpeciality() + " - " + doctor4.getCity());
        
        // Demo data - patients
        System.out.println("\nCreating Demo Patients:");
        System.out.println("----------------------");
        
        SimplePatient patient1 = new SimplePatient(
            "Alice Cooper", 
            "DELHI", 
            "alice.cooper@example.com", 
            "9876543214", 
            "arthritis"
        );
        
        SimplePatient patient2 = new SimplePatient(
            "Bob Smith", 
            "MUMBAI", 
            "bob.smith@example.com", 
            "9876543215", 
            "skin infection"
        );
        
        System.out.println(patient1.getName() + " - " + patient1.getCity() + " - Symptom: " + patient1.getSymptom());
        System.out.println(patient2.getName() + " - " + patient2.getCity() + " - Symptom: " + patient2.getSymptom());
        
        // Demo doctor suggestions
        System.out.println("\nDoctor Suggestions:");
        System.out.println("------------------");
        
        // For patient 1
        String city1 = patient1.getCity();
        String symptom1 = patient1.getSymptom();
        System.out.println("For " + patient1.getName() + " (City: " + city1 + ", Symptom: " + symptom1 + ")");
        
        try {
            DoctorCity doctorCity1 = DoctorCity.valueOf(city1);
            Speciality speciality1 = symptomSpecialityService.getSpecialityForSymptom(symptom1);
            
            if (speciality1 != null) {
                System.out.println("Recommended specialty: " + speciality1);
                
                // Find matching doctors
                List<SimpleDoctor> matchingDoctors = new ArrayList<>();
                for (SimpleDoctor doctor : Arrays.asList(doctor1, doctor2, doctor3, doctor4)) {
                    if (doctor.getCity() == doctorCity1 && doctor.getSpeciality() == speciality1) {
                        matchingDoctors.add(doctor);
                    }
                }
                
                if (matchingDoctors.isEmpty()) {
                    System.out.println("Result: There isn't any doctor present at your location for your symptom");
                } else {
                    System.out.println("Result: Found " + matchingDoctors.size() + " doctor(s):");
                    for (SimpleDoctor doctor : matchingDoctors) {
                        System.out.println("- " + doctor.getName() + " (" + doctor.getSpeciality() + ")");
                    }
                }
            } else {
                System.out.println("Result: Invalid symptom: " + symptom1);
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Result: We are still waiting to expand to your location");
        }
        
        // For patient 2
        String city2 = patient2.getCity();
        String symptom2 = patient2.getSymptom();
        System.out.println("\nFor " + patient2.getName() + " (City: " + city2 + ", Symptom: " + symptom2 + ")");
        
        try {
            DoctorCity doctorCity2 = DoctorCity.valueOf(city2);
            Speciality speciality2 = symptomSpecialityService.getSpecialityForSymptom(symptom2);
            
            if (speciality2 != null) {
                System.out.println("Recommended specialty: " + speciality2);
                
                // Find matching doctors
                List<SimpleDoctor> matchingDoctors = new ArrayList<>();
                for (SimpleDoctor doctor : Arrays.asList(doctor1, doctor2, doctor3, doctor4)) {
                    if (doctor.getCity() == doctorCity2 && doctor.getSpeciality() == speciality2) {
                        matchingDoctors.add(doctor);
                    }
                }
                
                if (matchingDoctors.isEmpty()) {
                    System.out.println("Result: There isn't any doctor present at your location for your symptom");
                } else {
                    System.out.println("Result: Found " + matchingDoctors.size() + " doctor(s):");
                    for (SimpleDoctor doctor : matchingDoctors) {
                        System.out.println("- " + doctor.getName() + " (" + doctor.getSpeciality() + ")");
                    }
                }
            } else {
                System.out.println("Result: Invalid symptom: " + symptom2);
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Result: We are still waiting to expand to your location");
        }
    }
} 