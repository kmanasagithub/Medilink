package com.medilink.backend.Patient.dto;

import com.medilink.backend.Patient.Enum.BloodGroup;
import com.medilink.backend.Patient.Enum.Gender;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PatientRequestDto {

    @NotNull(message = "User ID is required")
    private UUID userId;

    private LocalDate dateOfBirth;

    private Gender gender;

    private BloodGroup bloodGroup;

    private String allergies;

    private String emergencyContact;

    @Valid
    private Address address;
}
