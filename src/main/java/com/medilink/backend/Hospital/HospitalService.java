package com.medilink.backend.Hospital;

import com.medilink.backend.Hospital.dto.HospitalResponse;
import com.medilink.backend.Hospital.dto.HospitalUpdateRequest;
import jakarta.validation.Valid;

import java.util.List;
import java.util.UUID;

public interface HospitalService {

    HospitalResponse getMyProfile();

    HospitalResponse updateMyProfile(@Valid HospitalUpdateRequest request);

    void deactivateMyProfile();

    HospitalResponse getHospitalById(UUID hospitalId);

    List<HospitalResponse> getHospitalByParams(String name,
                                               String city,
                                               String state,
                                               String country);
}
