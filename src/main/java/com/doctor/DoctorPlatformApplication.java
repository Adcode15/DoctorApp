package com.doctor;

import com.doctor.entity.Doctor;
import com.doctor.entity.Patient;
import com.doctor.enums.DoctorCity;
import com.doctor.enums.Speciality;
import com.doctor.repository.DoctorRepository;
import com.doctor.repository.PatientRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class DoctorPlatformApplication {
    public static void main(String[] args) {
        SpringApplication.run(DoctorPlatformApplication.class, args);
    }
    
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("*")
                        .allowedMethods("GET", "POST", "PUT", "DELETE")
                        .allowedHeaders("*");
            }
        };
    }
    
    @Bean
    public CommandLineRunner initData(DoctorRepository doctorRepository, PatientRepository patientRepository) {
        return args -> {
            if (doctorRepository.count() == 0) {
                doctorRepository.save(new Doctor("Dr. John Doe", DoctorCity.DELHI, "john.doe@example.com", "9876543210", Speciality.ORTHOPEDIC));
                doctorRepository.save(new Doctor("Dr. Jane Smith", DoctorCity.NOIDA, "jane.smith@example.com", "9876543211", Speciality.GYNECOLOGY));
                doctorRepository.save(new Doctor("Dr. Bob Johnson", DoctorCity.FARIDABAD, "bob.johnson@example.com", "9876543212", Speciality.DERMATOLOGY));
                doctorRepository.save(new Doctor("Dr. Sarah Brown", DoctorCity.DELHI, "sarah.brown@example.com", "9876543213", Speciality.ENT));
                doctorRepository.save(new Doctor("Dr. Michael Davis", DoctorCity.NOIDA, "michael.davis@example.com", "9876543214", Speciality.ORTHOPEDIC));
            }
            if (patientRepository.count() == 0) {
                patientRepository.save(new Patient("Alice Cooper", "Delhi", "alice@example.com", "9876543215", "arthritis"));
                patientRepository.save(new Patient("Bob Dylan", "Noida", "bob@example.com", "9876543216", "skin infection"));
                patientRepository.save(new Patient("Charlie Parker", "Faridabad", "charlie@example.com", "9876543217", "ear pain"));
                patientRepository.save(new Patient("Diana Ross", "Mumbai", "diana@example.com", "9876543218", "back pain"));
            }
        };
    }
} 