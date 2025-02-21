package com.STWN.healthcare_project.model;

import com.STWN.healthcare_project.constant.RoleType;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GrantUserRoleRequest {
    @NotNull(message = "user id is required")
    private Long userId;
    @NotNull(message = "role type is required")
    private RoleType roleType;
}
