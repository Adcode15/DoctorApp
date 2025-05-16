package com.doctor.service;

import com.doctor.entity.Patient;
import com.doctor.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientService {
    private final PatientRepository patientRepository;
    private final SymptomSpecialityService symptomSpecialityService;

    @Autowired
    public PatientService(PatientRepository patientRepository, SymptomSpecialityService symptomSpecialityService) {
        this.patientRepository = patientRepository;
        this.symptomSpecialityService = symptomSpecialityService;
    }

    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    public Patient addPatient(Patient patient) {
        if (!symptomSpecialityService.isValidSymptom(patient.getSymptom())) {
            throw new IllegalArgumentException("Invalid symptom: " + patient.getSymptom());
        }
        return patientRepository.save(patient);
    }

    public void removePatient(Long id) {
        patientRepository.deleteById(id);
    }

    public Patient getPatientById(Long id) {
        return patientRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Patient not found with id: " + id));
    }
} 