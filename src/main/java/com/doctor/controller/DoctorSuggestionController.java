package com.doctor.controller;

import com.doctor.entity.Doctor;
import com.doctor.entity.Patient;
import com.doctor.enums.DoctorCity;
import com.doctor.enums.Speciality;
import com.doctor.service.DoctorService;
import com.doctor.service.PatientService;
import com.doctor.service.SymptomSpecialityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/suggestions")
public class DoctorSuggestionController {
    private final PatientService patientService;
    private final DoctorService doctorService;
    private final SymptomSpecialityService symptomSpecialityService;

    @Autowired
    public DoctorSuggestionController(
            PatientService patientService,
            DoctorService doctorService,
            SymptomSpecialityService symptomSpecialityService
    ) {
        this.patientService = patientService;
        this.doctorService = doctorService;
        this.symptomSpecialityService = symptomSpecialityService;
    }

    @GetMapping
    public ResponseEntity<?> suggestDoctorsByQuery(
            @RequestParam String city,
            @RequestParam String symptom) {
        try {
            DoctorCity doctorCity = DoctorCity.valueOf(city.toUpperCase());
            
            // Get the speciality based on the symptom
            var speciality = symptomSpecialityService.getSpecialityForSymptom(symptom);
            if (speciality == null) {
                return ResponseEntity.badRequest()
                        .body(Map.of("message", "Invalid symptom: " + symptom));
            }
            
            // Find doctors with the required speciality in the specified city
            List<Doctor> doctors = doctorService.findDoctorsByCityAndSpeciality(doctorCity, speciality);
            
            if (doctors.isEmpty()) {
                return ResponseEntity.ok(Map.of(
                        "message", "There isn't any doctor present at your location for your symptom"
                ));
            }
            
            return ResponseEntity.ok(doctors);
        } catch (IllegalArgumentException e) {
            // City not found in DoctorCity enum
            return ResponseEntity.ok(Map.of(
                    "message", "We are still waiting to expand to your location"
            ));
        }
    }

    @GetMapping("/{patientId}")
    public ResponseEntity<?> suggestDoctors(@PathVariable Long patientId) {
        try {
            Patient patient = patientService.getPatientById(patientId);
            
            try {
                DoctorCity patientCity = DoctorCity.valueOf(patient.getCity().toUpperCase());
                
                // Get the speciality based on the symptom
                var speciality = symptomSpecialityService.getSpecialityForSymptom(patient.getSymptom());
                if (speciality == null) {
                    return ResponseEntity.badRequest()
                            .body(Map.of("message", "Invalid symptom: " + patient.getSymptom()));
                }
                
                // Find doctors with the required speciality in the patient's city
                List<Doctor> doctors = doctorService.findDoctorsByCityAndSpeciality(patientCity, speciality);
                
                if (doctors.isEmpty()) {
                    return ResponseEntity.ok(Map.of(
                            "message", "There isn't any doctor present at your location for your symptom"
                    ));
                }
                
                return ResponseEntity.ok(doctors);
            } catch (IllegalArgumentException e) {
                // City not found in DoctorCity enum
                return ResponseEntity.ok(Map.of(
                        "message", "We are still waiting to expand to your location"
                ));
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
} 