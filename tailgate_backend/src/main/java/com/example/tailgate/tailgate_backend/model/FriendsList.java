package com.example.tailgate.tailgate_backend.model;

import jakarta.persistence.*;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;


@Entity
@Table(name = "FriendsLists")
public class FriendsList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int size;


    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    @ManyToMany
    @JoinTable(name = "friendslists_users",
            joinColumns = @JoinColumn(name = "friendslist_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> friends;





    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public int getSize() { return size; }
    public void setSize(int size) { this.size = size; }

    public User getUser() { return user; }
    public void setUser(User user){ this.user = user; }

    public User[] getFriends() {
        User[] friendsList; //= new User[size];
        friendsList = friends.toArray(new User[size]);
        return friendsList;
    }
    public Set<User> getFriendsSet(){
        return friends;
    }
    public void setFriends(Set<User> friends) { this.friends = friends;}
    public void addFriend(User friend) {
        friends.add(friend);
        //System.out.println(this.getFriendsSet());
        size = size +1;
    }
    public void deleteFriend(User friend){
        friends.remove(friend);
        size = size -1;
    }

    public boolean friendsListContains(User user){
        return friends.contains(user);
    }

    public void trimNulls(){
        friends.removeAll(Collections.singleton(null));
        setSize(friends.size());
    }
}

