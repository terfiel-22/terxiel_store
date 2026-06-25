package com.terxiel.store.controllers;

import com.terxiel.store.dtos.UserSummary;
import com.terxiel.store.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("users")
public class UserController {
    private final UserRepository userRepository;

    @GetMapping
    public List<UserSummary> getUsers()
    {
        return userRepository.findAll().stream().map(u->new UserSummary(u.getId(),u.getEmail(),u.getName())).toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserSummary> getUser(@PathVariable Long id)
    {
        var user = userRepository.findById(id).orElse(null);
        if(user == null)
        {
            return ResponseEntity.notFound().build();
        }
        var userSummary = new UserSummary(user.getId(), user.getEmail(), user.getName());
        return ResponseEntity.ok(userSummary);
    }
}
