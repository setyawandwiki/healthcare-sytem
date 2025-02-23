package com.STWN.healthcare_project.service;

import com.STWN.healthcare_project.common.exception.ResourceNotFoundException;
import com.STWN.healthcare_project.constant.AppointmentStatus;
import com.STWN.healthcare_project.constant.PaymentStatus;
import com.STWN.healthcare_project.entity.Appointment;
import com.STWN.healthcare_project.entity.DoctorSpecialization;
import com.STWN.healthcare_project.entity.Payment;
import com.STWN.healthcare_project.model.PaymentResponse;
import com.STWN.healthcare_project.repository.DoctorSpecializationRepository;
import com.STWN.healthcare_project.repository.PaymentRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final DoctorSpecializationRepository doctorSpecializationRepository;
    @Override
    @Transactional
    public PaymentResponse createPayment(Appointment appointment) {
        if (appointment.getStatus() != AppointmentStatus.PENDING) {
            throw new IllegalStateException("Payment can only be created for appointments with PENDING status");
        }

        DoctorSpecialization doctorSpecialization = doctorSpecializationRepository
                .findById(appointment.getDoctorSpecializationId())
                .orElseThrow(() -> new IllegalStateException("Doctor specialization not found"));

        BigDecimal hourlyFee = doctorSpecialization.getBaseFee();
        BigDecimal amount = calculateAmount(appointment, hourlyFee);
        String transactionId = UUID.randomUUID().toString();

        Payment payment = Payment.builder()
                .appointmentId(appointment.getId())
                .amount(amount)
                .paymentMethod("NOT_SELECTED")
                .status(PaymentStatus.PENDING)
                .transactionId(transactionId)
                .build();

        Payment save = paymentRepository.save(payment);

        return PaymentResponse.fromEntity(save);
    }

    @Override
    public PaymentResponse findByAppointmentId(Long appointmentId) {
        return paymentRepository.findByAppointmentId(appointmentId)
                .map(PaymentResponse::fromEntity)
                .orElse(null);
    }

    @Override
    public PaymentResponse cancelPayment(Long paymentId) {
        Payment payment = paymentRepository.findByIdAndLock(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found"));

        if (payment.getStatus() != PaymentStatus.PENDING) {
            throw new IllegalStateException("Only pending payments can be cancelled");
        }

        payment.setStatus(PaymentStatus.CANCELLED);
        Payment cancelledPayment = paymentRepository.save(payment);
        return PaymentResponse.fromEntity(cancelledPayment);
    }

    @Override
    public PaymentResponse recalculatePayment(Appointment updatedAppointment) {
        Payment payment = paymentRepository.findByAppointmentIdAndLock(updatedAppointment.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found for the appointment"));

        if (payment.getStatus() != PaymentStatus.PENDING) {
            throw new IllegalStateException("Only pending payments can be cancelled");
        }

        DoctorSpecialization doctorSpecialization = doctorSpecializationRepository
                .findById(updatedAppointment.getDoctorSpecializationId())
                .orElseThrow(() -> new IllegalStateException("Doctor specialization not found"));

        BigDecimal hourlyFee = doctorSpecialization.getBaseFee();
        BigDecimal newAmount = calculateAmount(updatedAppointment, hourlyFee);

        payment.setAmount(newAmount);
        paymentRepository.save(payment);
        return PaymentResponse.fromEntity(payment);
    }

    @Override
    public PaymentResponse cancelPaymentForAppointment(Long appointmentId) {
        Payment payment = paymentRepository.findByAppointmentIdAndLock(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found for the appointment"));

        if (payment.getStatus() != PaymentStatus.PENDING) {
            throw new IllegalStateException("Only pending payments can be cancelled");
        }

        payment.setStatus(PaymentStatus.CANCELLED);
        Payment cancelledPayment = paymentRepository.save(payment);
        return PaymentResponse.fromEntity(cancelledPayment);
    }

    private BigDecimal calculateAmount(Appointment appointment, BigDecimal hourlyFee) {
        Duration duration = Duration.between(appointment.getStartTime(), appointment.getEndTime());
        long hours = duration.toHours();
        if (duration.toMinutesPart() > 0 || duration.toSecondsPart() > 0) {
            hours += 1; // Round up to the next hour
        }
        return hourlyFee.multiply(BigDecimal.valueOf(hours)).setScale(2, RoundingMode.HALF_UP);
    }
}
