package com.terxiel.store.controllers;

import com.terxiel.store.dtos.UserSummary;
import com.terxiel.store.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("users")
public class UserController {
    private final UserRepository userRepository;

    @GetMapping
    public Iterable<UserSummary> getUsers()
    {
        return userRepository.findAllUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserSummary> getUser(@PathVariable Long id)
    {
        var user = userRepository.findUserById(id);
        if(user==null)
        {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(user);
    }
}
