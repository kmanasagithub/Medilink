package com.medilink.backend.Patient;

import com.medilink.backend.Patient.dto.PatientRequestDto;
import com.medilink.backend.Patient.dto.PatientResponseDto;
import com.medilink.backend.Patient.dto.PatientUpdateRequest;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PatientService {
    PatientResponseDto createPatient(@Valid PatientRequestDto request);

    List<PatientResponseDto> getAllPatients();

    PatientResponseDto getPatientById(UUID patientId);

    void deletePatientById(UUID patientId);

    PatientResponseDto updatePatientById(@Valid PatientUpdateRequest request, UUID patientId);

    PatientResponseDto activatePatient(UUID patientId);

    PatientResponseDto getPatientByUserId(UUID userId);
}
