package com.medilink.backend.User;

import com.medilink.backend.User.dto.UserRequest;
import com.medilink.backend.User.dto.UserResponse;
import com.medilink.backend.User.dto.UserUpdateRequest;

import java.util.List;
import java.util.UUID;

public interface UserService {

    UserResponse createUser(UserRequest request);

    UserResponse getUser(UUID userId);

    List<UserResponse> getAllUsers();

    UserResponse updateUser(UserUpdateRequest request, UUID userId);
}
