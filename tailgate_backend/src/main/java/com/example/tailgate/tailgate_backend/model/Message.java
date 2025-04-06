package com.example.tailgate.tailgate_backend.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "Messages")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne ()
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    private String messageBody;


    private String creationTime;


    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user){ this.user = user; }

    public String getMessageBody() { return messageBody; }
    public void setMessageBody(String message) { this.messageBody = message; }

    public String getCreationTime(){ return creationTime;}
    /**
    public String getCreationTimeFormatted(){
        DateTimeFormatter formatItem = DateTimeFormatter.ofPattern("dd-MM-yyyy  HH:mm:ss");
        return creationTime.format(formatItem);
    }
     **/
    public void setCreationTime(){
        DateTimeFormatter formatItem = DateTimeFormatter.ofPattern("yyyy-MM-dd  HH:mm:ss");
        LocalDateTime time = LocalDateTime.now();
        creationTime = time.format(formatItem);
    }


}



