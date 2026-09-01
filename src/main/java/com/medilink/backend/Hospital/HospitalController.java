package com.medilink.backend.Hospital;

import com.medilink.backend.Hospital.dto.HospitalResponse;
import com.medilink.backend.Hospital.dto.HospitalUpdateRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/hospitals")
public class HospitalController {

    private final HospitalService hospitalService;

    // Logged-in hospital
    @GetMapping("/me")
    public ResponseEntity<HospitalResponse> getMyProfile() {
        return ResponseEntity.ok(hospitalService.getMyProfile());
    }

    // Logged-in hospital updates own profile
    @PatchMapping("/me")
    public ResponseEntity<HospitalResponse> updateMyProfile(@RequestBody @Valid HospitalUpdateRequest request) {
        return ResponseEntity.ok(hospitalService.updateMyProfile(request));
    }

    // Logged-in hospital deactivates own profile
    @DeleteMapping("/me/deactivate")
    public ResponseEntity<Void> deactivateMyProfile() {
        hospitalService.deactivateMyProfile();
        return ResponseEntity.noContent().build();
    }

    // Public hospital lookup
    @GetMapping("/{hospitalId}")
    public ResponseEntity<HospitalResponse> getHospitalById(@PathVariable UUID hospitalId) {
        return ResponseEntity.ok(hospitalService.getHospitalById(hospitalId));
    }

    // Public hospital search
    @GetMapping
    public ResponseEntity<List<HospitalResponse>> searchHospitals(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String state,
            @RequestParam(required = false) String country
    ) {
        return ResponseEntity.ok(
                hospitalService.getHospitalByParams(
                        name,
                        city,
                        state,
                        country
                )
        );
    }
}
