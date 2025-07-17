package com.andresavilesdev.demo_jwt.security.auth.controller;

import com.andresavilesdev.demo_jwt.security.auth.dto.requests.LoginRequest;
import com.andresavilesdev.demo_jwt.security.auth.dto.requests.RegisterRequest;
import com.andresavilesdev.demo_jwt.security.auth.dto.responses.AuthResponse;
import com.andresavilesdev.demo_jwt.security.auth.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.login(loginRequest));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest registerRequest) {
        return ResponseEntity.ok(authService.register(registerRequest));
    }

}
