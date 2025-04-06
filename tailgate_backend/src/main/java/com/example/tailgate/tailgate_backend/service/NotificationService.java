package com.example.tailgate.tailgate_backend.service;

import com.example.tailgate.tailgate_backend.model.Notification;
import com.example.tailgate.tailgate_backend.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void send(Notification notification) {
        notificationRepository.save(notification);
        String destination = "/user/" + notification.getRecipientId() + "/queue/notifications";
        messagingTemplate.convertAndSend(destination, notification);
    }
}
