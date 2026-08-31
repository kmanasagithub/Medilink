package com.medilink.backend.DoctorHospital;

import com.medilink.backend.Doctor.dto.DoctorResponse;
import com.medilink.backend.DoctorHospital.dto.DoctorHospitalRequest;
import com.medilink.backend.DoctorHospital.dto.DoctorHospitalResponse;
import com.medilink.backend.DoctorHospital.dto.DoctorHospitalUpdateRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1.0/doctor-hospital")
public class DoctorHospitalController {

    private final DoctorHospitalService doctorHospitalService;

    // Assign a doctor to a hospital
    @PostMapping
    public ResponseEntity<DoctorHospitalResponse> assignDoctorToHospital(
            @RequestBody @Valid DoctorHospitalRequest request
            ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(doctorHospitalService.assignDoctorToHospital(request));
    }

    // Get all doctors working in a hospital
    @GetMapping("/hospitals/{hospitalId}/doctors")
    public ResponseEntity<List<DoctorResponse>> getDoctorsByHospital(
            @PathVariable UUID hospitalId) {
        return ResponseEntity.ok(
                doctorHospitalService.getDoctorsByHospital(hospitalId) );
    }

    // Get doctors in a hospital by department
    @GetMapping("/hospitals/{hospitalId}/doctors/search")
    public ResponseEntity<List<DoctorResponse>> searchDoctorByDepartment(
            @PathVariable UUID hospitalId,
            @RequestParam(required = false) String department
    ) {
        return ResponseEntity.ok(
                doctorHospitalService.searchDoctorsByDepartment( hospitalId, department ) );
    }

    // Get all hospitals where a doctor works
    @GetMapping("/doctors/{doctorId}/hospitals")
    public ResponseEntity<List<DoctorHospitalResponse>> getHospitalsByDoctor(
            @PathVariable UUID doctorId) {
        return ResponseEntity.ok(doctorHospitalService.getHospitalsByDoctor(doctorId));
    }

    // Get a particular doctor-hospital relationship
    @GetMapping("/{doctorHospitalId}")
    public ResponseEntity<DoctorHospitalResponse> getDoctorHospital(
            @PathVariable UUID doctorHospitalId) {
        return ResponseEntity.ok(
                doctorHospitalService.getDoctorHospitalById( doctorHospitalId ) );
    }

    // Update department, role, dates, etc.
    @PatchMapping("/{doctorHospitalId}")
    public ResponseEntity<DoctorHospitalResponse> updateDoctorHospital(
            @PathVariable UUID doctorHospitalId,
            @RequestBody @Valid DoctorHospitalUpdateRequest request) {
        return ResponseEntity.ok(
                doctorHospitalService.updateDoctorHospital( doctorHospitalId, request )
        );
    }

    // Remove doctor from hospital
    @DeleteMapping("/{doctorHospitalId}")
    public ResponseEntity<Void> removeDoctorFromHospital(
            @PathVariable UUID doctorHospitalId) {
        doctorHospitalService.removeDoctorFromHospital( doctorHospitalId );
        return ResponseEntity.noContent().build();
    }
}
