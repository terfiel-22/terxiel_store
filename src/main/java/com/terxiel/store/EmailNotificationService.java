package com.terxiel.store;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@Primary
public class EmailNotificationService implements  NotificationService{

    @Value("${mail.host}")
    private String host;

    @Value("${mail.port}")
    private int port;

    @Override
    public void send(String message, String recipientEmail) {
        System.out.println("Sending email...");
        System.out.println("Recipient: "+recipientEmail);
        System.out.println("HOST: "+host);
        System.out.println("PORT: "+port);
        System.out.println("Message: "+message);
    }
}
