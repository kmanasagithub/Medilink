package com.medilink.backend.User;

import com.medilink.backend.User.dto.UserRequest;
import com.medilink.backend.User.dto.UserResponse;
import com.medilink.backend.User.dto.UserUpdateRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    private ResponseEntity<UserResponse> getMyProfile() {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getMyProfile());
    }

    @PatchMapping("/me")
    private ResponseEntity<UserResponse> updateUser(@RequestBody @Valid UserUpdateRequest userRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.updateMyProfile(userRequest));
    }
}
