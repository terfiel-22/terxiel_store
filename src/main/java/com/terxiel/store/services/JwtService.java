package com.terxiel.store.services;

import com.terxiel.store.config.JwtConfig;
import com.terxiel.store.entities.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@AllArgsConstructor
@Service
public class JwtService {

    private final JwtConfig jwtConfig;

    public Jwt generateAccessToken(User user)
    {
        return generateJwtToken(user, jwtConfig.getAccessTokenExpiration());
    }

    public Jwt generateRefreshToken(User user)
    {
        return generateJwtToken(user, jwtConfig.getRefreshTokenExpiration());
    }

    public Jwt parseToken(String token)
    {
        try {
            var claims = getClaims(token);
            return new Jwt(claims,jwtConfig.getSecretKey());
        } catch (Exception e) {
            return null;
        }

    }

    private Jwt generateJwtToken(User user, long tokenExpiration) {
        final Date today = new Date(System.currentTimeMillis());
        final Date expirationDate = new Date(System.currentTimeMillis() + 1000 * tokenExpiration);

        var claims = Jwts.claims()
                .subject(String.valueOf(user.getId()))
                .add("name", user.getName())
                .add("email", user.getEmail())
                .add("role", user.getRole())
                .issuedAt(today)
                .expiration(expirationDate)
                .build();

        return new Jwt(claims, jwtConfig.getSecretKey());
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
