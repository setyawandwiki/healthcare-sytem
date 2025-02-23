package com.STWN.healthcare_project.service;

import com.STWN.healthcare_project.common.exception.AppointmentConflictException;
import com.STWN.healthcare_project.common.exception.ResourceNotFoundException;
import com.STWN.healthcare_project.entity.*;
import com.STWN.healthcare_project.model.AppointmentRequest;
import com.STWN.healthcare_project.model.AppointmentResponse;
import com.STWN.healthcare_project.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AppointmentServiceImpl implements AppointmentService {
    private final UserRepository userRepository;
    private final DoctorRepository doctorRepository;
    private final DoctorSpecializationRepository doctorSpecializationRepository;
    private final HospitalDoctorFeeRepository hospitalDoctorFeeRepository;
    private final DoctorAvailabilityRepository doctorAvailabilityRepository;
    private final AppointmentRepository appointmentRepository;
    private final HospitalRepository hospitalRepository;
    @Override
    @Transactional
    public AppointmentResponse bookAppointment(AppointmentRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("user not found"));
        Doctor doctor = doctorRepository.findById(request.getDoctorId())
                .orElseThrow(()-> new ResourceNotFoundException("doctor not found"));
        Hospital hospital = hospitalRepository.findById(doctor.getHospitalId())
                .orElseThrow(() -> new ResourceNotFoundException("hospital not found"));
        DoctorSpecialization doctorSpecialization = doctorSpecializationRepository
                .findById(request.getDoctorSpecializationId())
                .orElseThrow(() -> new ResourceNotFoundException("doctor specialization not found"));
        HospitalDoctorFee fee = hospitalDoctorFeeRepository
                .findByHospitalIdAndDoctorSpecializationId(doctor.getHospitalId(), doctorSpecialization.getId())
                .orElseThrow(() -> new ResourceNotFoundException("doctor specialization fee not found"));

        boolean isDoctorAvailable = doctorAvailabilityRepository.isDoctorAvailable(
                request.getDoctorId(),
                request.getAppointMent(),
                request.getStartTime(),
                request.getEndTime(),
                fee.getConsultationType()
        );

        if(!isDoctorAvailable){
            throw new AppointmentConflictException("the doctor is not availble for time you requested");
        }
        List<Appointment> overlappingAppointments = appointmentRepository.findOverlappingAppointments(
                request.getDoctorId(),
                request.getAppointMent(),
                request.getStartTime(),
                request.getEndTime(),
                fee.getConsultationType()
        );

        if(!overlappingAppointments.isEmpty()){
            throw new AppointmentConflictException("the selected item slot overlapping with existing appintments of " +
                    "of the same consultation type");
        }

        Appointment appointment = Appointment.builder()
                .patientId(request.getUserId())
                .doctorId(request.getDoctorId())
                .hospitalId(doctor.getHospitalId())
                .doctorSpecializationId(request.getDoctorSpecializationId())
                .appointmentDate(request.getAppointMent())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .consultationType(fee.getConsultationType())
                .status(AppointmentStatus.PENDING)
                .build();

        appointmentRepository.save(appointment);

        return AppointmentResponse.builder()
                .id(appointment.getId())
                .patientId(appointment.getPatientId())
                .doctorId(appointment.getDoctorId())
                .doctorName(doctor.getName())
                .hospitalId(hospital.getId())
                .hospitalName(hospital.getName())
                .appointmentDate(appointment.getAppointmentDate())
                .startTime(appointment.getStartTime())
                .endTime(appointment.getEndTime())
                .consultationType(appointment.getConsultationType())
                .status(appointment.getStatus())
                .build();
    }
}
