package com.verycoolprojects.youtubeapp.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;
import java.util.function.Function;

@Service
public class JwtService {
    private final SecretKey secretKey;

    @SneakyThrows
    public JwtService(@Value("${jwt.secretKey}") String secretKeyString) {
        this.secretKey = Keys.hmacShaKeyFor(
                Base64.getDecoder().decode(secretKeyString.getBytes(StandardCharsets.UTF_8)));
    }

    public String generateToken(String username) {
        long currentTime = System.currentTimeMillis();
        return Jwts.builder()
                .header()
                .type("JWT")
                .and()
                .claims()
                .id(UUID.randomUUID().toString())
                .subject(username)
                .issuedAt(new Date(currentTime))
                .expiration(new Date(currentTime + 7 * 24 * 60 * 60 * 1000))
                .and()
                .signWith(secretKey)
                .compact();

    }

    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        final String userName = extractUserName(token);
        return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

}
