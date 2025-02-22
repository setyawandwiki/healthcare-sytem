package com.STWN.healthcare_project.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class DoctorResponse {
    private Long id;
    private Long userId;
    private String name;
    private String email;
    private Long hospitalId;
    private String hospitalName;
    private String bio;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<SpecializationInfo> specializations;
    private List<AvailabilityInfo> availabilities;
}
