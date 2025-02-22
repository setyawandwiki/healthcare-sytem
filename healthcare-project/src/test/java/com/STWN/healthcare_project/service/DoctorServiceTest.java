package com.STWN.healthcare_project.service;

import com.STWN.healthcare_project.constant.RoleType;
import com.STWN.healthcare_project.entity.*;
import com.STWN.healthcare_project.model.DoctorRegistrationRequest;
import com.STWN.healthcare_project.model.DoctorSpecializationRequest;
import com.STWN.healthcare_project.repository.*;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class DoctorServiceTest {
    @Mock
    UserRepository userRepository;
    @Mock
    RoleRepository roleRepository;
    @Mock
    UserService userService;
    @Mock
    HospitalRepository hospitalRepository;
    @Mock
    DoctorRepository doctorRepository;
    @Mock
    SpecializationRepository specializationRepository;
    @Mock
    DoctorSpecializationRepository doctorSpecializationRepository;
    @Mock
    HospitalDoctorFeeRepository hospitalDoctorFeeRepository;
    @Mock
    CacheService cacheService;
    @Mock
    DoctorAvailabilityRepository doctorAvailabilityRepository;

    @InjectMocks
    DoctorServiceImpl doctorService;

    private DoctorRegistrationRequest request;
    private User user;
    private Hospital hospital;
    private Role doctorRole;
    private Specialization specialization;
    private Doctor doctor;
    private DoctorSpecialization doctorSpecialization;

    @BeforeEach
    void setUp(){
        request = new DoctorRegistrationRequest();
        request.setUserId(1L);
        request.setHospitalId(1L);
        request.setBio("Doctor Bio");
        DoctorSpecializationRequest specializationRequest = new DoctorSpecializationRequest();
        specializationRequest.setSpecializationId(1L);
        specializationRequest.setBaseFee(new BigDecimal("100.00"));
        request.setSpecializations(Collections.singletonList(specializationRequest));

        user = new User();
        user.setUserId(1L);
        user.setUsername("doctoruser");
        user.setEmail("doctor@example.com");

        hospital = new Hospital();
        hospital.setId(1L);
        hospital.setName("test hospital");

        doctorRole = new Role();
        doctorRole.setRoleId(1L);
        doctorRole.setName(RoleType.DOCTOR);

        specialization = new Specialization();
        specialization.setId(1L);
        specialization.setName("general medicine");

        doctor = new Doctor();
        doctor.setId(1L);
        doctor.setUserId(1L);
        doctor.setHospitalId(1L);
        doctor.setBio("doctor bio");
        doctor.setName("dr tirta");
        doctor.setCreatedAt(LocalDateTime.now());
        doctor.setUpdatedAt(LocalDateTime.now());

        doctorSpecialization = DoctorSpecialization.builder()
                .doctorId(doctor.getId())
                .specializationId(specialization.getId())
                .baseFee(BigDecimal.valueOf(100.00))
                .build();
    }
}