package com.example.tailgate.tailgate_backend.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "tailgate_posts")
@NoArgsConstructor // Generates an empty constructor (default values handled separately)
@Getter
@Setter
public class TailgatePost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String tailgateName;
    private String description;
    private String location;
    private LocalDateTime createdAt;

    @JsonProperty("isPublic")
    private Boolean isPublic = true;
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        if (this.isPublic == null) {
            this.isPublic = true;
        }
    }

    public TailgatePost(String tailgateName, String description, String location, Boolean isPublic) {
        this.tailgateName = tailgateName;
        this.description = description;
        this.location = location;
        this.isPublic = (isPublic != null) ? isPublic : true;
        this.createdAt = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "TailgatePost{" +
                "id=" + id +
                ", tailgateName='" + tailgateName + '\'' +
                ", description='" + description + '\'' +
                ", location='" + location + '\'' +
                ", createdAt=" + createdAt +
                ", isPublic=" + isPublic +
                '}';
    }
}
