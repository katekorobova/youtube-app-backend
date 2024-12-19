package com.verycoolprojects.youtubeapp.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {
    private String username;
    private String password;

    public LoginRequest mask() {
        return LoginRequest.builder()
                .username(username)
                .password("XXXX").build();
    }
}
