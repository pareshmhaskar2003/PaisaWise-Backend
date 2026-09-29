package com.paisawise.dto.Register;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {

    private String message;
    private String token;

    public AuthResponse(String userRegisteredSuccessfully) {
    }
}
