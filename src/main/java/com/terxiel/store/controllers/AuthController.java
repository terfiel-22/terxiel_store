package com.terxiel.store.controllers;

import com.terxiel.store.dtos.JwtResponse;
import com.terxiel.store.dtos.LoginRequest;
import com.terxiel.store.dtos.UserSummary;
import com.terxiel.store.exceptions.AuthenticationNotFoundException;
import com.terxiel.store.exceptions.UserNotFoundException;
import com.terxiel.store.mappers.UserMapper;
import com.terxiel.store.repositories.UserRepository;
import com.terxiel.store.services.JwtService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@AllArgsConstructor
@RestController
@RequestMapping("auth")
class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(
            @Valid @RequestBody LoginRequest  request
    )
    {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );

        String token = jwtService.generateToken(request.email());

        return ResponseEntity.ok(new JwtResponse(token));
    }

    @PostMapping("/validate")
    public boolean validate(@RequestHeader("Authorization") String authHeader)
    {
        System.out.println("Validate Token...");
        var token = authHeader.replace("Bearer ", "");
        return jwtService.validateToken(token);
    }

    @GetMapping("/me")
    public ResponseEntity<UserSummary> me()
    {
        // 1. Extract the current principal.
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null)
            throw new AuthenticationNotFoundException();
        var email = (String) authentication.getPrincipal();

        // 2. Lookup the user.
        var user = userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);

        // 3. Return a response.
        return ResponseEntity.ok(userMapper.toDto(user));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Map<String,String>> handleInvalidCredential()
    {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                Map.of("error","Please provide a valid credential.")
        );
    }

    @ExceptionHandler(AuthenticationNotFoundException.class)
    public ResponseEntity<Map<String,String>> handleAuthenticationNotFound()
    {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                Map.of("error","You are not currently signed in.")
        );
    }
}
