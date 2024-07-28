package com.caspiantech.user.ms.model.dto;


import com.caspiantech.user.ms.model.enums.AccountStatus;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.Objects;

@Data
@ToString(exclude = {"password"})
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private Long id;
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
