package com.example.tailgate.tailgate_backend.controller;

import com.example.tailgate.tailgate_backend.model.FriendRequest;
import com.example.tailgate.tailgate_backend.model.FriendsList;
import com.example.tailgate.tailgate_backend.model.User;
import com.example.tailgate.tailgate_backend.repository.FriendRequestRepository;
import com.example.tailgate.tailgate_backend.repository.FriendsListRepository;
import com.example.tailgate.tailgate_backend.repository.UserRepository;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static com.example.tailgate.tailgate_backend.model.FriendRequest.RequestStatus.*;


@RestController
public class FriendsController {

    @Autowired
    private FriendsListRepository friendsListRepository;

    @Autowired
    private FriendRequestRepository friendRequestRepository;

    @Autowired
    private UserRepository userRepository;


    // FriendsList Methods

    @PostMapping("/{email}/friendList")
    public ResponseEntity<?> newFriendsList (@PathVariable String email){
        Optional<User> userOptional = userRepository.findByEmailIdOrUserName(email, null);
        if (!userOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        if (friendsListRepository.findByUser(userOptional.get()) != null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User already has a FriendsList");
        }
        else {
            FriendsList list = new FriendsList();
            Set<User> friends = new HashSet<>();

            list.setUser(userOptional.get());
            list.setFriends(friends);
            list.setSize(0);
            friendsListRepository.save(list);
            return ResponseEntity.status(HttpStatus.OK).body("Success");
        }
    }

    @GetMapping("/{email}/friendList")
    public ResponseEntity<?> getFriendsListForUser (@PathVariable String email) {
        Optional<User> userOptional = userRepository.findByEmailIdOrUserName(email, null);

        FriendsList friendsList = friendsListRepository.findByUser(userOptional.get());
        if (friendsList == null){
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body("User has no created friendsList");
        }
        //System.out.println(friendsList.getSize());
        if (friendsList.getSize() == 0){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User has no friends");
        }
        else if (friendsList.getSize() <= 0){
            friendsList.setSize(0);
            friendsListRepository.save(friendsList);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User has no friends");
        }
        friendsList.trimNulls();
        User[] friends = friendsList.getFriends();
        Iterable<User> list = Arrays.asList(friends);
        return ResponseEntity.status(HttpStatus.OK).body(list);

    }

    @DeleteMapping("/{email}/friendList")
    public ResponseEntity<?> deleteFriendsListForUser (@PathVariable String email){
        Optional<User> userOptional = userRepository.findByEmailIdOrUserName(email, null);
        if (!userOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        friendsListRepository.deleteByUser(userOptional.get());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Success");
    }

    /*
    @PutMapping("/{email}/friendList/{friendEmail}")
    public ResponseEntity<?> addNewFriend (@PathVariable String email, @PathVariable String friendEmail){
        Optional<User> userOptional = userRepository.findByEmailIdOrUserName(email, null);
        Optional<User> friendOptional = userRepository.findByEmailIdOrUserName(friendEmail, null);
        if (!userOptional.isPresent() || !friendOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        FriendsList friendsList = friendsListRepository.findByUser(userOptional.get());

        if(friendsList.friendsListContains(friendOptional.get())){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("failure to add to list, Friend already added");
        }


        friendsList.addFriend(friendOptional.get());
        friendsListRepository.save(friendsList);
        return ResponseEntity.status(HttpStatus.CREATED).body("Friend added");
    }
    */


    @DeleteMapping("/{email}/friendList/{friendEmail}")
    public ResponseEntity<?> UnaddFriend (@PathVariable String email, @PathVariable String friendEmail){
        Optional<User> userOptional = userRepository.findByEmailIdOrUserName(email, null);
        Optional<User> friendOptional = userRepository.findByEmailIdOrUserName(friendEmail, null);
        if (!userOptional.isPresent() || !friendOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        FriendsList userFriendsList = friendsListRepository.findByUser(userOptional.get());
        FriendsList friendFriendsList = friendsListRepository.findByUser(friendOptional.get());

        userFriendsList.deleteFriend(friendOptional.get());
        friendFriendsList.deleteFriend(userOptional.get());

        friendsListRepository.save(userFriendsList);
        friendsListRepository.save(friendFriendsList);

        FriendRequest request = friendRequestRepository.findBySenderAndReceiver(userOptional.get(), friendOptional.get());
        if (request == null){
            request = friendRequestRepository.findBySenderAndReceiver(friendOptional.get(), userOptional.get());
        }

        friendRequestRepository.delete(request);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Success");
    }





    // FriendRequest Methods


    @PostMapping("/{email}/friendRequest/{friendEmail}/sendRequest")
    public ResponseEntity<?> sendFriendRequest (@PathVariable String email, @PathVariable String friendEmail){
        Optional<User> userOptional = userRepository.findByEmailIdOrUserName(email, null);
        Optional<User> friendOptional = userRepository.findByEmailIdOrUserName(friendEmail, null);

        if (friendRequestRepository.existsBySenderAndReceiver(userOptional.get(), friendOptional.get())
                | friendRequestRepository.existsBySenderAndReceiver(friendOptional.get(), userOptional.get())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Friend Request already sent");
        }
        FriendRequest request = new FriendRequest();

        request.setSender(userOptional.get());
        request.setReceiver(friendOptional.get());
        request.setStatus(PENDING);

        friendRequestRepository.save(request);
        return ResponseEntity.status(HttpStatus.CREATED).body("Friend Request sent to " + friendOptional.get().getUserName());
    }

    @PutMapping("/{email}/friendRequest/{friendEmail}/accept")
    public ResponseEntity<?> acceptFriendRequest (@PathVariable String email, @PathVariable String friendEmail) {
        Optional<User> userOptional = userRepository.findByEmailIdOrUserName(email, null);
        Optional<User> friendOptional = userRepository.findByEmailIdOrUserName(friendEmail, null);

        if (!friendRequestRepository.existsBySenderAndReceiver(friendOptional.get(), userOptional.get())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No Friend Request ever made");
        }
        FriendRequest request = friendRequestRepository.findBySenderAndReceiver(friendOptional.get(), userOptional.get());

        if (request.getStatus() != PENDING) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No Friend Request pending to accept");
        } else {
            request.setStatus(ACCEPTED);
            friendRequestRepository.save(request);
            FriendsList userFriendsList = friendsListRepository.findByUser(userOptional.get());
            FriendsList friendFriendsList = friendsListRepository.findByUser(friendOptional.get());

            userFriendsList.addFriend(friendOptional.get());
            friendFriendsList.addFriend(userOptional.get());
            friendsListRepository.save(userFriendsList);
            friendsListRepository.save(friendFriendsList);
            return ResponseEntity.status(HttpStatus.OK).body("You accepted " + friendOptional.get().getUserName() + "'s friend request.");
        }
    }

    @DeleteMapping("/{email}/friendRequest/{friendEmail}/decline")
    public ResponseEntity<?> declineFriendRequest (@PathVariable String email, @PathVariable String friendEmail) {
        Optional<User> userOptional = userRepository.findByEmailIdOrUserName(email, null);
        Optional<User> friendOptional = userRepository.findByEmailIdOrUserName(friendEmail, null);

        if (!friendRequestRepository.existsBySenderAndReceiver(friendOptional.get(), userOptional.get())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No Friend Request ever made");
        }
        FriendRequest request = friendRequestRepository.findBySenderAndReceiver(friendOptional.get(), userOptional.get());

        if (request.getStatus() != PENDING) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No Friend Request pending to decline");
        }
        else{
            request.setStatus(DECLINED);
            friendRequestRepository.save(request);

            return ResponseEntity.status(HttpStatus.OK).body("You declined " + friendOptional.get().getUserName() + "'s friend request.");
        }

    }

    @GetMapping("/{email}/friendRequests")
    public ResponseEntity<?> getFriendRequests (@PathVariable String email) {
        Optional<User> userOptional = userRepository.findByEmailIdOrUserName(email, null);

        ArrayList<FriendRequest> requests = friendRequestRepository.findByReceiver(userOptional.get());


        if (requests.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("You have no friend requests");
        }

        return ResponseEntity.status(HttpStatus.OK).body(requests.toArray());

    }

    @GetMapping("/{email}/friendRequests/pending")
    public ResponseEntity<?> getPendingFriendRequests (@PathVariable String email) {
        Optional<User> userOptional = userRepository.findByEmailIdOrUserName(email, null);

        ArrayList<FriendRequest> requests = friendRequestRepository.findByReceiver(userOptional.get());
        if (requests.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("You have no pending friend requests");
        }
        ArrayList<FriendRequest> pendingRequests = new ArrayList<>();
        for (int i = 0; i < requests.size(); ++i) {
            if (requests.get(i).getStatus() == PENDING) {
                pendingRequests.add(requests.get(i));
            }
        }

        return ResponseEntity.status(HttpStatus.OK).body(pendingRequests.toArray());
    }

}
