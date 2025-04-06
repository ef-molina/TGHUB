package com.example.tailgate.tailgate_backend.repository;


import com.example.tailgate.tailgate_backend.model.FriendRequest;
import com.example.tailgate.tailgate_backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;

public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {


    boolean existsBySenderAndReceiver(User sender, User receiver);

    FriendRequest findBySenderAndReceiver(User sender, User receiver);

    ArrayList<FriendRequest> findByReceiver(User receiver);

}

