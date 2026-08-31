package com.medilink.backend.Patient.dto;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Address {
    private String houseNumber;
    private String street;
    private String city;
    private String pincode;
    private String state;
    private String country;
}
