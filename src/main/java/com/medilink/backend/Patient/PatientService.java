package com.medilink.backend.Patient;

import com.medilink.backend.Patient.dto.PatientResponseDto;
import com.medilink.backend.Patient.dto.PatientUpdateRequest;

import java.util.List;
import java.util.UUID;

public interface PatientService {

    List<PatientResponseDto> getAllPatients();

    PatientResponseDto getPatientById(UUID patientId);

    PatientResponseDto getMyProfile();

    PatientResponseDto updateMyProfile(
            PatientUpdateRequest request
    );

    void deactivateMyProfile();

}
