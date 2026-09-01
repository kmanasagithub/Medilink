package com.medilink.backend.Patient;

import com.medilink.backend.Patient.Enum.PatientStatus;
import com.medilink.backend.Patient.dto.PatientResponseDto;
import com.medilink.backend.Patient.dto.PatientUpdateRequest;
import com.medilink.backend.User.UserEntity;
import com.medilink.backend.User.UserRepository;
import com.medilink.backend.User.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService{

    private final PatientRepository patientRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public List<PatientResponseDto> getAllPatients() {
        return patientRepository.findAll()
                .stream().filter(patient ->
                patient.getPatientStatus() == PatientStatus.ACTIVE
        )
                .map(patient -> convertToPatientResponseDto(patient))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public PatientResponseDto getPatientById(UUID patientId) {

        PatientEntity patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found with ID: "+patientId));

        if (patient.getPatientStatus() == PatientStatus.INACTIVE) {
            throw new RuntimeException("Patient is inactive");
        }

        return convertToPatientResponseDto(patient);
    }

    @Override
    @Transactional(readOnly = true)
    public PatientResponseDto getMyProfile() {

        UUID userId = getLoggedInUserId();

        PatientEntity patient = patientRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Patient profile not found")
                );

        if (patient.getPatientStatus() == PatientStatus.INACTIVE) {
            throw new RuntimeException("Patient profile is inactive");
        }

        return convertToPatientResponseDto(patient);
    }

    @Override
    @Transactional
    public PatientResponseDto updateMyProfile(
            PatientUpdateRequest request
    ) {

        UUID userId = getLoggedInUserId();

        PatientEntity patient = patientRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Patient profile not found")
                );

        if (patient.getPatientStatus() == PatientStatus.INACTIVE) {
            throw new RuntimeException("Patient is inactive, cannot be updated");
        }
        if (request.getDateOfBirth() != null) {
            patient.setDateOfBirth(request.getDateOfBirth());
        }
        if (request.getGender() != null) {
            patient.setGender(request.getGender());
        }
        if (request.getBloodGroup() != null) {
            patient.setBloodGroup(request.getBloodGroup());
        }
        if (request.getAllergies() != null) {
            patient.setAllergies(request.getAllergies());
        }
        if (request.getEmergencyContact() != null) {
            patient.setEmergencyContact(request.getEmergencyContact());
        }
        if (request.getAddress() != null) {
            patient.setAddress(request.getAddress());
        }
        return convertToPatientResponseDto(patient);
    }

    @Override
    @Transactional
    public void deactivateMyProfile() {

        UUID userId = getLoggedInUserId();

        PatientEntity patient = patientRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Patient profile not found"));

        if (patient.getPatientStatus() == PatientStatus.INACTIVE) {
            throw new RuntimeException("Patient is already inactive");
        }

        patient.setPatientStatus(PatientStatus.INACTIVE);
    }

    private UUID getLoggedInUserId() {

//        Authentication authentication = SecurityContextHolder
//                        .getContext()
//                        .getAuthentication();
//
//        if (authentication == null ||
//                !authentication.isAuthenticated()) {
//
//            throw new RuntimeException(
//                    "User is not authenticated"
//            );
//        }

        return UUID.fromString("jfoj");
    }

    private PatientResponseDto convertToPatientResponseDto(PatientEntity patient) {
            UserEntity user = patient.getUser();

        UserResponse userResponse = UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .role(user.getRole())
                .build();

            return PatientResponseDto.builder()
                    .id(patient.getId())
                    .user(userResponse)
                    .dateOfBirth(patient.getDateOfBirth())
                    .gender(patient.getGender())
                    .bloodGroup(patient.getBloodGroup())
                    .allergies(patient.getAllergies())
                    .emergencyContact(patient.getEmergencyContact())
                    .address(patient.getAddress())
                    .patientStatus(patient.getPatientStatus())
                    .build();

    }
}
