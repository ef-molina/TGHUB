package com.example.tailgate.tailgate_backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*") // allow cross-origin for dev
                .withSockJS(); // fallback option
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic", "/queue"); // for broadcasting + user queues
        registry.setApplicationDestinationPrefixes("/app"); // prefix for @MessageMapping
        registry.setUserDestinationPrefix("/user"); // for /user/{id}/queue/notifications
    }
}
