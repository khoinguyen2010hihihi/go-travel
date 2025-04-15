package com.homestay.homestayweb.controller;

import com.homestay.homestayweb.dto.request.LoginRequest;
import com.homestay.homestayweb.dto.request.SignupRequest;
import com.homestay.homestayweb.dto.response.JwtResponse;
import com.homestay.homestayweb.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    @PostMapping("/login")
    public JwtResponse login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @PostMapping("/register")
    public String register(@RequestBody SignupRequest request) {
        return authService.register(request);
    }
}