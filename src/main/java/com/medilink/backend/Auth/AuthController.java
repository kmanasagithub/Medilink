package com.medilink.backend.Auth;

import com.medilink.backend.Auth.dto.*;
import com.medilink.backend.User.dto.UserResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup/patient")
    public ResponseEntity<UserResponse> signupPatient(@RequestBody @Valid PatientSignupRequest signupRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.signupPatient(signupRequest));
    }

    @PostMapping("/signup/hospital")
    public ResponseEntity<UserResponse> signupHospital(@RequestBody @Valid HospitalSignupRequest signupRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.signupHospital(signupRequest));
    }

    @PostMapping("/signup/doctor")
    public ResponseEntity<UserResponse> signupDoctor(@RequestBody @Valid DoctorSignupRequest signupRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.signupDoctor(signupRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.login(loginRequest));
    }

    @PatchMapping("/change-password")
    public ResponseEntity<Void> changePassword(@RequestBody @Valid ChangePasswordRequest passwordRequest) {
        authService.changePassword(passwordRequest);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(){
        authService.logout();
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<LoginResponse> refreshToken(@RequestBody @Valid RefreshTokenEntity request) {
        return ResponseEntity.ok(authService.refreshToken(request));
    }
}
