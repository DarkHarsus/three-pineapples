package com.darkharsus.userservice.service.impl;

import com.darkharsus.userservice.dto.LoginRequest;
import com.darkharsus.userservice.dto.LoginResponse;
import com.darkharsus.userservice.dto.RegisterRequest;
import com.darkharsus.userservice.enums.Role;
import com.darkharsus.userservice.entity.User;
import com.darkharsus.userservice.repository.UserRepository;
import com.darkharsus.userservice.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private UserServiceImpl userService;

    private RegisterRequest registerRequest;
    private LoginRequest loginRequest;
    private User user;

    @BeforeEach
    void setUp() {
        registerRequest = new RegisterRequest();
        registerRequest.setEmail("test@example.com");
        registerRequest.setPassword("password");
        registerRequest.setRole("USER"); // String вместо Role
        registerRequest.setName("John");
        registerRequest.setSurname("Doe");
        registerRequest.setPhoneNumber("+1234567890");

        loginRequest = new LoginRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("password");

        user = new User();
        user.setEmail("test@example.com");
        user.setPassword("encodedPassword");
        user.setRole(Role.USER);
        user.setName("John");
        user.setSurname("Doe");
        user.setPhoneNumber("+1234567890");
        user.setCreatedAt(LocalDateTime.now());
    }

    @Test
    void testRegisterSuccess() {
        when(userRepository.findByEmail(registerRequest.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(registerRequest.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        userService.register(registerRequest);

        verify(userRepository).save(argThat(u ->
                u.getEmail().equals(registerRequest.getEmail()) &&
                        u.getRole() == Role.valueOf(registerRequest.getRole()) && // Преобразуем String в Role
                        u.getName().equals(registerRequest.getName()) &&
                        u.getSurname().equals(registerRequest.getSurname()) &&
                        u.getPhoneNumber().equals(registerRequest.getPhoneNumber())
        ));
    }

    @Test
    void testRegisterUserAlreadyExists() {
        when(userRepository.findByEmail(registerRequest.getEmail())).thenReturn(Optional.of(user));

        assertThrows(RuntimeException.class, () -> userService.register(registerRequest), "User already exists");
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testLoginSuccess() {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(mock(Authentication.class));
        when(userRepository.findByEmail(loginRequest.getEmail())).thenReturn(Optional.of(user));
        when(jwtUtil.generateToken(user.getEmail(), user.getRole().name())).thenReturn("jwtToken");

        LoginResponse response = userService.login(loginRequest);

        assertEquals("jwtToken", response.getToken());
        verify(authenticationManager).authenticate(argThat(auth ->
                auth.getPrincipal().equals(loginRequest.getEmail()) &&
                        auth.getCredentials().equals(loginRequest.getPassword())
        ));
        verify(jwtUtil).generateToken(user.getEmail(), user.getRole().name());
    }

    @Test
    void testLoginUserNotFound() {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(mock(Authentication.class));
        when(userRepository.findByEmail(loginRequest.getEmail())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userService.login(loginRequest), "User not found");
    }

    @Test
    void testLoginInvalidCredentials() {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new AuthenticationException("Invalid credentials") {});

        assertThrows(RuntimeException.class, () -> userService.login(loginRequest), "Invalid credentials");
    }
}