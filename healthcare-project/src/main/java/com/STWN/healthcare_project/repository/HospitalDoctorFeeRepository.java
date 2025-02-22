package com.STWN.healthcare_project.repository;

import com.STWN.healthcare_project.entity.HospitalDoctorFee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HospitalDoctorFeeRepository extends JpaRepository<HospitalDoctorFee, Long> {
    Optional<HospitalDoctorFee > findByHospitalIdAndDoctorSpecializationId(Long hospitalId, Long doctorSpecId);
}
