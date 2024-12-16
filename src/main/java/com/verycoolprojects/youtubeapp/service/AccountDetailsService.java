package com.verycoolprojects.youtubeapp.service;

import com.verycoolprojects.youtubeapp.model.auth.AccountDetails;
import com.verycoolprojects.youtubeapp.model.auth.Account;
import com.verycoolprojects.youtubeapp.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountDetailsService implements UserDetailsService {
    private final AccountRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Account> optionalAccount = repository.findByUsername(username);
        return new AccountDetails(optionalAccount.orElseThrow(() -> {
            log.debug("User Not Found: {}", username);
            return new UsernameNotFoundException("User not found");
        }));
    }
}
