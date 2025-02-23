package com.example.tailgate.tailgate_backend.controller;

import com.example.tailgate.tailgate_backend.model.User;
import com.example.tailgate.tailgate_backend.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * This class is a Spring REST controller that handles user registration and fetching all users
 * It includes endpoints for user registration and fetching all users
 * It uses Spring's @RestController and @RequestMapping annotations to define the base URL for the endpoints
 *
 * Endpoint: /api/users/register
 * Method: POST
 * Request Body: User object
 * Response: Success message
 *
 * Endpoint: /api/users
 * Method: GET
 * Response: List of all users
 * */

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository userRepository;

    // Constructor injection for UserRepository
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody User user) {
        System.out.println("Received User: " + user);

        // Check if the user exists in the database
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "User logged in successfully"));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of(
                            "success", false,
                            "error", "Invalid email or password"
                    ));
        }
    }


    /** POSTS */
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        System.out.println("Received User: " + user);

        // if there is no is already a user with the same email
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of(
                            "success", false,
                            "error", "Email already in use"
                    ));
        }

        try {
            userRepository.save(user);
            return ResponseEntity.ok(Map.of(
                        "success", true,
                    "message", "User registered successfully"));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false,
                            "error", "Error registering user: " + e.getMessage()));
        }

    }
    /** DEL */
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteUser(@RequestHeader("username") String username,
                                        @RequestHeader("email") String email,
                                        @RequestHeader("password") String password){

        System.out.println("Received User: " + username + " " + email);

        // Find user in the database by email
        Optional<User> existingUser = userRepository.findByEmail(email);

        // Check if the user exists and the password matches
        if (existingUser.isPresent()){

            // Check if the username, password, and email match
            if (existingUser.get().getUsername().equals(username) &&
                existingUser.get().getPassword().equals(password) &&
                existingUser.get().getEmail().equals(email)) {

                userRepository.delete(existingUser.get());
                return ResponseEntity.ok(Map.of("success", true, "message", "User deleted successfully"));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("success", false, "error", "Invalid User Credentials"));
            }

        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("success", false, "error", "User not found"));
        }

    }


    /** GETS */

    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }



}
