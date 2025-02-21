package com.STWN.healthcare_project.controller;

import com.STWN.healthcare_project.common.exception.ForbiddenAccessException;
import com.STWN.healthcare_project.entity.User;
import com.STWN.healthcare_project.model.UserInfo;
import com.STWN.healthcare_project.model.UserResponse;
import com.STWN.healthcare_project.model.UserUpdateRequest;
import com.STWN.healthcare_project.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping(path = "/users")
@SecurityRequirement(name = "Bearer")
public class UserController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<UserResponse> me(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserInfo userInfo = (UserInfo) authentication.getPrincipal();

        UserResponse userResponse = UserResponse.fromUSerAndRole(userInfo.getUser(), userInfo.getRoles());
        return ResponseEntity.ok(userResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable("id") Long id,
                                                   @Valid @RequestBody UserUpdateRequest request
    ){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserInfo userInfo = (UserInfo) authentication.getPrincipal();

        if(!userInfo.getUser().getUserId().equals(id)){
            throw new ForbiddenAccessException("user is not allowed to update");
        }

        UserResponse userResponse = userService.updateUser(id, request);
        return ResponseEntity.ok(userResponse);
    }
}
