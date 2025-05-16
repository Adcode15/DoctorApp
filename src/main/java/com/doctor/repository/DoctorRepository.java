package com.doctor.repository;

import com.doctor.entity.Doctor;
import com.doctor.enums.DoctorCity;
import com.doctor.enums.Speciality;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    List<Doctor> findByCityAndSpeciality(DoctorCity city, Speciality speciality);
} 