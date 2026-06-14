package com.terxiel.store;

import org.springframework.stereotype.Service;

@Service
public class SMSNotificationService implements NotificationService{
    @Override
    public void send(String message, String recipientEmail) {
        System.out.println("Sending SMS...");
        System.out.println("Message: "+message);
    }
}
