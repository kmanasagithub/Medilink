package com.medilink.backend.Hospital.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HospitalUpdateRequest {

    private String name;

    private String address;

    private String city;

    private String state;

    private String country;

    private String phone;

    private String website;
}
