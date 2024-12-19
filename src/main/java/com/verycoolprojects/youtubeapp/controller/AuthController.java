package com.verycoolprojects.youtubeapp.controller;


import com.verycoolprojects.youtubeapp.dto.auth.LoginRequest;
import com.verycoolprojects.youtubeapp.dto.auth.RegisterRequest;
import com.verycoolprojects.youtubeapp.dto.auth.TokenResponse;
import com.verycoolprojects.youtubeapp.service.AccountService;
import com.verycoolprojects.youtubeapp.service.JwtService;
import com.verycoolprojects.youtubeapp.util.Utils;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AccountService accountService;
    private final JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<TokenResponse> register(@RequestBody RegisterRequest request, HttpServletResponse response) {
        log.debug("Register Request: {}", request.mask());

        String username = request.getUsername();
        String password = request.getPassword();

        if (accountService.register(username, password)) {
            jwtService.setRefreshToken(username, response);
            String accessToken = jwtService.generateAccessToken(username);

            TokenResponse tokenResponse = new TokenResponse(username, accessToken);
            log.debug("Register Response: {}", tokenResponse.mask());
            return ResponseEntity.ok(tokenResponse);
        } else {
            log.debug("Register Response: 409 Conflict");
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest request, HttpServletResponse response) {
        log.debug("Register Request: {}", request.mask());

        String username = request.getUsername();
        String password = request.getPassword();
        if (accountService.verify(username, password)) {
            jwtService.setRefreshToken(username, response);
            String accessToken = jwtService.generateAccessToken(username);

            TokenResponse tokenResponse = new TokenResponse(username, accessToken);
            log.debug("Login Response: {}", tokenResponse.mask());
            return ResponseEntity.ok(tokenResponse);
        } else {
            log.debug("Login Response: 401 Unauthorized");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refresh(@CookieValue("refreshToken") String refreshToken,
                                                 HttpServletResponse response) {
        log.debug("Refresh Request: refreshToken={}", Utils.maskToken(refreshToken));

        String username = jwtService.validateRefreshToken(refreshToken, response);
        String accessToken = jwtService.generateAccessToken(username);

        TokenResponse tokenResponse = new TokenResponse(username, accessToken);
        log.debug("Refresh Response: {}", tokenResponse.mask());
        return ResponseEntity.ok(tokenResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@CookieValue("refreshToken") String refreshToken, HttpServletResponse response) {
        log.debug("Logout Request: refreshToken={}", Utils.maskToken(refreshToken));

        jwtService.revokeRefreshToken(refreshToken, response);

        log.debug("Logout Response: 200 Ok");
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
