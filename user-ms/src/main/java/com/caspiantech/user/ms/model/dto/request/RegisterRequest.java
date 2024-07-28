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
    @Column(name = "user_name", unique = true)
    private String name;

    @Column(name = "user_age")
    private Integer age;

    @Column(name = "region")
    private String region;

    @Column(name = "salary")
    private Double salary;

    @NotBlank
    @Column(name = "password")
    private String password;

    @NotBlank
    @Column(name = "email", unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "account_status")
    @Builder.Default
    private AccountStatus accountStatus = AccountStatus.ENABLED;

}
