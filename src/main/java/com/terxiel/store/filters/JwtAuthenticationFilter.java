package com.terxiel.store.filters;

import com.terxiel.store.services.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@AllArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    @NullMarked
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        // 1. Extract the 'Authorization' header from the incoming HTTP request
        var authHeader = request.getHeader("Authorization");

        // 2. Validate header: If missing or does not start with 'Bearer ', skip JWT processing
        // and pass the request down the security filter chain (unauthenticated state)
        if(authHeader == null || !authHeader.startsWith("Bearer "))
        {
            filterChain.doFilter(request, response);
            return;
        }

        // 3. Extract the clean token string by stripping out the "Bearer " prefix
        var token = authHeader.replace("Bearer ","");

        // 4. Validate token structure/signature. If invalid, stop processing and
        // let the request continue through the chain (Spring Security will block it later if protected)
        if(!jwtService.validateToken(token))
        {
            filterChain.doFilter(request, response);
            return;
        }

        // 5. Create an authentication token object using the user's id extracted from the JWT.
        // Note: Credentials are null (not needed after token check) and authorities are currently null.
        var authentication = new UsernamePasswordAuthenticationToken(
                jwtService.getSubjectFromToken(token),
                null,
                null
        );

        // 6. Build and attach request-specific details (like IP address, session ID) to the authentication object
        authentication.setDetails(
                new WebAuthenticationDetailsSource().buildDetails(request)
        );

        // 7. Store the authenticated object into Spring's SecurityContext.
        // This signals to the rest of the application that the user is fully logged in.
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 8. Continue to the next filter in the security filter chain (e.g., reaching your controller)
        filterChain.doFilter(request, response);
    }
}
