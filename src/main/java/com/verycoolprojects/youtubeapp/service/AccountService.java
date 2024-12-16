package com.verycoolprojects.youtubeapp.service;

import com.verycoolprojects.youtubeapp.model.auth.Account;
import com.verycoolprojects.youtubeapp.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository repository;
    private final PasswordEncoder encoder;

    public boolean register(String username, String password) {
        Optional<Account> optionalAccount = repository.findByUsername(username);
        if (optionalAccount.isEmpty()) {
            Account account = Account.builder()
                    .username(username)
                    .password(encoder.encode(password))
                    .build();
            repository.save(account);
            return true;
        } else {
            log.debug("Username already exists: {}", username);
            return false;
        }
    }
}
