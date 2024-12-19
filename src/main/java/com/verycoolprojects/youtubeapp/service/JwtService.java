package com.verycoolprojects.youtubeapp.service;

import com.verycoolprojects.youtubeapp.entity.Token;
import com.verycoolprojects.youtubeapp.repository.TokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.Base64;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

@Service
public class JwtService {
    private final boolean secureCookies;
    private final SecretKey secretKey;
    private final JwtParser parser;
    private final TokenRepository repository;

    @SneakyThrows
    public JwtService(@Value("${jwt.secureCookies}") boolean secureCookies,
                      @Value("${jwt.secretKey}") String secretKeyString, TokenRepository repository) {
        this.secureCookies = secureCookies;
        this.secretKey = Keys.hmacShaKeyFor(
                Base64.getDecoder().decode(secretKeyString.getBytes(StandardCharsets.UTF_8)));
        this.parser = Jwts.parser().verifyWith(secretKey).build();
        this.repository = repository;
    }

    public String generateAccessToken(String username) {
        long currentTime = System.currentTimeMillis();
        return Jwts.builder()
                .header()
                .type("JWT")
                .and()
                .claims()
                .id(UUID.randomUUID().toString())
                .subject(username)
                .issuedAt(new Date(currentTime))
                .expiration(new Date(currentTime + 15 * 60 * 1000))
                .and()
                .signWith(secretKey)
                .compact();

    }

    public void setRefreshToken(String username, HttpServletResponse response) {
        String id = UUID.randomUUID().toString();
        int maxAge = 7 * 24 * 60 * 60;
        long issuedAt = System.currentTimeMillis();
        long expiresAt = issuedAt + maxAge * 1000;

        String refreshToken = Jwts.builder()
                .header()
                .type("JWT")
                .and()
                .claims()
                .id(id)
                .issuedAt(new Date(issuedAt))
                .expiration(new Date(expiresAt))
                .and()
                .signWith(secretKey)
                .compact();

        Token token = Token.builder()
                .id(id)
                .username(username)
                .issuedAt(new Timestamp(issuedAt))
                .expiresAt(new Timestamp(expiresAt))
                .revoked(false)
                .build();

        Cookie cookie = new Cookie("refreshToken", refreshToken);
        cookie.setSecure(secureCookies);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(maxAge);
        response.addCookie(cookie);

        repository.save(token);
    }

    public String extractUserName(Claims claims) {
        return extractClaim(claims, Claims::getSubject);
    }

    public String extractId(Claims claims) {
        return extractClaim(claims, Claims::getId);
    }

    private <T> T extractClaim(Claims claims, Function<Claims, T> claimResolver) {
        return claimResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return parser.parseSignedClaims(token).getPayload();
    }

    public String validateAccessToken(String token) {
        Claims claims = extractAllClaims(token);

        if (isExpired(claims)) {
            throw new JwtException("The access token has expired");
        }

        return extractUserName(claims);
    }

    public String validateRefreshToken(String refreshToken, HttpServletResponse response) {
        Claims claims = extractAllClaims(refreshToken);

        if (isExpired(claims)) {
            throw new JwtException("The refresh token has expired");
        }

        Optional<Token> optionalToken = repository.findById(extractId(claims));
        if (optionalToken.isEmpty()) {
            throw new JwtException("Couldn't validate the refresh token");
        }

        Token token = optionalToken.get();
        if (token.isRevoked()) {
            throw new JwtException("The refresh token has been revoked");
        }

        String username = token.getUsername();
        if (soonToExpire(claims)) {
            setRefreshToken(username, response);
            invalidate(token);
        }
        return token.getUsername();
    }

    public void revokeRefreshToken(String refreshToken, HttpServletResponse response) {

        Cookie cookie = new Cookie("refreshToken", "");
        cookie.setSecure(secureCookies);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        Claims claims = extractAllClaims(refreshToken);
        Optional<Token> optionalToken = repository.findById(extractId(claims));
        if (optionalToken.isEmpty()) {
            throw new JwtException("Couldn't validate the refresh token");
        }

        Token token = optionalToken.get();
        invalidate(token);
    }

    public boolean soonToExpire(Claims claims) {
        return extractExpiration(claims).before(new Date(System.currentTimeMillis() + 5 * 24 * 60 * 60 * 1000));
    }

    public void invalidate(Token token) {
        token.setRevoked(true);
        repository.save(token);
    }

    private boolean isExpired(Claims claims) {
        return extractExpiration(claims).before(new Date());
    }

    private Date extractExpiration(Claims claims) {
        return extractClaim(claims, Claims::getExpiration);
    }

}
