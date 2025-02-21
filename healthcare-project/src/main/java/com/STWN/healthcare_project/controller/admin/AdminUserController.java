package com.STWN.healthcare_project.controller.admin;

import com.STWN.healthcare_project.model.GrantUserRoleRequest;
import com.STWN.healthcare_project.model.UserResponse;
import com.STWN.healthcare_project.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer")
@PreAuthorize("hasRole('SUPER_ADMIN')")
public class AdminUserController {
    private final UserService userService;
    @PostMapping
    public ResponseEntity<UserResponse> grant(@Valid @RequestBody GrantUserRoleRequest request){
        UserResponse userResponse = userService.grantUserRole(request.getUserId(), request.getRoleType());
        return ResponseEntity.ok(userResponse);
    }
}
