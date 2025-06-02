package com.darkharsus.userservice.service.impl;

import com.darkharsus.userservice.dto.LoginRequest;
import com.darkharsus.userservice.dto.LoginResponse;
import com.darkharsus.userservice.dto.RegisterRequest;
import com.darkharsus.userservice.entity.User;
import com.darkharsus.userservice.enums.Role;
import com.darkharsus.userservice.repository.UserRepository;
import com.darkharsus.userservice.security.JwtUtil;
import com.darkharsus.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static com.darkharsus.userservice.constants.ProjectConstants.ALREADY_EXIST;
import static com.darkharsus.userservice.constants.ProjectConstants.INVALID_CREDENTIALS;
import static com.darkharsus.userservice.constants.ProjectConstants.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    public void register(RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException(ALREADY_EXIST);
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.valueOf(request.getRole()));
        user.setName(request.getName());
        user.setSurname(request.getSurname());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setCreatedAt(LocalDateTime.now());

        userRepository.save(user);
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        try {
            User user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new RuntimeException(USER_NOT_FOUND));

            String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());
            return new LoginResponse(token);
        } catch (AuthenticationException e) {
            throw new RuntimeException(INVALID_CREDENTIALS);
        }
    }
}