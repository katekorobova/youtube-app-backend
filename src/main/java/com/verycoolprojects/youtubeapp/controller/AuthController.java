package com.verycoolprojects.youtubeapp.controller;


import com.verycoolprojects.youtubeapp.model.auth.Account;
import com.verycoolprojects.youtubeapp.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@Slf4j
@CrossOrigin
@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AccountService service;

    @PostMapping("/register")
    public ResponseEntity<Account> register(@RequestParam String username, @RequestParam String password) {
        log.debug("Register Request: username={}, password={}", username, password);
        boolean success = service.register(username, password);
        if (success) {
            log.debug("Register Response: 201 Created");
            return new ResponseEntity<>(HttpStatus.CREATED);
        } else {
            log.debug("Register Response: 409 Conflict");
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String username, @RequestParam String password) {
        Optional<String> optionalToken = service.verify(username, password);
        return optionalToken.map(ResponseEntity::ok)
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.UNAUTHORIZED));
    }
}
