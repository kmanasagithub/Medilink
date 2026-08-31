package com.medilink.backend.Hospital;

import com.medilink.backend.Doctor.dto.DoctorResponse;
import com.medilink.backend.Hospital.dto.HospitalRequest;
import com.medilink.backend.Hospital.dto.HospitalResponse;
import com.medilink.backend.Hospital.dto.HospitalUpdateRequest;
import jakarta.validation.Valid;

import java.util.List;
import java.util.UUID;

public interface HospitalService {

    HospitalResponse createHospital(@Valid HospitalRequest request);

    HospitalResponse getHospitalById(UUID hospitalId);

    List<HospitalResponse> getHospitalByParams(String name,
                                               String city,
                                               String state,
                                               String country);

    void deleteHospitalById(UUID hospitalId);

    HospitalResponse updateHospital(UUID hospitalId, @Valid HospitalUpdateRequest request);

    HospitalResponse activateHospital(UUID hospitalId);
}
