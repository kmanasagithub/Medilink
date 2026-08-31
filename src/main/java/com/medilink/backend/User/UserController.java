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
@RequestMapping("/api/v1.0/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    private ResponseEntity<UserResponse> createUser(@RequestBody @Valid UserRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(request));
    }

    @GetMapping("/{userId}")
    private ResponseEntity<UserResponse> getUser(@PathVariable UUID userId) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getUser(userId));
    }

    @GetMapping
    private ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getAllUsers());
    }

    @PatchMapping("/{userId}")
    private ResponseEntity<UserResponse> updateUser(@RequestBody @Valid UserUpdateRequest userRequest,
                                                    @PathVariable UUID userId) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.updateUser(userRequest,userId));
    }
}
