package com.STWN.healthcare_project.repository;

import com.STWN.healthcare_project.constant.PaymentStatus;
import com.STWN.healthcare_project.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    @Query(value = "SELECT * FROM payment WHERE appointment_id = :appointmentId FOR UPDATE", nativeQuery = true)
    Optional<Payment> findByAppointmentIdAndLock(Long appointmentId);

    @Query(value = "SELECT * FROM payment WHERE id = :id FOR UPDATE", nativeQuery = true)
    Optional<Payment> findByIdAndLock(@Param("id") Long id);

    Optional<Payment> findByAppointmentId(Long appointmentId);

    @Query("SELECT p FROM Payment p WHERE p.appointmentId = :appointmentId AND p.status = 'COMPLETED'")
    Optional<Payment> findCompletedPaymentByAppointmentId(@Param("appointmentId") Long appointmentId);

    List<Payment> findByStatus(PaymentStatus status);
}
