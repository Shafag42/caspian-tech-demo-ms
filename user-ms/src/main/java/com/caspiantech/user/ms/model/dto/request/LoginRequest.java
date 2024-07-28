package com.caspiantech.user.ms.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.Objects;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class LoginRequest {
    @NotBlank
    private String email;

    @NotBlank
    @ToString.Exclude
    private String password;
}
