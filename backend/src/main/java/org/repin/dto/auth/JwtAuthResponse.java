package org.repin.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
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
    Collection<? extends GrantedAuthority> role;

    private String email;
    private UUID userId;

    public JwtAuthResponse(String accessToken, long expiresIn, Collection<? extends GrantedAuthority> authorities, String email, UUID userId) {
        this.accessToken = accessToken;
        this.expiresIn = expiresIn;
        this.role = authorities;
        this.email = email;
        this.userId = userId;
    }
}