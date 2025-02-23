package com.STWN.healthcare_project.controller;

import com.STWN.healthcare_project.model.AppointmentRequest;
import com.STWN.healthcare_project.model.AppointmentRescheduleRequest;
import com.STWN.healthcare_project.model.AppointmentResponse;
import com.STWN.healthcare_project.model.UserInfo;
import com.STWN.healthcare_project.service.AppointmentService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/appointments")
@SecurityRequirement(name = "Bearer")
public class AppointmentController {
    private final AppointmentService appointmentService;
    @PostMapping("/book")
    public ResponseEntity<AppointmentResponse> bookAppointment(@Valid @RequestBody AppointmentRequest request){
        AppointmentResponse response = appointmentService.bookAppointment(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("{appointmentId}/reschedule")
    public ResponseEntity<AppointmentResponse> reschedule(
            @PathVariable Long  appointmentId,
            @Valid @RequestBody AppointmentRescheduleRequest request
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserInfo userInfo = (UserInfo) authentication.getPrincipal();

        AppointmentResponse response = appointmentService.rescheduleAppointment(userInfo.getUserId(), appointmentId, request);

        return ResponseEntity.ok(response);
    }

    @PutMapping("{appointmentId}/cancel")
    public ResponseEntity<AppointmentResponse> cancelAppointment(
            @PathVariable Long appointmentId
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserInfo userInfo = (UserInfo) authentication.getPrincipal();

        appointmentService.cancelAppointment(userInfo.getUserId(), appointmentId);

        AppointmentResponse response = appointmentService.findById(appointmentId);

        return ResponseEntity.ok(response);
    }

    @GetMapping("")
    public ResponseEntity<List<AppointmentResponse>> listAppointments(
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserInfo userInfo = (UserInfo) authentication.getPrincipal();

        List<AppointmentResponse>  appointmentResponses = appointmentService.listUserAppointments(userInfo.getUserId());

        return ResponseEntity.ok(appointmentResponses);
    }

    @GetMapping("/{appointmentId}")
    public ResponseEntity<AppointmentResponse> getAppointment(
            @PathVariable Long appointmentId
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserInfo userInfo = (UserInfo) authentication.getPrincipal();

        AppointmentResponse appointmentResponse = appointmentService.findById(appointmentId);

        return ResponseEntity.ok(appointmentResponse);
    }
}
