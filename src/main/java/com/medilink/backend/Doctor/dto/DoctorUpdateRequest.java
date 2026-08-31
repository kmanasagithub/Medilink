package com.medilink.backend.Doctor.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DoctorUpdateRequest {

    private String licenseNumber;

    private String qualification;

    private String specialization;

    private Integer experienceYears;

    private String bio;

    private BigDecimal consultationFee;

}
