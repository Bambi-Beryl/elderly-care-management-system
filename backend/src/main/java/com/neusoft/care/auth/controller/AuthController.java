package com.neusoft.care.auth.controller;

import com.neusoft.care.auth.dto.LoginRequest;
import com.neusoft.care.auth.dto.LoginResponse;
import com.neusoft.care.auth.dto.RegisterRequest;
import com.neusoft.care.auth.service.AuthService;
import com.neusoft.care.common.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public Result<LoginResponse> login(@RequestBody LoginRequest request) {
        return Result.ok(authService.login(request));
    }

    @PostMapping("/register")
    public Result<LoginResponse> register(@RequestBody RegisterRequest request) {
        return Result.ok(authService.register(request));
    }

    @PostMapping("/logout")
    public Result<Void> logout(@RequestHeader(value = "Authorization", required = false) String token) {
        authService.logout(token);
        return Result.ok();
    }

    @GetMapping("/me")
    public Result<LoginResponse> me(@RequestHeader(value = "Authorization", required = false) String token) {
        return Result.ok(authService.current(token));
    }
}
