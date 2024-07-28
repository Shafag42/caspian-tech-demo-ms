package com.caspiantech.user.ms.exceptions;


import lombok.Builder;

@Builder
public record ErrorResponse(String message) {
}

