package com.homestay.homestayweb.service.implementation;

import com.homestay.homestayweb.dto.request.LoginRequest;
import com.homestay.homestayweb.dto.request.SignupRequest;
import com.homestay.homestayweb.dto.response.JwtResponse;
import com.homestay.homestayweb.entity.Role;
import com.homestay.homestayweb.entity.User;
import com.homestay.homestayweb.enums.ERole;
import com.homestay.homestayweb.repository.RoleRepository;
import com.homestay.homestayweb.repository.UserRepository;
import com.homestay.homestayweb.security.JwtUtil;
import com.homestay.homestayweb.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authManager;

    @Override
    public JwtResponse login(LoginRequest request) {
        Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String token = jwtUtil.generateToken(user.getEmail());

        List<String> roles = user.getRoles().stream()
                .map(role -> role.getName().name())
                .collect(Collectors.toList());

        return new JwtResponse(
                token, "Bearer", user.getId(),
                user.getUsername(), user.getEmail(), roles
        );
    }

    @Override
    public String register(SignupRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            return "Email already in use";
        }

        Set<Role> roles = new HashSet<>();
        Set<String> strRoles = request.getRoles();

        if (strRoles == null || strRoles.isEmpty()) {
            roles.add(getRole(ERole.USER));
        } else {
            for (String role : strRoles) {
                switch (role.toLowerCase()) {
                    case "admin": roles.add(getRole(ERole.ADMIN)); break;
                    case "host": roles.add(getRole(ERole.HOST)); break;
                    default: roles.add(getRole(ERole.USER)); break;
                }
            }
        }

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(roles)
                .build();

        userRepository.save(user);
        return "User registered successfully";
    }

    private Role getRole(ERole name) {
        return roleRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("Role " + name + " not found"));
    }
}
