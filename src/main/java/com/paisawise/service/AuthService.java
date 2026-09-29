package com.paisawise.service;

import com.paisawise.dto.Register.AuthResponse;
import com.paisawise.dto.Register.RegisterRequest;
import com.paisawise.entity.User;
import com.paisawise.enums.Role;
import com.paisawise.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.paisawise.dto.Login.LoginRequest;
import com.paisawise.exception.AccountDisabledException;
import com.paisawise.exception.ResourceNotFoundException;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthResponse register(RegisterRequest request) {

        User user = new User();

        user.setName(request.getName());
        user.setEmail(request.getEmail());

        user.setPassword(
                passwordEncoder.encode(request.getPassword())
        );

        user.setRole(Role.USER);
        user.setEnabled(true);

        userRepository.save(user);

        return new AuthResponse("User registered successfully");
    }

    public AuthResponse login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Invalid email or password"));

        if (!user.isEnabled()) {
            throw new AccountDisabledException(
                "Your account has been disabled. Please contact the administrator."
            );
        }

        boolean passwordMatches = passwordEncoder.matches(
                request.getPassword(),
                user.getPassword()
        );

        if (!passwordMatches) {
            throw new RuntimeException("Invalid email or password");
        }

        String token = jwtService.generateToken(user.getEmail(), user.getRole().name());

        return new AuthResponse(
                "Login successful",
                token
        );
    }
}