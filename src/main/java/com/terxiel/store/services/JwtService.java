package com.terxiel.store.services;

import com.terxiel.store.config.JwtConfig;
import com.terxiel.store.entities.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@AllArgsConstructor
@Service
public class JwtService {

    private final JwtConfig jwtConfig;

    public String generateAccessToken(User user)
    {
        return generateJwtToken(user, jwtConfig.getAccessTokenExpiration());
    }

    public String generateRefreshToken(User user)
    {
        return generateJwtToken(user, jwtConfig.getRefreshTokenExpiration());
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
                .signWith(jwtConfig.getSecretKey())
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

    private Claims getClaims(String token)
    {
        return Jwts.parser()
                .verifyWith(jwtConfig.getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
