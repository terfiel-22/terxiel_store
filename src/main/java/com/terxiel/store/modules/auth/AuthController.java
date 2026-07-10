package com.terxiel.store.modules.auth;

import com.terxiel.store.config.JwtConfig;
import com.terxiel.store.shared.dtos.ErrorDTO;
import com.terxiel.store.modules.auth.dtos.JwtResponse;
import com.terxiel.store.modules.auth.dtos.LoginRequest;
import com.terxiel.store.modules.user.exceptions.UserNotFoundException;
import com.terxiel.store.modules.user.dtos.UserSummary;
import com.terxiel.store.mappers.UserMapper;
import com.terxiel.store.modules.auth.services.AuthService;
import com.terxiel.store.modules.auth.services.JwtService;
import com.terxiel.store.repositories.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final JwtConfig jwtConfig;
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(
            @Valid @RequestBody LoginRequest  request,
            HttpServletResponse httpServletResponse
    )
    {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );

        var user = userRepository.findByEmail(request.email()).orElseThrow(UserNotFoundException::new);

        // Issue refreshToken on cookie.
        var refreshToken = jwtService.generateRefreshToken(user);
        var cookie = new Cookie("refreshToken", refreshToken.toString());
        cookie.setHttpOnly(true);
        cookie.setPath("/auth/refresh");
        cookie.setMaxAge(jwtConfig.getRefreshTokenExpiration()); // 7d
        cookie.setSecure(true);
        httpServletResponse.addCookie(cookie);

        var accessToken = jwtService.generateAccessToken(user);
        return ResponseEntity.ok(new JwtResponse(accessToken.toString()));
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtResponse> refresh(@CookieValue(value = "refreshToken") String refreshToken)
    {
        var jwt = jwtService.parseToken(refreshToken);
        if(jwt == null || jwt.isExpired())
        {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        var user = userRepository.findById(jwt.getUserId()).orElseThrow(UserNotFoundException::new);
        var accessToken = jwtService.generateAccessToken(user);
        return ResponseEntity.ok(new JwtResponse(accessToken.toString()));
    }

    @GetMapping("/me")
    public ResponseEntity<UserSummary> me()
    {
        return ResponseEntity.ok(userMapper.toDto(authService.getCurrentUser()));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorDTO> handleInvalidCredential()
    {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                new ErrorDTO("Please provide a valid credential.")
        );
    }
}
