package com.STWN.healthcare_project.service;

import com.STWN.healthcare_project.model.AppointmentRequest;
import com.STWN.healthcare_project.model.AppointmentRescheduleRequest;
import com.STWN.healthcare_project.model.AppointmentResponse;

import java.util.List;

public interface AppointmentService {
    AppointmentResponse bookAppointment(AppointmentRequest request);

    AppointmentResponse rescheduleAppointment(Long userId, Long appointmentId, AppointmentRescheduleRequest request);
    List<AppointmentResponse> listUserAppointments(Long userId);
    void cancelAppointment(Long userId, Long appointmentId);
    AppointmentResponse findById(Long appointmentId);
      List<AppointmentResponse> listDoctorAppointments(Long doctorId);
//    AppointmentMeetingResponse getMeetingStatus(Long userId, Long appointmentId);
}
