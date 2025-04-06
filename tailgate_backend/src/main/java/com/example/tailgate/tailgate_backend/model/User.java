package com.example.tailgate.tailgate_backend.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")  // Matches the table name in MySQL
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private boolean active;

    @Column(name = "email_id") // Maps 'email_id' (DB) to 'emailId' (Java)
    private String emailId;  // Matches column name in DB

    @Column(nullable = false)
    private String password;  // Store securely (hash in real app)


    @Column(name = "user_name")
    private String userName;  // Matches column name in DB

    @Column(name = "new_password")
    private String newPassword;

    /**
     @OneToOne
     @JoinColumn(referencedColumnName = "id", name = "friendsList_id")
     private FriendsList friends;
     **/




    public String getNewPassword() {
        return newPassword;
    }
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public String getEmailId() { return emailId; }
    public void setEmailId(String emailId) { this.emailId = emailId; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }


    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    @JsonManagedReference
    @ManyToMany
    @JoinTable(
            name = "user_tailgates",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "tailgate_id")
    )
    private List<Tailgate> tailgates = new ArrayList<>();

    public List<Tailgate> getTailgates() {
        return tailgates;
    }

    public void setTailgates(List<Tailgate> tailgates) {
        this.tailgates = tailgates;
    }



}
