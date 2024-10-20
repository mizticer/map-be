package com.employeedashboard.oirs.dto.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthRequest {
    @NotBlank(message = "EMPTY PASSWORD")
    private String password;
    @NotBlank(message = "EMPTY EMAIL")
    private String username;

}

