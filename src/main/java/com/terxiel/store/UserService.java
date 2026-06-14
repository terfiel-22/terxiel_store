package com.terxiel.store;

import org.springframework.stereotype.Service;

@Service
public class UserService {
    private  final UserRepository userRepository;
    private  final NotificationService notificationService;

    public  UserService(UserRepository userRepository, NotificationService notificationService)
    {
        this.userRepository = userRepository;
        this.notificationService = notificationService;
    }

    public void registerUser(User user)
    {
        var existingUser = userRepository.findByEmail(user.getEmail());
        if(existingUser!=null)
        {
            throw new IllegalArgumentException("The email provided already exist.");
        }
        userRepository.save(user);
        notificationService.send(user.getName()+", your account is successfully created.",user.getEmail());
    }
}
