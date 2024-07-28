package com.caspiantech.user.ms.model.dto.response;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"accessToken", "refreshToken"})
public class JwtAuthResponse {

    private String accessToken;
    private String refreshToken;
}
