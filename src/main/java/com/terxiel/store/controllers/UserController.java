package com.terxiel.store.controllers;

import com.terxiel.store.dtos.RegisterUserRequest;
import com.terxiel.store.dtos.UpdateUserRequest;
import com.terxiel.store.dtos.UserSummary;
import com.terxiel.store.entities.User;
import com.terxiel.store.mappers.UserMapper;
import com.terxiel.store.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Set;

@RestController
@AllArgsConstructor
@RequestMapping("users")
public class UserController {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

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
    public ResponseEntity<UserSummary> createUser(
            @RequestBody RegisterUserRequest registerUserRequest,
            UriComponentsBuilder uriComponentsBuilder
    )
    {
        var user = userMapper.toEntity(registerUserRequest);

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
}
