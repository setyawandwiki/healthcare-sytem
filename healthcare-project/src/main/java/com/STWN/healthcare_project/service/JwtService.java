package com.STWN.healthcare_project.service;

import com.STWN.healthcare_project.model.UserInfo;

public interface JwtService {
    String generateToken(UserInfo userInfo);
    boolean validateToken(String token);
    String getUsernameFromToken(String token);
}
