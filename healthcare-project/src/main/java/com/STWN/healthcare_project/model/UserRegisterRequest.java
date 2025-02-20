package com.STWN.healthcare_project.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class UserRegisterRequest {
    @NotBlank(message = "username is required")
    @Size(min = 3, max = 50, message = "username must between 3 and 50 characters")
    private String username;
    @NotBlank(message = "email is required")
    @Email(message = "invalid email format")
    private String email;
    @NotBlank(message = "password is required")
    @Size(min = 8, max = 30, message = "password must between 8 and 30 character")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$",
            message = "Password must contain at least one digit, one lowercase, one uppercase, one special character, and no whitespace")
    private String password;
    @NotBlank(message = "password confirmation is required")
    private String passwordConfirmation;
}
