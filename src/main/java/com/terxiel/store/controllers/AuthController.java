package com.terxiel.store.controllers;

import com.terxiel.store.dtos.LoginRequest;
import com.terxiel.store.exceptions.InvalidCredentialException;
import com.terxiel.store.repositories.UserRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@AllArgsConstructor
@RestController
@RequestMapping("auth")
class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<Void> login(
            @Valid @RequestBody LoginRequest  request
    )
    {
        var user = userRepository.findByEmail(request.email()).orElseThrow(InvalidCredentialException::new);
        var hashedPassword = user.getPassword();

        if(!passwordEncoder.matches(request.password(), hashedPassword))
        {
            throw new InvalidCredentialException();
        }

        return ResponseEntity.ok().build();
    }

    @ExceptionHandler(InvalidCredentialException.class)
    public ResponseEntity<Map<String,String>> handleInvalidCredential()
    {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                Map.of("error","Please provide a valid credential.")
        );
    }
}
