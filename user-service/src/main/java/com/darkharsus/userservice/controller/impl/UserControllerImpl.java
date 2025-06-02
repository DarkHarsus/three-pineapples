package com.darkharsus.userservice.controller.impl;

import com.darkharsus.userservice.controller.UserController;
import com.darkharsus.userservice.dto.LoginRequest;
import com.darkharsus.userservice.dto.LoginResponse;
import com.darkharsus.userservice.dto.RegisterRequest;
import com.darkharsus.userservice.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class UserControllerImpl implements UserController {

    private final UserService userService;

    @Override

    public ResponseEntity<Void> register(@Valid @RequestBody RegisterRequest request) {
        userService.register(request);
        return ResponseEntity.ok().build();
    }

    @Override

    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(userService.login(request));
    }
}
