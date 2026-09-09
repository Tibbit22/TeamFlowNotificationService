package org.example.teamflownotificationservice.service;

import org.example.teamflownotificationservice.event.UserRegisteredEvent;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    public void processUserRegisteredEvent(UserRegisteredEvent event) {
        System.out.println(
                "Sending notification to: " + event.getEmail()
                        + " for user: " + event.getUsername()
        );
    }
}