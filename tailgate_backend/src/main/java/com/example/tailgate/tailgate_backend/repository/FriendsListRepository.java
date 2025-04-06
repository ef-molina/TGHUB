package com.example.tailgate.tailgate_backend.repository;

import com.example.tailgate.tailgate_backend.model.FriendsList;
import com.example.tailgate.tailgate_backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;


public interface FriendsListRepository extends JpaRepository<FriendsList, Long> {

    FriendsList findByUser(User user);

    @Transactional
    void deleteByUser(User user);


}

