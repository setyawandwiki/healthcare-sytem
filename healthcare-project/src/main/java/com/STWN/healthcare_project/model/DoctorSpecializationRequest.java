package com.STWN.healthcare_project.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class DoctorSpecializationRequest {
    @NotNull(message = "Specialization ID is required")
    @Positive(message = "Specialization ID must be positive")
    private Long specializationId;

    @NotNull(message = "Fee is required")
    @Positive(message = "Fee must be positive")
    private BigDecimal baseFee;

    @NotNull(message = "Consultation type is required")
    @Pattern(regexp = "^(ONLINE|OFFLINE)$", message = "Consultation type must be either ONLINE or OFFLINE")
    private String consultationType;
}
