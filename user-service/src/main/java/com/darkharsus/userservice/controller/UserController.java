package com.darkharsus.userservice.controller;

import com.darkharsus.userservice.dto.LoginRequest;
import com.darkharsus.userservice.dto.LoginResponse;
import com.darkharsus.userservice.dto.RegisterRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

public interface UserController {
    /**
     * Endpoint register служит для регистрации новых пользователей.
     */
    @PostMapping("/register")
    ResponseEntity<Void> register(@Valid RegisterRequest request);

    /**
     * Endpoint login служит для авторизации пользователей.
     */
    @PostMapping("/login")
    ResponseEntity<LoginResponse> login(@Valid LoginRequest request);
}
