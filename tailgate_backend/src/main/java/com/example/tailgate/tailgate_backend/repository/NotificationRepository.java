package com.example.tailgate.tailgate_backend.repository;


import com.example.tailgate.tailgate_backend.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByRecipientId(int recipientId);
}
