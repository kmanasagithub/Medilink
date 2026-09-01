package com.medilink.backend.Hospital.dto;

import com.medilink.backend.Hospital.Enum.HospitalStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HospitalResponse {

    private UUID id;

    private String hosiptalName;

    private String address;

    private String city;

    private String state;

    private String country;

    private String phone;

    private String website;

    private HospitalStatus status;
}
