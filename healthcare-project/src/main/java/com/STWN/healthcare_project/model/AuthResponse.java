package com.STWN.healthcare_project.model;

import com.STWN.healthcare_project.constant.RoleType;
import com.STWN.healthcare_project.entity.Role;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AuthResponse {
    private String token;
    private Long userId;
    private String username;
    private String email;
    private List<RoleType> roles;

    public static AuthResponse fromUserInfo(UserInfo userInfo, String token){
        return AuthResponse.builder()
                .token(token)
                .userId(userInfo.getUserId())
                .username(userInfo.getUsername())
                .email(userInfo.getEmail())
                .roles(userInfo.getRoles().stream().map(Role::getName).toList())
                .build();
    }
}
