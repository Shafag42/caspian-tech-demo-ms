package com.caspiantech.user.ms.controller;

import com.caspiantech.user.ms.model.dto.request.LoginRequest;
import com.caspiantech.user.ms.model.dto.request.RefreshTokenRequest;
import com.caspiantech.user.ms.model.dto.request.RegisterRequest;
import com.caspiantech.user.ms.model.dto.response.JwtAuthResponse;
import com.caspiantech.user.ms.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @ResponseStatus(CREATED)
    public void register(@Valid @RequestBody RegisterRequest registerRequest) {
        authService.register(registerRequest);
    }

    @PostMapping("/login")
    public JwtAuthResponse login(@Valid @RequestBody LoginRequest loginRequest) {
        return authService.login(loginRequest);
    }

    @PostMapping("/refresh")
    public JwtAuthResponse refresh(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        return authService.refreshAccessToken(refreshTokenRequest.getRefreshToken());
    }
}
