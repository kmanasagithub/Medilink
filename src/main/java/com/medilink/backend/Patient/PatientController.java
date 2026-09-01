package com.medilink.backend.Patient;

import com.medilink.backend.Patient.dto.PatientResponseDto;
import com.medilink.backend.Patient.dto.PatientUpdateRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/patients")
public class PatientController {

    private final PatientService patientService;

    @GetMapping
    public ResponseEntity<List<PatientResponseDto>> getAllPatients() {
        return ResponseEntity.ok(patientService.getAllPatients());
    }

    @GetMapping("/{patientId}")
    public ResponseEntity<PatientResponseDto> getPatient(@PathVariable UUID patientId) {
        return ResponseEntity.status(HttpStatus.OK).body(patientService.getPatientById(patientId));
    }

    @GetMapping("/me")
    public ResponseEntity<PatientResponseDto> getMyProfile() {
        return ResponseEntity.ok(patientService.getMyProfile());
    }

    @PatchMapping("/me")
    public ResponseEntity<PatientResponseDto> updateMyProfile(
            @RequestBody @Valid PatientUpdateRequest request
    ) {
        return ResponseEntity.ok(patientService.updateMyProfile(request));
    }

    @DeleteMapping("/me/deactivate")
    public ResponseEntity<Void> deactivateMyProfile() {
        patientService.deactivateMyProfile();
        return ResponseEntity.noContent().build();
    }


}
