package com.medilink.backend.Patient;

import com.medilink.backend.Patient.Enum.PatientStatus;
import com.medilink.backend.Patient.dto.PatientRequestDto;
import com.medilink.backend.Patient.dto.PatientResponseDto;
import com.medilink.backend.Patient.dto.PatientUpdateRequest;
import com.medilink.backend.User.Enum.Role;
import com.medilink.backend.User.UserEntity;
import com.medilink.backend.User.UserRepository;
import com.medilink.backend.User.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService{

    private final PatientRepository patientRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public PatientResponseDto createPatient(PatientRequestDto request) {
        UserEntity user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException(""));

        if (user.getRole() != Role.PATIENT) {
            throw new RuntimeException(
                    "User must have PATIENT role to create a patient profile"
            );
        }

        if (patientRepository.existsByUserId(user.getId())) {
            throw new RuntimeException("Patient profile already exists");
        }

        PatientEntity patient = PatientEntity.builder()
                .user(user)
                .dateOfBirth(request.getDateOfBirth())
                .gender(request.getGender())
                .bloodGroup(request.getBloodGroup())
                .allergies(request.getAllergies())
                .emergencyContact(request.getEmergencyContact())
                .address(request.getAddress())
                .patientStatus(PatientStatus.ACTIVE)
                .build();

        patient = patientRepository.save(patient);

        return convertToPatientResponseDto(patient);
    }

    @Override
    public List<PatientResponseDto> getAllPatients() {
        return patientRepository.findAll()
                .stream()
                .map(patient -> convertToPatientResponseDto(patient))
                .toList();
    }

    @Override
    public PatientResponseDto getPatientById(UUID patientId) {

        PatientEntity patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found with ID: "+patientId));

        if (patient.getPatientStatus() == PatientStatus.INACTIVE) {
            throw new RuntimeException("Patient is inactive");
        }

        return convertToPatientResponseDto(patient);
    }

    @Override
    @Transactional
    public PatientResponseDto updatePatientById(PatientUpdateRequest request, UUID patientId) {
        PatientEntity patient = patientRepository.findById(patientId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Patient not found with ID: " + patientId
                        )
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
    public PatientResponseDto activatePatient(UUID patientId) {
        PatientEntity patient = patientRepository.findById(patientId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Patient not found with ID: " + patientId
                        )
                );

        if (patient.getPatientStatus() == PatientStatus.ACTIVE) {
            throw new RuntimeException("Patient is already active");
        }

        patient.setPatientStatus(PatientStatus.ACTIVE);

        return convertToPatientResponseDto(patient);
    }

    @Override
    public PatientResponseDto getPatientByUserId(UUID userId) {
            PatientEntity patient = patientRepository.findByUserId(userId)
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "Patient profile not found for user ID: " + userId
                            )
                    );

            if (patient.getPatientStatus() == PatientStatus.INACTIVE) {
                throw new RuntimeException("Patient is inactive");
            }

            return convertToPatientResponseDto(patient);
    }

    @Override
    @Transactional
    public void deletePatientById(UUID patientId) {
        PatientEntity patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException(""));

        if (patient.getPatientStatus() == PatientStatus.INACTIVE) {
            throw new RuntimeException("Patient is already inactive");
        }

        patient.setPatientStatus(PatientStatus.INACTIVE);
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
