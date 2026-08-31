package com.medilink.backend.DoctorHospital;

import com.medilink.backend.Doctor.DoctorEntity;
import com.medilink.backend.Doctor.DoctorRepository;
import com.medilink.backend.Doctor.Enum.DoctorStatus;
import com.medilink.backend.Doctor.dto.DoctorResponse;
import com.medilink.backend.DoctorHospital.Enum.DoctorHospitalStatus;
import com.medilink.backend.DoctorHospital.dto.DoctorHospitalRequest;
import com.medilink.backend.DoctorHospital.dto.DoctorHospitalResponse;
import com.medilink.backend.DoctorHospital.dto.DoctorHospitalUpdateRequest;
import com.medilink.backend.Hospital.Enum.HospitalStatus;
import com.medilink.backend.Hospital.HospitalEntity;
import com.medilink.backend.Hospital.HospitalRepository;
import com.medilink.backend.Hospital.dto.HospitalResponse;
import com.medilink.backend.User.UserEntity;
import com.medilink.backend.User.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class DoctorHospitalServiceImpl implements DoctorHospitalService{

    private final DoctorHospitalRepository doctorHospitalRepository;
    private final DoctorRepository doctorRepository;
    private final HospitalRepository hospitalRepository;

    // ASSIGN DOCTOR TO HOSPITAL
    @Override
    public DoctorHospitalResponse assignDoctorToHospital(DoctorHospitalRequest request) {
        DoctorEntity doctor = doctorRepository.findById(request.getDoctorId())
                .orElseThrow(() -> new RuntimeException(""));

        HospitalEntity hospital = hospitalRepository.findById(request.getHospitalId())
                .orElseThrow(() -> new RuntimeException(""));

        // Doctor must be active
        if(doctor.getDoctorStatus() != DoctorStatus.ACTIVE) {
            throw new RuntimeException(
                    "Doctor must be active before being assigned to a hospital"
            );
        }

        if(doctorHospitalRepository.existsByDoctorIdAndHospitalId(
                doctor.getId(),hospital.getId())){
            throw new RuntimeException(
                    "Doctor is already assigned to this hospital"
            );
        }

        DoctorHospitalEntity saved = DoctorHospitalEntity.builder()
                .doctor(doctor)
                .hospital(hospital)
                .department(request.getDepartment())
                .role(request.getRole())
                .status(DoctorHospitalStatus.ACTIVE)
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .build();

        saved = doctorHospitalRepository.save(saved);
        return convertToResponse(saved);
    }


    // GET ALL DOCTORS IN A HOSPITAL
    @Override
    @Transactional(readOnly = true)
    public List<DoctorResponse> getDoctorsByHospital(UUID hospitalId) {
        // Make sure hospital exists
        hospitalRepository.findById(hospitalId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Hospital not found with ID: " + hospitalId
                        )
                );

        return doctorHospitalRepository
                .findByHospitalIdAndStatus(
                        hospitalId,
                        DoctorHospitalStatus.ACTIVE
                )
                .stream()
                // Get the DoctorEntity
                .map(DoctorHospitalEntity::getDoctor)
                // Only active doctors
                .filter(doctor ->
                        doctor.getDoctorStatus() == DoctorStatus.ACTIVE
                )
                // Convert DoctorEntity -> DoctorResponse
                .map(this::convertToDoctorResponse)
                .toList();
    }


    @Override
    public List<DoctorResponse> searchDoctorsByDepartment(UUID hospitalId, String department) {
        // Make sure hospital exists
        hospitalRepository.findById(hospitalId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Hospital not found with ID: " + hospitalId
                        )
                );

        List<DoctorHospitalEntity> relationships;

        if (department == null || department.isBlank()) {

            relationships =
                    doctorHospitalRepository.findByHospitalIdAndStatus(
                            hospitalId,
                            DoctorHospitalStatus.ACTIVE
                    );

        } else {

            relationships =
                    doctorHospitalRepository
                            .findByHospitalIdAndDepartmentIgnoreCase(
                                    hospitalId,
                                    department
                            )
                            .stream()
                            .filter(relationship ->
                                    relationship.getStatus()
                                            == DoctorHospitalStatus.ACTIVE
                            )
                            .toList();
        }

        return relationships.stream()
                .map(DoctorHospitalEntity::getDoctor)
                .filter(doctor ->
                        doctor.getDoctorStatus() == DoctorStatus.ACTIVE
                )
                .map(this::convertToDoctorResponse)
                .toList();
    }

    // GET ALL HOSPITALS WHERE DOCTOR WORKS
    @Override
    @Transactional(readOnly = true)
    public List<DoctorHospitalResponse> getHospitalsByDoctor(UUID doctorId) {
        // Make sure doctor exists
        doctorRepository.findById(doctorId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Doctor not found with ID: " + doctorId
                        )
                );

        return doctorHospitalRepository
                .findByDoctorIdAndStatus(
                        doctorId,
                        DoctorHospitalStatus.ACTIVE
                )
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    // GET ONE DOCTOR-HOSPITAL RELATIONSHIP
    @Override
    public DoctorHospitalResponse getDoctorHospitalById(UUID doctorHospitalId) {
        DoctorHospitalEntity doctorHospital =
                doctorHospitalRepository.findById(doctorHospitalId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Doctor-hospital relationship not found"
                                )
                        );

        return convertToResponse(doctorHospital);
    }

    @Override
    public void removeDoctorFromHospital(UUID doctorHospitalId) {
        DoctorHospitalEntity doctorHospital =
                doctorHospitalRepository.findById(doctorHospitalId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Doctor-hospital relationship not found"
                                )
                        );

        if (doctorHospital.getStatus()
                == DoctorHospitalStatus.INACTIVE) {

            throw new RuntimeException(
                    "Doctor is already removed from this hospital"
            );
        }

        doctorHospital.setStatus(
                DoctorHospitalStatus.INACTIVE
        );

        doctorHospital.setEndDate(
                java.time.LocalDate.now()
        );
    }

    // UPDATE DOCTOR-HOSPITAL RELATIONSHIP
    @Override
    @Transactional
    public DoctorHospitalResponse updateDoctorHospital(UUID doctorHospitalId, DoctorHospitalUpdateRequest request) {
        DoctorHospitalEntity doctorHospital =
                doctorHospitalRepository.findById(doctorHospitalId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Doctor-hospital relationship not found"
                                )
                        );

        if (request.getDepartment() != null) {
            doctorHospital.setDepartment(
                    request.getDepartment()
            );
        }

        if (request.getRole() != null) {
            doctorHospital.setRole(
                    request.getRole()
            );
        }

        if (request.getStatus() != null) {
            doctorHospital.setStatus(
                    request.getStatus()
            );
        }

        if (request.getStartDate() != null) {
            doctorHospital.setStartDate(
                    request.getStartDate()
            );
        }

        if (request.getEndDate() != null) {
            doctorHospital.setEndDate(
                    request.getEndDate()
            );
        }

        return convertToResponse(doctorHospital);
    }

    // ------------------------------------------------------------------------
    // MAPPERS
    // ------------------------------------------------------------------------
    private DoctorHospitalResponse convertToResponse(DoctorHospitalEntity entity) {
        DoctorHospitalResponse response = DoctorHospitalResponse.builder()
                .doctor(convertToDoctorResponse(entity.getDoctor()))
                .hospital(convertToHospitalResponse(entity.getHospital()))
                .department(entity.getDepartment())
                .role(entity.getRole())
                .startDate(entity.getStartDate())
                .endDate(entity.getEndDate())
                .status(entity.getStatus())
                .build();
        return response;
    }

    private HospitalResponse convertToHospitalResponse(HospitalEntity hospital) {
        return HospitalResponse.builder()
                .id(hospital.getId())
                .name(hospital.getName())
                .address(hospital.getAddress())
                .city(hospital.getCity())
                .state(hospital.getState())
                .country(hospital.getCountry())
                .phone(hospital.getPhone())
                .website(hospital.getWebsite())
                .status(HospitalStatus.ACTIVE)
                .build();
    }

    private DoctorResponse convertToDoctorResponse(
            DoctorEntity doctor) {

        UserEntity user = doctor.getUser();

        UserResponse userResponse =
                UserResponse.builder()
                        .id(user.getId())
                        .name(user.getName())
                        .email(user.getEmail())
                        .phoneNumber(user.getPhoneNumber())
                        .role(user.getRole())
                        .build();

        return DoctorResponse.builder()
                .id(doctor.getId())
                .user(userResponse)
                .licenseNumber(doctor.getLicenseNumber())
                .qualification(doctor.getQualification())
                .specialization(doctor.getSpecialization())
                .experienceYears(doctor.getExperienceYears())
                .bio(doctor.getBio())
                .consultationFee(doctor.getConsultationFee())
                .verificationStatus(
                        doctor.getVerificationStatus()
                )
                .doctorStatus(
                        doctor.getDoctorStatus()
                )
                .build();
    }

}
