package com.STWN.healthcare_project.service;

import com.STWN.healthcare_project.model.UserRegisterRequest;
import com.STWN.healthcare_project.model.UserResponse;

public interface UserService {
    UserResponse registerRequest(UserRegisterRequest request);
    UserResponse getUserById(Long id);
    UserResponse getUserByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
