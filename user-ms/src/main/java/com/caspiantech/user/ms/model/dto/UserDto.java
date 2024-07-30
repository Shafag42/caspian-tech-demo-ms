package com.caspiantech.user.ms.model.dto;


import com.caspiantech.user.ms.model.enums.AccountStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import lombok.*;


@Getter
@Setter
@ToString(exclude = {"password"})
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private Long id;

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
