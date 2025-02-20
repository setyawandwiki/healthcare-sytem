package com.STWN.healthcare_project.controller;

import com.STWN.healthcare_project.model.*;
import com.STWN.healthcare_project.service.AuthService;
import com.STWN.healthcare_project.service.JwtService;
import com.STWN.healthcare_project.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final AuthService authService;
    private final JwtService jwtService;
    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody UserRegisterRequest request){
        UserResponse userResponse = userService.registerRequest(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(userResponse);
    }
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> authenticate(@Valid @RequestBody AuthRequest request){
        UserInfo userInfo = authService.authenticate(request);
        String token = jwtService.generateToken(userInfo);

        AuthResponse authResponse = AuthResponse.fromUserInfo(userInfo, token);
        return ResponseEntity.ok(authResponse);
    }
}
