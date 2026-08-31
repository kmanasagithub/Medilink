package com.medilink.backend.Doctor;

import com.medilink.backend.Doctor.dto.DoctorRequest;
import com.medilink.backend.Doctor.dto.DoctorResponse;
import com.medilink.backend.Doctor.dto.DoctorUpdateRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequestMapping("/api/v1.0/doctors")
@RestController
@RequiredArgsConstructor
public class DoctorController {

    private final DoctorService doctorService;

    @PostMapping
    public ResponseEntity<DoctorResponse> createDoctor(@RequestBody @Valid DoctorRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(doctorService.createDoctor(request));
    }

    // View/search all doctors
    @GetMapping
    public ResponseEntity<List<DoctorResponse>> getAllDoctors() {
        return ResponseEntity.ok(doctorService.getAllDoctors());
    }

    // Logged-in doctor's own profile
    @GetMapping("/me")
    public ResponseEntity<DoctorResponse> getMyProfile() {
        return ResponseEntity.ok(doctorService.getMyProfile());
    }

    @GetMapping("/{doctorId}")
    public ResponseEntity<DoctorResponse> getDoctorById(@PathVariable UUID doctorId) {
        return ResponseEntity.status(HttpStatus.OK).body(doctorService.getDoctorById(doctorId));
    }

    // Logged-in doctor updates own profile
    @PatchMapping("/me")
    public ResponseEntity<DoctorResponse> updateMyProfile(
            @RequestBody @Valid DoctorUpdateRequest request) {
        return ResponseEntity.ok(doctorService.updateMyProfile(request));
    }

    // Logged-in doctor reactivates own profile
    @PatchMapping("/me/activate")
    public ResponseEntity<DoctorResponse> activateMyProfile() {
        return ResponseEntity.ok(doctorService.activateMyProfile());
    }

    // Logged-in doctor deactivates own profile
    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteMyProfile() {
        doctorService.deleteMyProfile();
        return ResponseEntity.noContent().build();
    }
}
