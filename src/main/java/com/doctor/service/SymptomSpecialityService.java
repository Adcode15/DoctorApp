package com.doctor.service;

import com.doctor.enums.Speciality;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SymptomSpecialityService {
    private final Map<String, Speciality> symptomToSpeciality = new HashMap<>();
    private final List<String> validSymptoms;

    public SymptomSpecialityService() {
        // Orthopedic symptoms
        symptomToSpeciality.put("arthritis", Speciality.ORTHOPEDIC);
        symptomToSpeciality.put("back pain", Speciality.ORTHOPEDIC);
        symptomToSpeciality.put("tissue injuries", Speciality.ORTHOPEDIC);
        
        // Gynecology symptoms
        symptomToSpeciality.put("dysmenorrhea", Speciality.GYNECOLOGY);
        
        // Dermatology symptoms
        symptomToSpeciality.put("skin infection", Speciality.DERMATOLOGY);
        symptomToSpeciality.put("skin burn", Speciality.DERMATOLOGY);
        
        // ENT symptoms
        symptomToSpeciality.put("ear pain", Speciality.ENT);
        
        validSymptoms = Arrays.asList(
            "arthritis", "back pain", "tissue injuries",
            "dysmenorrhea", 
            "skin infection", "skin burn",
            "ear pain"
        );
    }

    public boolean isValidSymptom(String symptom) {
        if (symptom == null) {
            return false;
        }
        return validSymptoms.contains(symptom.toLowerCase());
    }

    public Speciality getSpecialityForSymptom(String symptom) {
        if (symptom == null) {
            return null;
        }
        return symptomToSpeciality.get(symptom.toLowerCase());
    }
} 