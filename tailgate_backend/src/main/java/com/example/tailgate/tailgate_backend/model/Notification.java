package com.example.tailgate.tailgate_backend.model;


import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "recipient_id", nullable = false)
    private int recipientId;

    @Column(name = "recipient_role")
    private String recipientRole;

    @Column(name = "sender_id", nullable = false)
    private int senderId;

    @Column(nullable = false)
    private String type;

    @Column(columnDefinition = "TEXT")
    private String message;

    @Column(name = "is_read", nullable = false)
    private boolean isRead = false;

    @Column(name = "created_at", nullable = false, updatable = false,
            columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp createdAt;

    @Column(name = "tailgate_id")
    private Integer tailgateId;

    public Notification() {}

    public Notification(int recipientId, String recipientRole, int senderId, String type,
                        String message, Integer tailgateId) {
        this.recipientId = recipientId;
        this.recipientRole = recipientRole;
        this.senderId = senderId;
        this.type = type;
        this.message = message;
        this.tailgateId = tailgateId;
    }

    // Getters and Setters

    public Long getId() { return id; }

    public int getRecipientId() { return recipientId; }

    public void setRecipientId(int recipientId) { this.recipientId = recipientId; }

    public String getRecipientRole() { return recipientRole; }

    public void setRecipientRole(String recipientRole) { this.recipientRole = recipientRole; }

    public int getSenderId() { return senderId; }

    public void setSenderId(int senderId) { this.senderId = senderId; }

    public String getType() { return type; }

    public void setType(String type) { this.type = type; }

    public String getMessage() { return message; }

    public void setMessage(String message) { this.message = message; }

    public boolean isRead() { return isRead; }

    public void setRead(boolean read) { isRead = read; }

    public Timestamp getCreatedAt() { return createdAt; }

    public Integer getTailgateId() { return tailgateId; }

    public void setTailgateId(Integer tailgateId) { this.tailgateId = tailgateId; }
}
