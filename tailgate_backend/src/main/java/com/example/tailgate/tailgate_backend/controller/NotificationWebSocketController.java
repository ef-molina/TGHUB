package com.example.tailgate.tailgate_backend.controller;



import com.example.tailgate.tailgate_backend.model.Notification;
import com.example.tailgate.tailgate_backend.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
public class NotificationWebSocketController {

    @Autowired
    private NotificationService notificationService;

    @MessageMapping("/notify")
    public void sendNotification(Notification notification) {
        notificationService.send(notification);
    }
}
