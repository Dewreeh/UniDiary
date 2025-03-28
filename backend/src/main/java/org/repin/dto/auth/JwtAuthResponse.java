package org.repin.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtAuthResponse {
    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("token_type")
    private String tokenType = "Bearer";

    @JsonProperty("expires_in")
    private long expiresIn;

    @JsonProperty("role")
    private String role;

    private String email;
    private UUID userId;

    public JwtAuthResponse(String accessToken, long expiresIn, String role, String email, UUID userId) {
        this.accessToken = accessToken;
        this.expiresIn = expiresIn;
        this.role = role;
        this.email = email;
        this.userId = userId;
    }
}