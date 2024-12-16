package com.verycoolprojects.youtubeapp.jwt;

import com.verycoolprojects.youtubeapp.service.AccountDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    private final JwtService jwtService;
    private final AccountDetailsService accountDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader(AUTHORIZATION_HEADER);
        if (isBearerToken(authHeader)) {
            String token = authHeader.substring(BEARER_PREFIX.length());
            String username = jwtService.extractUserName(token);

            if (username != null && isAuthenticationAbsent()) {
                processTokenAuthentication(token, username, request);

            }
        }

        filterChain.doFilter(request, response);
    }

    private boolean isBearerToken(String authHeader) {
        return authHeader != null && authHeader.startsWith(BEARER_PREFIX);
    }

    private boolean isAuthenticationAbsent() {
        return SecurityContextHolder.getContext().getAuthentication() == null;
    }

    private void processTokenAuthentication(String token, String username, HttpServletRequest request) {
        UserDetails userDetails = accountDetailsService.loadUserByUsername(username);

        if (jwtService.validateToken(token, userDetails)) {
            // Create authentication token
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities()
            );

            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }
    }
}