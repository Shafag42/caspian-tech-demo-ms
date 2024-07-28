package com.caspiantech.user.ms.service;


import com.caspiantech.user.ms.model.dto.request.LoginRequest;
import com.caspiantech.user.ms.model.dto.request.RegisterRequest;
import com.caspiantech.user.ms.model.dto.response.JwtAuthResponse;

public interface AuthService {

    void register (RegisterRequest registerRequest);

    JwtAuthResponse login(LoginRequest loginRequest);

    void logout();

    JwtAuthResponse refreshAccessToken(String refreshTokenRequest);
}
