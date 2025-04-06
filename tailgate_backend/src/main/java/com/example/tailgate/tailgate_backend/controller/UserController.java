package com.example.tailgate.tailgate_backend.controller;

import com.example.tailgate.tailgate_backend.model.User;
import com.example.tailgate.tailgate_backend.repository.FriendsListRepository;
import com.example.tailgate.tailgate_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;


@RestController
@RequestMapping("/users") // Base URL
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FriendsListRepository friendsListRepository;

    // GET User by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        Optional<User> user = userRepository.findById(id);
        return user.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // GET User by Username and Password
    @GetMapping("/login")
    public ResponseEntity<?> getUserByUsernameAndPassword(
            @RequestParam String userName, @RequestParam String password) {
        Optional<User> user = userRepository.findByUserNameAndPassword(userName, password);

        return user.map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    // PUT (Update) User Info by ID
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
        return userRepository.findById(id).map(user -> {
            user.setUserName(updatedUser.getUserName());
            user.setEmailId(updatedUser.getEmailId());
            user.setActive(updatedUser.isActive());
            if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
                user.setPassword(updatedUser.getPassword());
            }
            userRepository.save(user);
            return ResponseEntity.ok(user);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }


    // PUT (Update) User Info by Email or Username
    // PUT (Update) User Info by Email or Username, including password change
    @PutMapping
    public ResponseEntity<?> updateUserByEmailOrUsername(
            @RequestBody User updatedUser) {

        String userName = updatedUser.getUserName();
        String emailId = updatedUser.getEmailId();
        String password = updatedUser.getPassword();
        String newPassword = updatedUser.getNewPassword();

        System.out.println("userName: " + userName);
        System.out.println("emailId: " + emailId);
        System.out.println("password: " + password);
        System.out.println("newPassword: " + newPassword);


        Optional<User> existingUser = userRepository.findByEmailIdOrUserName(emailId, userName);

        if (existingUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"message\":\"User not found\"}");
        }

        User user = existingUser.get();

        // Update only if new values are provided
        if (updatedUser.getUserName() != null && !updatedUser.getUserName().isEmpty()) {
            user.setUserName(updatedUser.getUserName());
        }
        if (updatedUser.getEmailId() != null && !updatedUser.getEmailId().isEmpty()) {
            user.setEmailId(updatedUser.getEmailId());
        }

        // update password if new password is provided
        if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
            user.setPassword(newPassword); // Change password too
        }

        userRepository.save(user);
        return ResponseEntity.ok("{\"message\":\"User updated successfully\"}");
    }





    // GET request to fetch all users
    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";

    // POST Method to create new User
    @PostMapping
    public String createUser(@RequestBody User user){
        if (user == null)
            return failure;
        userRepository.save(user);
        return success;
    }

    // DELETE Method for User deletion by user id
    @DeleteMapping(path = "/{id}")
    public String deleteUserById(@PathVariable int id){
        User user = userRepository.findById(id);
        friendsListRepository.deleteByUser(user);
        userRepository.deleteById(id);
        return success;
    }

    @DeleteMapping(path = "/delete")
    String deleteUserByEmailOrUsername(@RequestParam String emailId, @RequestParam String userName){
        userRepository.deleteByEmailIdOrUserName(emailId, userName);
        return success;
    }
}

