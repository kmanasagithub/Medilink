package com.medilink.backend.Auth;

import com.medilink.backend.Auth.dto.*;
import com.medilink.backend.User.dto.UserResponse;
import jakarta.validation.Valid;

public interface AuthService {

    void changePassword(@Valid ChangePasswordRequest passwordRequest);

    void logout();

    UserResponse signupPatient(@Valid PatientSignupRequest signupRequest);

    LoginResponse login(@Valid LoginRequest loginRequest);

    LoginResponse refreshToken(@Valid RefreshTokenEntity request);

    UserResponse signupHospital(@Valid HospitalSignupRequest signupRequest);

    UserResponse signupDoctor(@Valid DoctorSignupRequest signupRequest);
}
