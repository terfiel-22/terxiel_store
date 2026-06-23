package com.terxiel.store.controllers;

import com.terxiel.store.dtos.UserSummary;
import com.terxiel.store.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class UserController {
    private final UserRepository userRepository;

    @GetMapping("/users")
    public Iterable<UserSummary> getAllUsers()
    {
        return userRepository.findAllUsers();
    }
}
