package com.STWN.healthcare_project.controller.admin;

import com.STWN.healthcare_project.model.DoctorRegistrationRequest;
import com.STWN.healthcare_project.model.DoctorResponse;
import com.STWN.healthcare_project.model.DoctorSpecializationRequest;
import com.STWN.healthcare_project.service.DoctorService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/doctors")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer")
@PreAuthorize("hasAnyRole('SUPER_ADMIN', 'HOSPITAL_ADMIN')")
public class AdminDoctorController {
    private final DoctorService doctorService;
    @PostMapping("/register")
    public ResponseEntity<DoctorResponse> registerDoctor(@Valid @RequestBody DoctorRegistrationRequest request) {
        DoctorResponse response = doctorService.registerDoctor(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{doctorId}/specializations")
    public ResponseEntity<DoctorResponse> addDoctorSpecialization(
            @PathVariable Long doctorId,
            @Valid @RequestBody DoctorSpecializationRequest request) {
        DoctorResponse response = doctorService.addDoctorSpecialization(
                doctorId,
                request.getSpecializationId(),
                request.getBaseFee(),
                request.getConsultationType()
        );
        return ResponseEntity.ok(response);
    }
}
