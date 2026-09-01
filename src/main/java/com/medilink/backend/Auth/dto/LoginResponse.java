package com.medilink.backend.Auth.dto;

import com.medilink.backend.User.dto.UserResponse;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Getter
@Setter
public class LoginResponse {

    private String accessToken;

    private String refreshToken;

    private UserResponse user;
}
