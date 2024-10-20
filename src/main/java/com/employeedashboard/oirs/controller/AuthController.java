package com.employeedashboard.oirs.controller;

import com.employeedashboard.oirs.dto.auth.AuthRequest;
import com.employeedashboard.oirs.dto.auth.AuthResponse;
import com.employeedashboard.oirs.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService service;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody @Validated AuthRequest authRequest) {
        return ResponseEntity.ok(service.login(authRequest));
    }

}
