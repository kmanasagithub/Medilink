package com.medilink.backend.Doctor;

import com.medilink.backend.Doctor.Enum.DoctorStatus;
import com.medilink.backend.Doctor.Enum.VerificationStatus;
import com.medilink.backend.Doctor.dto.DoctorRequest;
import com.medilink.backend.Doctor.dto.DoctorResponse;
import com.medilink.backend.Doctor.dto.DoctorUpdateRequest;
import com.medilink.backend.User.Enum.Role;
import com.medilink.backend.User.UserEntity;
import com.medilink.backend.User.UserRepository;
import com.medilink.backend.User.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DoctorServiceImpl implements DoctorService{

    private final DoctorRepository doctorRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public DoctorResponse createDoctor(DoctorRequest request) {
        UserEntity user = userRepository.findById(request.getUserId()).
                orElseThrow(() -> new RuntimeException("User not found with ID: "
                        + request.getUserId()));

        if (user.getRole() != Role.DOCTOR) {
            throw new RuntimeException(
                    "User must have DOCTOR role to create a doctor profile"
            );
        }

        if (doctorRepository.existsByUserId(user.getId())) {
            throw new RuntimeException("Doctor profile already exists");
        }

        DoctorEntity doctor = DoctorEntity.builder()
                .user(user)
                .licenseNumber(request.getLicenseNumber())
                .qualification(request.getQualification())
                .specialization(request.getSpecialization())
                .experienceYears(request.getExperienceYears())
                .bio(request.getBio())
                .consultationFee(request.getConsultationFee())
                .verificationStatus(VerificationStatus.PENDING)
                .doctorStatus(DoctorStatus.INACTIVE)
                .build();

        DoctorEntity savedDoctor = doctorRepository.save(doctor);

        return convertToDoctorResponse(savedDoctor);
    }

    @Override
    public DoctorResponse getDoctorById(UUID doctorId) {
        DoctorEntity doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException(""));

        if (doctor.getDoctorStatus() == DoctorStatus.INACTIVE) {
            throw new RuntimeException("Doctor is inactive");
        }

        return convertToDoctorResponse(doctor);
    }

    @Override
    @Transactional
    public List<DoctorResponse> getAllDoctors() {
        return doctorRepository.findAll()
                .stream()
                .filter(doctor ->
                        doctor.getDoctorStatus() != DoctorStatus.INACTIVE
                )
                .map(this::convertToDoctorResponse)
                .toList();
    }

    @Override
    @Transactional
    public DoctorResponse getMyProfile() {
        UUID userId = getLoggedInUserId();

        DoctorEntity doctor = doctorRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Doctor profile not found"));
        return convertToDoctorResponse(doctor);

    }

    @Override
    @Transactional
    public DoctorResponse updateMyProfile(DoctorUpdateRequest request) {

        UUID userId = getLoggedInUserId();

        DoctorEntity doctor = doctorRepository.findByUserId(userId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Doctor profile not found"
                        )
                );

        if (doctor.getDoctorStatus() == DoctorStatus.INACTIVE) {
            throw new RuntimeException("Doctor is inactive");
        }

        if (request.getQualification() != null) {
            doctor.setQualification(request.getQualification());
        }

        if (request.getSpecialization() != null) {
            doctor.setSpecialization(request.getSpecialization());
        }

        if (request.getExperienceYears() != null) {
            doctor.setExperienceYears(request.getExperienceYears());
        }

        if (request.getBio() != null) {
            doctor.setBio(request.getBio());
        }

        if (request.getConsultationFee() != null) {
            doctor.setConsultationFee(request.getConsultationFee());
        }

        return convertToDoctorResponse(doctor);

    }

    @Override
    @Transactional
    public void deleteMyProfile() {
        UUID userId = getLoggedInUserId();

        DoctorEntity doctor = doctorRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Doctor profile not found"));

        if (doctor.getDoctorStatus() == DoctorStatus.INACTIVE) {
            throw new RuntimeException("Doctor is already inactive");
        }

        doctor.setDoctorStatus(DoctorStatus.INACTIVE);

    }

    @Override
    @Transactional
    public DoctorResponse activateMyProfile() {

        UUID userId = getLoggedInUserId();

        DoctorEntity doctor = doctorRepository.findByUserId(userId)
                .orElseThrow(() ->
                        new RuntimeException("Doctor profile not found")
                );

        if (doctor.getDoctorStatus() != DoctorStatus.INACTIVE) {
            throw new RuntimeException("Doctor is not inactive");
        }

        doctor.setDoctorStatus(DoctorStatus.ACTIVE);

        return convertToDoctorResponse(doctor);
    }

    private UUID getLoggedInUserId() {
        return UUID.fromString("mcejop");
    }

    private DoctorResponse convertToDoctorResponse(DoctorEntity doctor) {
        UserEntity user = doctor.getUser();

        UserResponse userResponse = UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .role(user.getRole())
                .build();

        DoctorResponse doctorResponse = DoctorResponse.builder()
                .id(doctor.getId())
                .user(userResponse)
                .licenseNumber(doctor.getLicenseNumber())
                .qualification(doctor.getQualification())
                .specialization(doctor.getSpecialization())
                .experienceYears(doctor.getExperienceYears())
                .bio(doctor.getBio())
                .consultationFee(doctor.getConsultationFee())
                .verificationStatus(doctor.getVerificationStatus())
                .doctorStatus(doctor.getDoctorStatus())
                .build();

        return doctorResponse;
    }

}
