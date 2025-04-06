package com.example.tailgate.tailgate_backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class TailgateWebSocketController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    // Client subscribes to: /topic/tailgate.{tailgateId}
    // Backend sends a message here when someone joins
    public void notifyTailgateJoin(Long tailgateId, String username) {
        String destination = "/topic/tailgate." + tailgateId;
        String message = username + " joined tailgate " + tailgateId;
        messagingTemplate.convertAndSend(destination, message);
    }

    @MessageMapping("/tailgate/{id}/send")
    @SendTo("/topic/tailgate/{id}")
    public <ChatMessage> ChatMessage sendToTailgate(@DestinationVariable String id, ChatMessage message) {
        // persist and broadcast
        return message;
    }

}
