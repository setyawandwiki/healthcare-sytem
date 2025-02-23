package com.STWN.healthcare_project.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AppointmentRequest {
    private Long userId;
    @NotNull(message = "doctor id is required")
    private Long doctorId;
    @NotNull(message = "appointment date is required")
    private Long doctorSpecializationId;
    @NotNull(message = "start time is required")
    private LocalDate appointMent;
    @NotNull(message = "start time is required")
    private LocalTime startTime;
    @NotNull(message = "end time is required")
    private LocalTime endTime;
}
