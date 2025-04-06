package com.example.tailgate.tailgate_backend.controller;


import com.example.tailgate.tailgate_backend.model.Tailgate;
import com.example.tailgate.tailgate_backend.model.User;
import com.example.tailgate.tailgate_backend.repository.TailgateRepository;
import com.example.tailgate.tailgate_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/user-tailgates")
public class UserTailgateController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TailgateRepository tailgateRepository;

    // User joins a tailgate
    @PostMapping("/join")
    public ResponseEntity<String> joinTailgate(@RequestParam Long userId, @RequestParam int tailgateId) {
        Optional<User> userOpt = userRepository.findById(userId);
        Optional<Tailgate> tailgateOpt = tailgateRepository.findById(tailgateId);

        if (userOpt.isEmpty() || tailgateOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User or tailgate not found");
        }

        User user = userOpt.get();
        Tailgate tailgate = tailgateOpt.get();

        if (!user.getTailgates().contains(tailgate)) {
            user.getTailgates().add(tailgate);
            userRepository.save(user);
        }

        return ResponseEntity.ok("User joined tailgate successfully");
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getTailgatesForUser(@PathVariable Long userId) {
        return userRepository.findById(userId)
                .map(user -> ResponseEntity.ok(user.getTailgates()))
                .orElseGet(() ->
                        ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body((java.util.List<Tailgate>) Map.of("error", "User not found"))
                );
    }



    @GetMapping("/tailgate/{tailgateId}")
    public ResponseEntity<?> getUsersForTailgate(@PathVariable int tailgateId) {
        return tailgateRepository.findById(tailgateId)
                .map(tailgate -> ResponseEntity.ok(tailgate.getUsers()))
                .orElseGet(() ->
                        ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body((java.util.List<User>) Map.of("error", "Tailgate not found"))
                );
    }

    @DeleteMapping("/leave")
    public ResponseEntity<?> leaveTailgate(@RequestParam Long userId, @RequestParam int tailgateId) {
        Optional<User> userOpt = userRepository.findById(userId);
        Optional<Tailgate> tailgateOpt = tailgateRepository.findById(tailgateId);

        if (userOpt.isEmpty() || tailgateOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "User or Tailgate not found"));
        }

        User user = userOpt.get();
        Tailgate tailgate = tailgateOpt.get();

        if (!user.getTailgates().contains(tailgate)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "User is not joined to this tailgate"));
        }

        user.getTailgates().remove(tailgate);
        userRepository.save(user);

        return ResponseEntity.ok(Map.of("message", "User left the tailgate"));
    }



}

