package com.STWN.healthcare_project.service;

import com.STWN.healthcare_project.common.exception.InvalidPasswordException;
import com.STWN.healthcare_project.model.AuthRequest;
import com.STWN.healthcare_project.model.UserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    @Override
    public UserInfo authenticate(AuthRequest request) {
        try{
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
            return (UserInfo) authentication.getPrincipal();
        }catch (Exception e){
            throw new InvalidPasswordException("Invalid username or password");
        }
    }
}
