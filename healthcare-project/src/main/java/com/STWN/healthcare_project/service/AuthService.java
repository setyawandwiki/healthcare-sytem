package com.STWN.healthcare_project.service;

import com.STWN.healthcare_project.model.AuthRequest;
import com.STWN.healthcare_project.model.UserInfo;

public interface AuthService {
    UserInfo authenticate(AuthRequest request);
}
