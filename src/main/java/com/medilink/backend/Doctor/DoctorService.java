package com.medilink.backend.Doctor;

import com.medilink.backend.Doctor.dto.DoctorRequest;
import com.medilink.backend.Doctor.dto.DoctorResponse;
import com.medilink.backend.Doctor.dto.DoctorUpdateRequest;
import jakarta.validation.Valid;

import java.util.List;
import java.util.UUID;

public interface DoctorService {

    DoctorResponse getDoctorById(UUID doctorId);

    List<DoctorResponse> getAllDoctors();

    DoctorResponse getMyProfile();

    DoctorResponse updateMyProfile(@Valid DoctorUpdateRequest request);

    void deleteMyProfile();

}
