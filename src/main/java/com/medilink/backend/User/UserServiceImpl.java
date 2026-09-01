package com.medilink.backend.User;

import com.medilink.backend.User.dto.UserRequest;
import com.medilink.backend.User.dto.UserResponse;
import com.medilink.backend.User.dto.UserUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@RequestMapping
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public UserResponse getMyProfile() {
        UserEntity user = getLoggedInUser();
        return convertToResponse(user);
    }

    @Override
    @Transactional
    public UserResponse updateMyProfile(UserUpdateRequest request) {

        UserEntity user = getLoggedInUser();

        if (request.getName() != null) {
            user.setName(request.getName());
        }
        if (request.getPhoneNumber() != null) {
            user.setPhoneNumber(request.getPhoneNumber());
        }

        return convertToResponse(user);
    }

    private UserEntity getLoggedInUser() {
        return new UserEntity();
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
