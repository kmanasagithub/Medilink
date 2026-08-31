package com.medilink.backend.DoctorHospital.dto;

import com.medilink.backend.DoctorHospital.Enum.DoctorHospitalStatus;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DoctorHospitalUpdateRequest {
    @Size(max = 100)
    private String department;

    @Size(max = 100)
    private String role;

    private LocalDate startDate;

    private LocalDate endDate;

    private DoctorHospitalStatus status;
}
