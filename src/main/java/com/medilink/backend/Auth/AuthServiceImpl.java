package com.medilink.backend.Auth;

import com.medilink.backend.Auth.dto.*;
import com.medilink.backend.Doctor.DoctorEntity;
import com.medilink.backend.Doctor.DoctorRepository;
import com.medilink.backend.Doctor.Enum.DoctorStatus;
import com.medilink.backend.Doctor.Enum.VerificationStatus;
import com.medilink.backend.Hospital.Enum.HospitalStatus;
import com.medilink.backend.Hospital.HospitalEntity;
import com.medilink.backend.Hospital.HospitalRepository;
import com.medilink.backend.Patient.Enum.PatientStatus;
import com.medilink.backend.Patient.PatientEntity;
import com.medilink.backend.Patient.PatientRepository;
import com.medilink.backend.Security.CustomerDetailService;
import com.medilink.backend.Security.JwtService;
import com.medilink.backend.User.Enum.Role;
import com.medilink.backend.User.UserEntity;
import com.medilink.backend.User.UserRepository;
import com.medilink.backend.User.dto.UserResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{

    private final UserRepository userRepository;
    private final DoctorRepository doctorRepository;
    private final PasswordEncoder passwordEncoder;
    private final HospitalRepository hospitalRepository;
    private final AuthenticationManager authenticationManager;
    private final PatientRepository patientRepository;
    private final JwtService jwtService;

    @Override
    @Transactional
    public UserResponse signupPatient(PatientSignupRequest request) {

        validateEmail(request.getEmail());

        UserEntity user = UserEntity.builder()
                .name(request.getName())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .role(Role.PATIENT)
                .enabled(true)
                .build();

        UserEntity savedUser = userRepository.save(user);

        PatientEntity patient = PatientEntity.builder()
                .user(savedUser)
                .dateOfBirth(request.getDateOfBirth())
                .gender(request.getGender())
                .bloodGroup(request.getBloodGroup())
                .allergies(request.getAllergies())
                .emergencyContact(request.getEmergencyContact())
                .address(request.getAddress())
                .patientStatus(PatientStatus.ACTIVE)
                .build();
        patient = patientRepository.save(patient);
        return convertToResponse(savedUser);
    }

    @Override
    @Transactional
    public UserResponse signupDoctor(DoctorSignupRequest request) {

        validateEmail(request.getEmail());

        UserEntity user = UserEntity.builder()
                .name(request.getName())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .passwordHash(
                        passwordEncoder.encode(request.getPassword())
                )
                .role(Role.DOCTOR)
                .enabled(true)
                .build();

        UserEntity savedUser = userRepository.save(user);

        DoctorEntity doctor = DoctorEntity.builder()
                .user(savedUser)
                .licenseNumber(request.getLicenseNumber())
                .qualification(request.getQualification())
                .specialization(request.getSpecialization())
                .experienceYears(request.getExperienceYears())
                .bio(request.getBio())
                .verificationStatus(VerificationStatus.PENDING)
                .doctorStatus(DoctorStatus.INACTIVE)
                .build();

        doctorRepository.save(doctor);

        return convertToResponse(savedUser);
    }

    @Override
    @Transactional
    public UserResponse signupHospital(HospitalSignupRequest request) {

        validateEmail(request.getEmail());

        UserEntity user = UserEntity.builder()
                .name(request.getAdminName())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .passwordHash(
                        passwordEncoder.encode(request.getPassword())
                )
                .role(Role.HOSPITAL)
                .enabled(true)
                .build();

        UserEntity savedUser = userRepository.save(user);

        HospitalEntity hospital = HospitalEntity.builder()
                .user(user)
                .hospitalName(request.getHospitalName())
                .address(request.getAddress())
                .city(request.getCity())
                .state(request.getState())
                .country(request.getCountry())
                .website(request.getWebsite())
                .status(HospitalStatus.INACTIVE)
                .build();

        hospitalRepository.save(hospital);

        return convertToResponse(savedUser);
    }

    private void validateEmail(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email already registered");
        }
    }


    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(),loginRequest.getPassword())
        );

        UserEntity userDetails = (UserEntity) authentication.getPrincipal();
        String accessToken = jwtService.generateAccessToken(userDetails);
        String refreshToken = jwtService.generateRefreshToken(userDetails);

        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .user(convertToResponse(userDetails))
                .build();

    }

    @Override
    public void logout() {

    }

    @Override
    public void changePassword(ChangePasswordRequest passwordRequest) {

    }

    @Override
    public LoginResponse refreshToken(RefreshTokenEntity request) {
        return null;
    }

    private UserResponse convertToResponse(UserEntity user) {
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .role(user.getRole())
                .build();
    }

}
