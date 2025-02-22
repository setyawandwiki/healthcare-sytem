package com.STWN.healthcare_project.service;

import com.STWN.healthcare_project.common.exception.ForbiddenAccessException;
import com.STWN.healthcare_project.common.exception.ResourceNotFoundException;
import com.STWN.healthcare_project.common.exception.UserNotFoundException;
import com.STWN.healthcare_project.constant.RoleType;
import com.STWN.healthcare_project.entity.*;
import com.STWN.healthcare_project.model.*;
import com.STWN.healthcare_project.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DoctorServiceImpl implements DoctorService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserService userService;
    private final HospitalRepository hospitalRepository;
    private final DoctorRepository doctorRepository;
    private final SpecializationRepository specializationRepository;
    private final DoctorSpecializationRepository doctorSpecializationRepository;
    private final HospitalDoctorFeeRepository hospitalDoctorFeeRepository;
    private final CacheService cacheService;
    private final DoctorAvailabilityRepository doctorAvailabilityRepository;

    public static final String DOCTOR_CACHE_KEY = "cache:key:doctor:";
    @Override
    public DoctorResponse registerDoctor(DoctorRegistrationRequest request) {
        log.info("Registering new doctor with user ID: {}", request.getUserId());

        // Fetch and validate user
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + request.getUserId()));

        // Fetch and validate hospital
        Hospital hospital = hospitalRepository.findById(request.getHospitalId())
                .orElseThrow(() -> new ResourceNotFoundException("Hospital not found with id: " + request.getHospitalId()));

        // Fetch doctor role
        Role doctorRole = roleRepository.findByName(RoleType.DOCTOR)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor role not found"));

        // Grant doctor role to user
        userService.grantUserRole(user.getUserId(), RoleType.DOCTOR);

        // Create and save doctor entity
        Doctor doctor = new Doctor();
        doctor.setUserId(user.getUserId());
        doctor.setHospitalId(hospital.getId());
        doctor.setBio(request.getBio());
        doctor.setName(request.getName());
        doctor = doctorRepository.save(doctor);

        // Process specializations
        List<SpecializationInfo> specializationInfos = new ArrayList<>();
        for (DoctorSpecializationRequest specRequest : request.getSpecializations()) {
            Specialization specialization = specializationRepository.findById(specRequest.getSpecializationId())
                    .orElseThrow(() -> new ResourceNotFoundException("Specialization not found with id: " + specRequest.getSpecializationId()));

            DoctorSpecialization doctorSpecialization = new DoctorSpecialization();
            doctorSpecialization.setDoctorId(doctor.getId());
            doctorSpecialization.setSpecializationId(specialization.getId());
            doctorSpecialization.setBaseFee(specRequest.getBaseFee());
            doctorSpecialization = doctorSpecializationRepository.save(doctorSpecialization);

            HospitalDoctorFee hospitalDoctorFee = new HospitalDoctorFee();
            hospitalDoctorFee.setHospitalId(hospital.getId());
            hospitalDoctorFee.setDoctorSpecializationId(doctorSpecialization.getId());
            hospitalDoctorFee.setFee(specRequest.getBaseFee()); // Initially set to base fee, can be updated later
            hospitalDoctorFee.setConsultationType(specRequest.getConsultationType());
            hospitalDoctorFee = hospitalDoctorFeeRepository.save(hospitalDoctorFee);

            specializationInfos.add(
                    SpecializationInfo.builder()
                            .specializationId(specialization.getId())
                            .specializationName(specialization.getName())
                            .baseFee(doctorSpecialization.getBaseFee())
                            .hospitalFee(hospitalDoctorFee.getFee())
                            .consultationType(hospitalDoctorFee.getConsultationType())
                            .build()
            );
        }

        log.info("Doctor registered successfully with ID: {}", doctor.getId());

        String cacheKey = DOCTOR_CACHE_KEY + doctor.getId();

        // Construct and return DoctorResponse
        DoctorResponse doctorResponse = DoctorResponse.builder()
                .id(doctor.getId())
                .userId(user.getUserId())
                .name(doctor.getName())
                .email(user.getEmail())
                .hospitalId(hospital.getId())
                .hospitalName(hospital.getName())
                .bio(doctor.getBio())
                .createdAt(doctor.getCreatedAt())
                .updatedAt(doctor.getUpdatedAt())
                .specializations(specializationInfos)
                .availabilities(new ArrayList<>()) // No availabilities yet for a new doctor
                .build();
        cacheService.put(cacheKey,doctorResponse, Duration.ofHours(1));

        return doctorResponse;
    }

    @Override
    public Page<DoctorResponse> getAllDoctors(String keyword, Pageable pageable) {
        return doctorRepository.searchDoctor(keyword, pageable)
                .map(doctor -> getDoctorById(doctor.getId()));
    }

    @Override
    public DoctorResponse getDoctorById(Long doctorId) {
        String cacheKey = DOCTOR_CACHE_KEY + doctorId;
        return cacheService.get(cacheKey, DoctorResponse.class).orElseGet(() -> {
            Doctor doctor = doctorRepository.findById(doctorId)
                    .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id "+doctorId));
            DoctorResponse doctorResponse = convertToDoctorResponse(doctor);
            cacheService.put(cacheKey,doctorResponse, Duration.ofHours(1));
            return doctorResponse;
        });
    }

    @Override
    public List<DoctorAvailability> getDoctorAvailabilitiesFromToday(Long doctorId) {
        return doctorAvailabilityRepository.findAvailabilitiesByDoctorIdFromToday(doctorId);
    }

    @Override
    @Transactional
    public DoctorResponse addDoctorSpecialization(Long doctorId, Long specializationId,
                                                  BigDecimal fee, String consultationType) {

        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id: " + doctorId));

        specializationRepository.findById(specializationId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Specialization not found with id: " + specializationId));

        DoctorSpecialization doctorSpecialization = new DoctorSpecialization();
        doctorSpecialization.setDoctorId(doctorId);
        doctorSpecialization.setSpecializationId(specializationId);
        doctorSpecialization.setBaseFee(fee);
        doctorSpecialization = doctorSpecializationRepository.save(doctorSpecialization);

        HospitalDoctorFee hospitalDoctorFee = new HospitalDoctorFee();
        hospitalDoctorFee.setHospitalId(doctor.getHospitalId());
        hospitalDoctorFee.setDoctorSpecializationId(doctorSpecialization.getId());
        hospitalDoctorFee.setFee(fee);
        hospitalDoctorFee.setConsultationType(consultationType);
        hospitalDoctorFeeRepository.save(hospitalDoctorFee);

        String cacheKey = DOCTOR_CACHE_KEY + doctor.getId();
        cacheService.evict(cacheKey);

        return convertToDoctorResponse(doctor);
    }

    @Override
    public void deleteDoctorAvailability(Long doctorId, Long availabilityId) {
        DoctorAvailability availability = doctorAvailabilityRepository.findById(doctorId)
                .orElseThrow(() -> new ResourceNotFoundException("doctor availability not found with id : "
                        + availabilityId));

        DoctorResponse doctorResponse = getDoctorById(doctorId);
        if(!availability.getDoctorId().equals(doctorId)){
            throw new ForbiddenAccessException("cannot update doctor availability");
        }
        doctorAvailabilityRepository.delete(availability);
    }

    @Override
    @Transactional
    public DoctorResponse updateDoctorAvailability(Long doctorId, DoctorAvailabilityRequest request) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id : " + doctorId));
        DoctorAvailability availability = new DoctorAvailability();
        availability.setDoctorId(doctorId);
        availability.setDate(request.getDate());
        availability.setStartTime(request.getStartTime());
        availability.setEndTime(request.getEndTime());
        availability.setConsultationType(request.getConsultationType());
        availability.setAvailable(true);

        doctorAvailabilityRepository.save(availability);

        return convertToDoctorResponse(doctor);
    }

    @Override
    public Doctor getDoctorByUserId(Long userId) {
        return doctorRepository.findByUserId(userId)
                .orElseThrow(()-> new ResourceNotFoundException("doctor with id : "+ userId + " is not found"));
    }

    private DoctorResponse convertToDoctorResponse(Doctor doctor) {
        User user = userRepository.findById(doctor.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found for doctor with id: " + doctor.getId()));

        String hospitalName = hospitalRepository.findById(doctor.getHospitalId())
                .map(Hospital::getName)
                .orElse("Unknown Hospital");

        List<DoctorSpecialization> specializations = doctorSpecializationRepository.findByDoctorId(doctor.getId());

        List<SpecializationInfo> specializationInfos = specializations.stream()
                .map(spec -> {
                    String specializationName = specializationRepository.findById(spec.getSpecializationId())
                            .map(Specialization::getName)
                            .orElse("Unknown Specialization");

                    HospitalDoctorFee fee = hospitalDoctorFeeRepository
                            .findByHospitalIdAndDoctorSpecializationId(doctor.getHospitalId(), spec.getId())
                            .orElse(new HospitalDoctorFee());

                    return SpecializationInfo.builder()
                            .specializationId(spec.getId())
                            .specializationName(specializationName)
                            .baseFee(spec.getBaseFee())
                            .hospitalFee(fee.getFee())
                            .consultationType(fee.getConsultationType())
                            .build();
                })
                .collect(Collectors.toList());

        List<AvailabilityInfo> availabilities = getDoctorAvailabilitiesFromToday(doctor.getId())
                .stream()
                .map(doctorAvailability -> AvailabilityInfo.builder()
                        .id(doctorAvailability.getId())
                        .isAvailable(true)
                        .startDateTime(LocalDateTime.of(
                                doctorAvailability.getDate(),
                                doctorAvailability.getStartTime()
                        ))
                        .endDateTime(LocalDateTime.of(
                                doctorAvailability.getDate(),
                                doctorAvailability.getEndTime()
                        ))
                        .consultationType(doctorAvailability.getConsultationType())
                        .build())
                .toList();

        return DoctorResponse.builder()
                .id(doctor.getId())
                .userId(user.getUserId())
                .name(doctor.getName())
                .email(user.getEmail())
                .hospitalId(doctor.getHospitalId())
                .hospitalName(hospitalName)
                .bio(doctor.getBio())
                .createdAt(doctor.getCreatedAt())
                .updatedAt(doctor.getUpdatedAt())
                .specializations(specializationInfos)
                .availabilities(availabilities)
                .build();
    }
}
