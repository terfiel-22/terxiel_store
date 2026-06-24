package com.terxiel.store.controllers;

import com.terxiel.store.dtos.UserSummary;
import com.terxiel.store.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

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
    public Optional<UserSummary> getUser(@PathVariable Long id)
    {
        return userRepository.findUserById(id);
    }
}
