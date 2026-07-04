package com.terxiel.store.handlers;

import com.terxiel.store.entities.BlacklistedToken;
import com.terxiel.store.repositories.BlacklistedTokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class JwtLogoutHandler implements LogoutHandler {
    private final BlacklistedTokenRepository blacklistedTokenRepository;

    @Override
    public void logout(HttpServletRequest request, @NonNull HttpServletResponse response, Authentication authentication) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            var token = authHeader.replace("Bearer ","");
            var blacklist =  BlacklistedToken.builder().token(token).build();
            blacklistedTokenRepository.save(blacklist);
        }
    }
}