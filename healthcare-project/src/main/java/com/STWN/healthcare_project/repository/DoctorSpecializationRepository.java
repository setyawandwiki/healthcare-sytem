package com.STWN.healthcare_project.repository;

import com.STWN.healthcare_project.entity.DoctorSpecialization;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DoctorSpecializationRepository extends JpaRepository<DoctorSpecialization, Long> {
    List<DoctorSpecialization> findByDoctorId(Long doctorId);
    List<DoctorSpecialization> findBySpecializationId(Long id);
    Optional<DoctorSpecialization> findByDoctorIdAndSpecialization(Long doctorId, Long specialization);
}
