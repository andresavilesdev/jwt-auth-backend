package com.andresavilesdev.demo_jwt.security.auth.service;

import com.andresavilesdev.demo_jwt.security.auth.dto.requests.LoginRequest;
import com.andresavilesdev.demo_jwt.security.auth.dto.requests.RegisterRequest;
import com.andresavilesdev.demo_jwt.security.auth.dto.responses.AuthResponse;
import com.andresavilesdev.demo_jwt.security.jwt.service.JwtService;
import com.andresavilesdev.demo_jwt.security.user.model.UserEntity;
import com.andresavilesdev.demo_jwt.security.user.repository.IUserRepository;
import com.andresavilesdev.demo_jwt.security.user.role.Role;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final IUserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    public AuthService(IUserRepository userRepository, JwtService jwtService, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
    }

    public AuthResponse login(LoginRequest loginRequest) {

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        UserDetails userDetails = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Username not found"));
        String token = jwtService.getToken(userDetails);

        return AuthResponse.builder()
                .token(token)
                .build();
    }

    public AuthResponse register(RegisterRequest registerRequest) {

        UserEntity userEntity = UserEntity.builder()
                .username(registerRequest.getUsername())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .firstname(registerRequest.getFirstname())
                .lastname(registerRequest.getLastname())
                .country(registerRequest.getCountry())
                .role(Role.USER)
                .build();

        userRepository.save(userEntity);

        return AuthResponse.builder()
                .token(jwtService.getToken(userEntity))
                .build();
    }

    public AuthResponse registerAdmin(RegisterRequest registerRequest) {

        UserEntity userEntity = UserEntity.builder()
                .username(registerRequest.getUsername())
                .password(registerRequest.getPassword())
                .firstname(registerRequest.getFirstname())
                .lastname(registerRequest.getLastname())
                .country(registerRequest.getCountry())
                .role(Role.ADMIN)
                .build();

        userRepository.save(userEntity);

        return AuthResponse.builder()
                .token(jwtService.getToken(userEntity))
                .build();
    }
}
