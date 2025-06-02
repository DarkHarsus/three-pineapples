package com.darkharsus.userservice.service;

import com.darkharsus.userservice.dto.LoginRequest;
import com.darkharsus.userservice.dto.LoginResponse;
import com.darkharsus.userservice.dto.RegisterRequest;

public interface UserService {
    void register(RegisterRequest request);
    LoginResponse login(LoginRequest request);
}