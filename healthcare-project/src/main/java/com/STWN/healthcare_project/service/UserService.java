package com.STWN.healthcare_project.service;

import com.STWN.healthcare_project.constant.RoleType;
import com.STWN.healthcare_project.model.UserRegisterRequest;
import com.STWN.healthcare_project.model.UserResponse;
import com.STWN.healthcare_project.model.UserUpdateRequest;

public interface UserService {
    UserResponse registerRequest(UserRegisterRequest request);
    UserResponse getUserById(Long id);
    UserResponse getUserByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    UserResponse updateUser(Long userId, UserUpdateRequest request);
    UserResponse grantUserRole(Long userId, RoleType roleType);
}
