package com.STWN.healthcare_project.controller;

import com.STWN.healthcare_project.model.DoctorResponse;
import com.STWN.healthcare_project.service.DoctorService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}
