package com.terxiel.store.services;

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

    public String generateToken(String email)
    {
        final long tokenExpiration = 86400; // 1-day
        final Date today = new Date(System.currentTimeMillis());
        final Date expirationDate = new Date(System.currentTimeMillis() + 1000 * tokenExpiration);

        return Jwts.builder()
                .subject(email)
                .issuedAt(today)
                .expiration(expirationDate)
                .signWith(getSigningKey())
                .compact();
    }

    public boolean validateToken(String token)
    {
        try {
            var claims = Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

           return claims.getExpiration().after(new Date());
        } catch (JwtException ex)
        {
            return false;
        }
    }

    private SecretKey getSigningKey()
    {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
