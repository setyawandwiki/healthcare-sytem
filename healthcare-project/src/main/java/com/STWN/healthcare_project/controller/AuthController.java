package com.STWN.healthcare_project.controller;

import com.STWN.healthcare_project.model.UserRegisterRequest;
import com.STWN.healthcare_project.model.UserResponse;
import com.STWN.healthcare_project.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    public ResponseEntity<UserResponse> register(@Valid @RequestBody UserRegisterRequest request){
        UserResponse userResponse = userService.registerRequest(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(userResponse);
    }
}
