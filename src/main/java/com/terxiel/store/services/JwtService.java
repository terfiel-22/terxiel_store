package com.terxiel.store.services;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {

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

    private SecretKey getSigningKey() {

        // Must be a base64-encoded string that is at least 256 bits long
        String SECRET_KEY = "YW55LXN1Y2gta2V5LXNob3VsZC1iZS12ZXJ5LWxvbmctYW5kLXNlY3VyZS0xMjM0NTY3ODk=";

        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
