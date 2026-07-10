package com.terxiel.store.modules.user;

import com.terxiel.store.modules.user.dtos.ChangePasswordRequest;
import com.terxiel.store.modules.user.dtos.RegisterUserRequest;
import com.terxiel.store.modules.user.dtos.UpdateUserRequest;
import com.terxiel.store.modules.user.dtos.UserSummary;
import com.terxiel.store.entities.Role;
import com.terxiel.store.mappers.UserMapper;
import com.terxiel.store.repositories.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;
import java.util.Set;

@Tag(name = "Users", description = "REST API for managing users.")
@RestController
@AllArgsConstructor
@RequestMapping("users")
public class UserController {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Operation(summary = "Endpoint for fetching users")
    @GetMapping
    public Iterable<UserSummary> getUsers(
            @RequestHeader(required = false, name = "x-auth-token")
            String authToken,
            @RequestParam(required = false, defaultValue = "", name = "sort")
            String sort
    )
    {
        System.out.println(authToken);
        if(!Set.of("name","email").contains(sort))
            sort = "name";

        return userRepository.findAllUsers(Sort.by(sort)).stream().map(userMapper::toDto).toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserSummary> getUser(@PathVariable Long id)
    {
        var user = userRepository.findById(id).orElse(null);
        if(user == null)
        {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(userMapper.toDto(user));
    }

    @PostMapping
    public ResponseEntity<?> createUser(
            @Valid @RequestBody RegisterUserRequest registerUserRequest,
            UriComponentsBuilder uriComponentsBuilder
    )
    {
        if(userRepository.existsByEmail(registerUserRequest.email()))
        {
            return ResponseEntity.unprocessableContent().body(Map.of("email","Email is already registered."));
        }

        var user = userMapper.toEntity(registerUserRequest);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.USER);
        userRepository.save(user);

        var userSummary = userMapper.toDto(user);
        var uri = uriComponentsBuilder.path("/users/{id}").buildAndExpand(userSummary.id()).toUri();

        return ResponseEntity.created(uri).body(userSummary);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserSummary> updateUser(
            @PathVariable Long id,
            @RequestBody UpdateUserRequest updateUserRequest
    )
    {
        var user = userRepository.findById(id).orElse(null);
        if(user == null)
        {
            return ResponseEntity.notFound().build();
        }

        userMapper.update(updateUserRequest,user);
        userRepository.save(user);

        var userSummary = userMapper.toDto(user);
        return ResponseEntity.ok(userSummary);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(
            @PathVariable Long id
    )
    {
        var user = userRepository.findById(id).orElse(null);
        if(user == null)
        {
            return ResponseEntity.notFound().build();
        }
        userRepository.delete(user);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/change-password")
    public ResponseEntity<Void> changePassword(
            @PathVariable Long id,
            @RequestBody ChangePasswordRequest changePasswordRequest
            )
    {
        var user = userRepository.findById(id).orElse(null);
        if(user == null)
        {
            return ResponseEntity.notFound().build();
        }

        if(!user.getPassword().equals(changePasswordRequest.oldPassword()))
        {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        user.setPassword(changePasswordRequest.newPassword());
        userRepository.save(user);

        return ResponseEntity.noContent().build();
    }
}
