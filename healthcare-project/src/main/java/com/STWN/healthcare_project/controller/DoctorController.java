package com.STWN.healthcare_project.controller;

import com.STWN.healthcare_project.common.exception.ForbiddenAccessException;
import com.STWN.healthcare_project.entity.Doctor;
import com.STWN.healthcare_project.model.DoctorAvailabilityRequest;
import com.STWN.healthcare_project.model.DoctorResponse;
import com.STWN.healthcare_project.model.UserInfo;
import com.STWN.healthcare_project.service.DoctorService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/doctors")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer")
public class DoctorController {
    private final DoctorService doctorService;
    @GetMapping
    public ResponseEntity<Page<DoctorResponse>> searchDoctors(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        Sort.Direction direction = sortDir.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(direction, sortBy));

        Page<DoctorResponse> doctors = doctorService.getAllDoctors(keyword, pageRequest);
        return ResponseEntity.ok(doctors);
    }

    @PreAuthorize("hasRole('DOCTOR')")
    @PostMapping("/{doctorId}/availabilities")
    public ResponseEntity<DoctorResponse> updateDoctorAvailability(
            @PathVariable Long doctorId,
            @Valid @RequestBody DoctorAvailabilityRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserInfo userInfo = (UserInfo) authentication.getPrincipal();

        Doctor existingDoctor = doctorService.getDoctorByUserId(userInfo.getUserId());
        if (!existingDoctor.getUserId().equals(userInfo.getUserId())) {
            throw new ForbiddenAccessException("Cannot update doctor availability");
        }
        DoctorResponse response = doctorService.updateDoctorAvailability(existingDoctor.getId(), request);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('DOCTOR')")
    @DeleteMapping("/availabilities/{availabilityId}")
    public ResponseEntity<Void> deleteDoctorAvailability(@PathVariable Long availabilityId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserInfo userInfo = (UserInfo) authentication.getPrincipal();

        Doctor existingDoctor = doctorService.getDoctorByUserId(userInfo.getUserId());
        doctorService.deleteDoctorAvailability(existingDoctor.getId(), availabilityId);
        return ResponseEntity.noContent().build();
    }
}
