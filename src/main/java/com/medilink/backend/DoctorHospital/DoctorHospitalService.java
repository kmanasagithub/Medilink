package com.medilink.backend.DoctorHospital;

import com.medilink.backend.Doctor.dto.DoctorResponse;
import com.medilink.backend.DoctorHospital.dto.DoctorHospitalRequest;
import com.medilink.backend.DoctorHospital.dto.DoctorHospitalResponse;
import com.medilink.backend.DoctorHospital.dto.DoctorHospitalUpdateRequest;
import jakarta.validation.Valid;

import java.util.List;
import java.util.UUID;

public interface DoctorHospitalService {

    DoctorHospitalResponse assignDoctorToHospital(@Valid DoctorHospitalRequest request);

    List<DoctorResponse> getDoctorsByHospital(UUID hospitalId);

    List<DoctorResponse> searchDoctorsByDepartment(UUID hospitalId, String department);

    List<DoctorHospitalResponse> getHospitalsByDoctor(UUID doctorId);

    DoctorHospitalResponse getDoctorHospitalById(UUID doctorHospitalId);

    void removeDoctorFromHospital(UUID doctorHospitalId);

    DoctorHospitalResponse updateDoctorHospital(UUID doctorHospitalId, @Valid DoctorHospitalUpdateRequest request);
}
