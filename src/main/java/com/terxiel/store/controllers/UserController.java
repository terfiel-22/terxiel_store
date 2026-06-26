package com.terxiel.store.controllers;

import com.terxiel.store.dtos.UserSummary;
import com.terxiel.store.mappers.UserMapper;
import com.terxiel.store.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
