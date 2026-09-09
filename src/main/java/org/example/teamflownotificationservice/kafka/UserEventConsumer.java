package org.example.teamflownotificationservice.kafka;

import org.example.teamflownotificationservice.event.UserRegisteredEvent;
import org.example.teamflownotificationservice.service.NotificationService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class UserEventConsumer {

    private final NotificationService notificationService;

    public UserEventConsumer(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @KafkaListener(
            topics = "user-events",
            groupId = "notification-service-v2"
    )
    public void handleUserRegisteredEvent(UserRegisteredEvent event) {
        notificationService.processUserRegisteredEvent(event);
    }
}