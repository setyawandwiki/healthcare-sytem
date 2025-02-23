package com.STWN.healthcare_project.service;

import com.STWN.healthcare_project.model.AppointmentRequest;
import com.STWN.healthcare_project.model.AppointmentResponse;

public interface AppointmentService {
    AppointmentResponse bookAppointment(AppointmentRequest request);
}
