package com.verycoolprojects.youtubeapp.dto.auth;

import com.verycoolprojects.youtubeapp.util.Utils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenResponse {
    private String username;
    private String accessToken;

    public TokenResponse mask() {
        return TokenResponse.builder()
                .username(username)
                .accessToken(Utils.maskToken(accessToken))
                .build();
    }
}
