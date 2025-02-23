package com.STWN.healthcare_project.service;

import com.STWN.healthcare_project.entity.Appointment;
import com.STWN.healthcare_project.model.PaymentResponse;

public interface PaymentService {
    PaymentResponse createPayment(Appointment appointment);
    PaymentResponse findByAppointmentId(Long appointmentId);
    PaymentResponse cancelPayment(Long paymentId);
    PaymentResponse recalculatePayment(Appointment updatedAppointment);
    PaymentResponse cancelPaymentForAppointment(Long appointmentId);
}
