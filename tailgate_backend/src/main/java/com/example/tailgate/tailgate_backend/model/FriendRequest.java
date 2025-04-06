package com.example.tailgate.tailgate_backend.model;


import jakarta.persistence.*;

@Entity
public class FriendRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User sender;

    @ManyToOne
    private User receiver;

    @Enumerated(EnumType.STRING)
    private RequestStatus status;

    public enum RequestStatus {
        PENDING,
        ACCEPTED,
        DECLINED
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getSender() { return sender;}
    public void setSender(User user) { sender = user; }

    public User getReceiver() { return receiver;}
    public void setReceiver(User user) { receiver = user; }

    public RequestStatus getStatus() { return status; }
    public void setStatus(RequestStatus status) { this.status = status;}
}
