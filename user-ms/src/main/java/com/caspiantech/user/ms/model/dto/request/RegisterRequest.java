package com.caspiantech.user.ms.model.dto.request;

import com.caspiantech.user.ms.model.enums.AccountStatus;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import lombok.*;


@Data
@ToString(exclude = {"password"})
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class RegisterRequest {
    @NotBlank
    private String name;

    private Integer age;

    private String region;

    private Double salary;

    @NotBlank
    private String password;

    @NotBlank
    private String email;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private AccountStatus accountStatus = AccountStatus.ENABLED;

}
