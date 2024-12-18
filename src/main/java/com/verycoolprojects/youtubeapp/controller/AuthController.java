package com.verycoolprojects.youtubeapp.controller;


import com.verycoolprojects.youtubeapp.dto.auth.LoginRequest;
import com.verycoolprojects.youtubeapp.dto.auth.RegisterRequest;
import com.verycoolprojects.youtubeapp.dto.auth.TokenResponse;
import com.verycoolprojects.youtubeapp.jwt.JwtService;
import com.verycoolprojects.youtubeapp.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AccountService accountService;
    private final JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<TokenResponse> register(@RequestBody RegisterRequest request) {
        log.debug("Register Request: {}", request);

        String username = request.getUsername();
        String password = request.getPassword();
        boolean success = accountService.register(username, password);

        if (success) {
            TokenResponse response = new TokenResponse(username, jwtService.generateToken(username));
            log.debug("Register Response: {}", response.mask());
            return ResponseEntity.ok(response);
        } else {
            log.debug("Register Response: 409 Conflict");
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest request) {
        log.debug("Register Request: {}", request);

        String username = request.getUsername();
        String password = request.getPassword();
        Optional<String> optionalToken = accountService.verify(username, password);

        if (optionalToken.isPresent()) {
            TokenResponse response = new TokenResponse(username, optionalToken.get());
            log.debug("Login Response: {}", response.mask());
            return ResponseEntity.ok(response);
        } else {
            log.debug("Login Response: 401 Unauthorized");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }
}
