package com.medilink.backend.Patient.dto;


import com.medilink.backend.Patient.Enum.BloodGroup;
import com.medilink.backend.Patient.Enum.Gender;
import com.medilink.backend.Patient.Enum.PatientStatus;
import com.medilink.backend.User.dto.UserResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PatientResponseDto {

    private UUID id;

    // User information
    private UserResponse user;

    private LocalDate dateOfBirth;

    private Gender gender;

    private BloodGroup bloodGroup;

    private String allergies;

    private String emergencyContact;

    private Address address;

    private PatientStatus patientStatus;
}
