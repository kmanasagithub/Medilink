package com.medilink.backend.Patient;

import com.medilink.backend.Patient.dto.PatientRequestDto;
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
@RequestMapping("/api/v1.0/patients")
public class PatientController {

    private final PatientService patientService;

    @PostMapping
    public ResponseEntity<PatientResponseDto> createPatient(@RequestBody @Valid PatientRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(patientService.createPatient(request));
    }

    @GetMapping
    public ResponseEntity<List<PatientResponseDto>> getAllPatients() {
        return ResponseEntity.ok(patientService.getAllPatients());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<PatientResponseDto> getPatientByUserId(
            @PathVariable UUID userId) {

        return ResponseEntity.ok(
                patientService.getPatientByUserId(userId)
        );
    }

    @GetMapping("/{patientId}")
    public ResponseEntity<PatientResponseDto> getPatient(@PathVariable UUID patientId) {
        return ResponseEntity.status(HttpStatus.OK).body(patientService.getPatientById(patientId));
    }

    @PatchMapping("/{patientId}")
    public ResponseEntity<PatientResponseDto> updatePatient(@RequestBody @Valid PatientUpdateRequest request,
                                                            @PathVariable UUID patientId) {
        return ResponseEntity.status(HttpStatus.OK).body(patientService.updatePatientById(request,patientId));
    }

    @DeleteMapping("/{patientId}")
    public ResponseEntity<Void> deletePatient(@PathVariable UUID patientId) {
        patientService.deletePatientById(patientId);
        return ResponseEntity.noContent().build();

    }

//   InACTIVE to Activate user
    @PatchMapping("/{patientId}/activate")
    public ResponseEntity<PatientResponseDto> activatePatient(@PathVariable UUID patientId) {
        return ResponseEntity.status(HttpStatus.OK).body(patientService.activatePatient(patientId));
    }

}
