package com.paisawise.controller;

import com.paisawise.dto.User.ChangePasswordRequest;
import com.paisawise.dto.User.ProfileResponse;
import com.paisawise.dto.User.UpdateProfileRequest;
import com.paisawise.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
@CrossOrigin("*")
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping
    public ResponseEntity<ProfileResponse> getProfile() {

        return ResponseEntity.ok(
                profileService.getProfile()
        );
    }

    @PutMapping
    public ResponseEntity<ProfileResponse> updateProfile(
            @RequestBody UpdateProfileRequest request
    ) {

        return ResponseEntity.ok(
                profileService.updateProfile(request)
        );
    }

    @PutMapping("/password")
    public ResponseEntity<String> changePassword(
            @RequestBody ChangePasswordRequest request
    ) {

        profileService.changePassword(request);

        return ResponseEntity.ok(
                "Password changed successfully"
        );
    }
}
