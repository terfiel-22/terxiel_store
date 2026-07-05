package com.terxiel.store.services;

import com.terxiel.store.entities.User;
import com.terxiel.store.exceptions.AuthenticationNotFoundException;
import com.terxiel.store.exceptions.UserNotFoundException;
import com.terxiel.store.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class AuthService {

    private final UserRepository userRepository;

    public User getCurrentUser()
    {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null)
            throw new AuthenticationNotFoundException();
        var userId = (Long) authentication.getPrincipal();
        if(userId == null)
            throw new AuthenticationNotFoundException();
        return userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
    }
}
