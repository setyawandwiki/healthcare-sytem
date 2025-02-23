package com.STWN.healthcare_project.model;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentRescheduleRequest {
    @NotNull(message = "appointment date is required")
    private LocalDate appointmentDate;
    @NotNull(message = "start time is required")
    private LocalTime startTime;
    @NotNull(message = "end time is required")
    private LocalTime endTime;
}
