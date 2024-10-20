package com.employeedashboard.oirs.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.UUID;


@RequiredArgsConstructor
@Data
@AllArgsConstructor
@Builder
public class AuthResponse {
    private String message;
    private String token;
    private UUID id;
}
