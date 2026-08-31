package com.medilink.backend.User;

import com.medilink.backend.User.dto.UserRequest;
import com.medilink.backend.User.dto.UserResponse;
import com.medilink.backend.User.dto.UserUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
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
    private final ModelMapper modelMapper;

    @Override
    public UserResponse createUser(UserRequest request) {
        UserEntity newUser = UserEntity.builder()
                .email(request.getEmail())
                .name(request.getName())
                .phoneNumber(request.getPhoneNumber())
                .role(request.getRole())
                .build();

        UserEntity savedUser = userRepository.save(newUser);

        return modelMapper.map(savedUser,UserResponse.class);
    }

    @Override
    public UserResponse getUser(UUID userId) {
        UserEntity existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not with ID: "+userId));

        return modelMapper.map(existingUser,UserResponse.class);
    }

    @Override
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(user -> modelMapper.map(user,UserResponse.class))
                .toList();
    }

    @Override
    @Transactional
    public UserResponse updateUser(UserUpdateRequest request, UUID userId) {
        UserEntity existingUser = userRepository.findById(userId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "User not found with ID: " + userId
                        )
                );

        if(request.getName() != null) {
            existingUser.setName(request.getName());
        }
        if(request.getEmail() != null) {
            existingUser.setEmail(request.getEmail());
        }
        if(request.getPhoneNumber() != null) {
            existingUser.setPhoneNumber(request.getPhoneNumber());
        }

        UserEntity updatedUser = userRepository.save(existingUser);

        return modelMapper.map(updatedUser, UserResponse.class);
    }
}
