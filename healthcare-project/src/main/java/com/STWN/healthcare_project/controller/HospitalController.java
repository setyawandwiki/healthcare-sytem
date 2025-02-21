package com.STWN.healthcare_project.controller;

import com.STWN.healthcare_project.model.HospitalResponse;
import com.STWN.healthcare_project.service.HospitalService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping
@SecurityRequirement(name = "Bearer")
public class HospitalController {
    private final HospitalService hospitalService;
    @GetMapping
    public ResponseEntity<Page<HospitalResponse>> search(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir
    ) {
        Sort.Direction direction = sortDir.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(direction, sortBy));

        Page<HospitalResponse> hospitalResponses = hospitalService.search(keyword, pageRequest);
        return ResponseEntity.ok(hospitalResponses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<HospitalResponse> get(@PathVariable Long id
    ) {
        HospitalResponse response = hospitalService.get(id);
        return ResponseEntity.ok(response);
    }
}
