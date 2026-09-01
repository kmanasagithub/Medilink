package com.medilink.backend.Auth.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DoctorSignupRequest {

    @NotBlank
    private String name;

    @NotBlank
    @Email
    private String email;

    private String phoneNumber;

    @NotBlank
    @Size(min = 8, max = 100)
    private String password;

    @NotBlank
    private String licenseNumber;

    @NotBlank
    private String qualification;

    @NotBlank
    private String specialization;

    @NotNull
    @Min(0)
    private Integer experienceYears;

    private String bio;
}
