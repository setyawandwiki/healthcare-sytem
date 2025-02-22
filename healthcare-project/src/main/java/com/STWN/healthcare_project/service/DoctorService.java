package com.STWN.healthcare_project.service;

import com.STWN.healthcare_project.entity.Doctor;
import com.STWN.healthcare_project.entity.DoctorAvailability;
import com.STWN.healthcare_project.model.DoctorAvailabilityRequest;
import com.STWN.healthcare_project.model.DoctorRegistrationRequest;
import com.STWN.healthcare_project.model.DoctorResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

public interface DoctorService {
    DoctorResponse registerDoctor(DoctorRegistrationRequest request);
    Page<DoctorResponse> getAllDoctors(String keyword, Pageable pageable);
    DoctorResponse getDoctorById(Long doctorId);
    List<DoctorAvailability> getDoctorAvailabilitiesFromToday(Long doctorId);
    DoctorResponse addDoctorSpecialization(Long doctorId, Long specialization, BigDecimal fee, String cosultationType);
    void deleteDoctorAvailability(Long doctorId, Long availabilityId);
    DoctorResponse updateDoctorAvailability(Long doctorId, DoctorAvailabilityRequest request);
    Doctor getDoctorByUserId(Long userId);
}
