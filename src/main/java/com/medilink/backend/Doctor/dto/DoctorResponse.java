package com.medilink.backend.Doctor.dto;

import com.medilink.backend.Doctor.Enum.VerificationStatus;
import com.medilink.backend.Doctor.Enum.DoctorStatus;
import com.medilink.backend.User.dto.UserResponse;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DoctorResponse {

    private UUID id;

    private UserResponse user;

    private String licenseNumber;

    private String qualification;

    private String specialization;

    private Integer experienceYears;

    private String bio;

    private BigDecimal consultationFee;

    private VerificationStatus verificationStatus;

    private DoctorStatus doctorStatus;
}
