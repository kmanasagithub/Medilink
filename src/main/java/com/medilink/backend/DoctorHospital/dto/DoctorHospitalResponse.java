package com.medilink.backend.DoctorHospital.dto;

import com.medilink.backend.Doctor.dto.DoctorResponse;
import com.medilink.backend.DoctorHospital.Enum.DoctorHospitalStatus;
import com.medilink.backend.Hospital.dto.HospitalResponse;
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
public class DoctorHospitalResponse {

    private UUID id;

    private DoctorResponse doctor;

    private HospitalResponse hospital;

    private String department;
    private String role;

    private LocalDate startDate;
    private LocalDate endDate;

    private DoctorHospitalStatus status;
}