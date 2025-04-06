package com.example.tailgate.tailgate_backend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tailgate")
public class Tailgate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-increment ID
    private int id;

    @Column(name = "tailgate_name", nullable = false, unique = true)
    private String tailgateName;

    @Column(name = "location")
    private String location;

    @Column(name = "description")
    private String description;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "is_public")
    private boolean isPublic;

    @Column(name = "start_time")
    private LocalTime startTime;

    @Column(name = "end_time")
    private LocalTime endTime;


    @Column(name = "max_attendees")
    private Integer maxAttendees;

    @Column(name = "current_attendees")
    private Integer currentAttendees;

    @Column(name = "food_provided")
    private boolean foodProvided;

    @Column(name = "can_edit")
    private boolean canEdit;

    // Default constructor (required by JPA)
    public Tailgate() {}

    // Constructor for quick initialization
    public Tailgate(String tailgateName, String location, LocalDate date, String description,
                    LocalTime startTime, LocalTime endTime, boolean isPublic,
                    Integer maxAttendees, Integer currentAttendees, boolean foodProvided, boolean canEdit) {
        this.tailgateName = tailgateName;
        this.location = location;
        this.date = date;
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
        this.isPublic = isPublic;
        this.maxAttendees = maxAttendees;
        this.currentAttendees = currentAttendees;
        this.foodProvided = foodProvided;
        this.canEdit = canEdit;
    }

    // Getter methods
    public int getId() {
        return id;
    }

    public String getTailgateName() {
        return tailgateName;
    }

    public String getLocation() {
        return location;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public Integer getMaxAttendees() {
        return maxAttendees;
    }

    public Integer getCurrentAttendees() {
        return currentAttendees;
    }

    public boolean isFoodProvided() {
        return foodProvided;
    }

    public boolean isCanEdit() {
        return canEdit;
    }

    // Setter methods
    public void setTailgateName(String tailgateName) {
        this.tailgateName = tailgateName;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }

    public void setMaxAttendees(Integer maxAttendees) {
        this.maxAttendees = maxAttendees;
    }

    public void setCurrentAttendees(Integer currentAttendees) {
        this.currentAttendees = currentAttendees;
    }

    public void setFoodProvided(boolean foodProvided) {
        this.foodProvided = foodProvided;
    }

    public void setCanEdit(boolean canEdit) {
        this.canEdit = canEdit;
    }

    @JsonBackReference
    @ManyToMany(mappedBy = "tailgates")
    private List<User> users = new ArrayList<>();

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

}

