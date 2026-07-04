package com.terxiel.store.services;

import com.terxiel.store.entities.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {
    @Value("${spring.jwt.secret}")
    public String secret;

    public String generateAccessToken(User user)
    {
        final long tokenExpiration = 300; // 5m
        return generateJwtToken(user, tokenExpiration);
    }

    public String generateRefreshToken(User user)
    {
        final long tokenExpiration = 604800; // 7d
        return generateJwtToken(user, tokenExpiration);
    }

    private String generateJwtToken(User user, long tokenExpiration) {
        final Date today = new Date(System.currentTimeMillis());
        final Date expirationDate = new Date(System.currentTimeMillis() + 1000 * tokenExpiration);

        return Jwts.builder()
                .subject(String.valueOf(user.getId()))
                .claim("name", user.getName())
                .claim("email", user.getEmail())
                .issuedAt(today)
                .expiration(expirationDate)
                .signWith(getSigningKey())
                .compact();
    }

    public boolean validateToken(String token)
    {
        try {
            var claims = getClaims(token);

           return claims.getExpiration().after(new Date());
        } catch (JwtException ex)
        {
            return false;
        }
    }

    public Long getSubjectFromToken(String token)
    {
        return Long.valueOf(getClaims(token).getSubject());
    }

    private SecretKey getSigningKey()
    {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private Claims getClaims(String token)
    {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
