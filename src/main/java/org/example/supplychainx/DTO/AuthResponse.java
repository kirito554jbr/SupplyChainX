package org.example.supplychainx.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {

    private String accessToken;
    private String refreshToken;
    private long accessTokenExpiry;
    private String tokenType = "Bearer";
    private UserInfoDTO user;

    public AuthResponse(String accessToken, String refreshToken, long accessTokenExpiry, UserInfoDTO user) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.accessTokenExpiry = accessTokenExpiry;
        this.user = user;
    }
}

