package com.example.SmartHouse.jwt;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.TemporalUnit;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.example.SmartHouse.entity.Token;
import com.example.SmartHouse.enums.TokenType;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtTokenProviderImpl implements JwtTokenProvider {

    @Value("${jwt.secret}")
    private String secret;

    private SecretKey getSigningKey() {
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    @Override
    public Token generateAccessToken(Map<String, Object> extraClaims, long duration, TemporalUnit durationType, UserDetails user) {
        Instant now = Instant.now();
        Instant expiry = now.plus(duration, durationType);
        String tokenValue = Jwts.builder()
                .claims(extraClaims)
                .subject(user.getUsername())
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiry))
                .signWith(getSigningKey())
                .compact();
        // user будет установлен позже в AuthServiceImpl (token.setUser(user))
        return new Token(TokenType.ACCESS, tokenValue, LocalDateTime.ofInstant(expiry, ZoneOffset.UTC), false, null);
    }

    @Override
    public Token generateRefreshToken(long duration, TemporalUnit durationType, UserDetails user) {
        Instant now = Instant.now();
        Instant expiry = now.plus(duration, durationType);
        String tokenValue = Jwts.builder()
                .subject(user.getUsername())
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiry))
                .signWith(getSigningKey())
                .compact();
        return new Token(TokenType.REFRESH, tokenValue, LocalDateTime.ofInstant(expiry, ZoneOffset.UTC), false, null);
    }

    @Override
    public boolean validateToken(String tokenValue) {
        try {
            Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(tokenValue);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public String getUsernameFromToken(String tokenValue) {
        return extractClaim(tokenValue, Claims::getSubject);
    }

    @Override
    public LocalDateTime getExpiryDateFromToken(String tokenValue) {
        Date expiry = extractClaim(tokenValue, Claims::getExpiration);
        return expiry.toInstant().atOffset(ZoneOffset.UTC).toLocalDateTime();
    }

    private <T> T extractClaim(String tokenValue, Function<Claims, T> claimsResolver) {
        Claims claims = Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(tokenValue)
                .getPayload();
        return claimsResolver.apply(claims);
    }
}