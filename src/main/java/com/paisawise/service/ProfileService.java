package com.paisawise.service;

import com.paisawise.dto.User.ChangePasswordRequest;
import com.paisawise.dto.User.ProfileResponse;
import com.paisawise.dto.User.UpdateProfileRequest;
import com.paisawise.entity.User;
import com.paisawise.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public ProfileResponse getProfile() {

        User user = getCurrentUser();

        return new ProfileResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole().name()
        );
    }

    public ProfileResponse updateProfile(
            UpdateProfileRequest request
    ) {

        User user = getCurrentUser();

        if (request.getName() == null ||
                request.getName().trim().isEmpty()) {

            throw new RuntimeException(
                    "Name cannot be empty"
            );
        }

        user.setName(request.getName().trim());

        User updatedUser =
                userRepository.save(user);

        return new ProfileResponse(
                updatedUser.getId(),
                updatedUser.getName(),
                updatedUser.getEmail(),
                updatedUser.getRole().name()
        );
    }

    public void changePassword(
            ChangePasswordRequest request
    ) {

        User user = getCurrentUser();

        if (request.getCurrentPassword() == null ||
                request.getCurrentPassword().isBlank()) {

            throw new RuntimeException(
                    "Current password is required"
            );
        }

        if (request.getNewPassword() == null ||
                request.getNewPassword().isBlank()) {

            throw new RuntimeException(
                    "New password is required"
            );
        }

        if (!passwordEncoder.matches(
                request.getCurrentPassword(),
                user.getPassword()
        )) {

            throw new RuntimeException(
                    "Current password is incorrect"
            );
        }

        if (request.getNewPassword().length() < 6) {

            throw new RuntimeException(
                    "New password must be at least 6 characters"
            );
        }

        if (passwordEncoder.matches(
                request.getNewPassword(),
                user.getPassword()
        )) {

            throw new RuntimeException(
                    "New password must be different from current password"
            );
        }

        user.setPassword(
                passwordEncoder.encode(
                        request.getNewPassword()
                )
        );

        userRepository.save(user);
    }

    private User getCurrentUser() {

        String email =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName();

        return userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException(
                                "User not found"
                        )
                );
    }
}