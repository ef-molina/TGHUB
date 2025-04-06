package com.example.tailgate.tailgate_backend.controller;

import com.example.tailgate.tailgate_backend.model.Message;
import com.example.tailgate.tailgate_backend.model.User;
import com.example.tailgate.tailgate_backend.repository.MessagesRepository;
import com.example.tailgate.tailgate_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.json.JSONObject;
import java.util.Optional;


@RestController
@RequestMapping("/messages")
public class MessageController {



    @Autowired
    private MessagesRepository messagesRepository;

    @Autowired
    private UserRepository userRepository;


    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";

    @PostMapping
    ResponseEntity<?> newMessage(@RequestParam String userName, @RequestParam String password, @RequestBody JSONObject msg){
        String message = msg.getString("msg");

        Optional<User> userOptional = userRepository.findByUserNameAndPassword(userName, password);

        Message newMessage = new Message();
        if (msg == null ||  !userOptional.isPresent() ){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(failure);
        }
        newMessage.setMessageBody(message);
        newMessage.setUser(userOptional.get());
        newMessage.setCreationTime();
        messagesRepository.save(newMessage);
        return ResponseEntity.status(HttpStatus.CREATED).body(message);
    }

    @GetMapping(path = "/user")
    public Iterable<Message> getUserMessages(@RequestParam String userName, @RequestParam String password){

        return messagesRepository.findAllMessagesByUser(userName, password);

    }


    @PutMapping
    public ResponseEntity<?> updateMessage(@RequestParam String userName, @RequestParam String password, @RequestParam String msg, @RequestBody JSONObject newMsg){
        String newMessage = newMsg.getString("newMsg");

        Optional<User> user = userRepository.findByUserNameAndPassword(userName, password);
       ///**
        if (!user.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(failure);
        }
        //**/
        Optional<Message> messageSpecific = messagesRepository.findMessageByMessageBodyAndUser(msg, user.get());
        if (!messageSpecific.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(failure);
        }

        if (newMsg != null) {
            messageSpecific.get().setMessageBody(newMessage);
        }
            messagesRepository.save(messageSpecific.get());
            return ResponseEntity.status(HttpStatus.OK).body(success);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteMessage(@RequestParam String userName, @RequestParam String password, @RequestParam String msg){
        Optional<User> user = userRepository.findByUserNameAndPassword(userName, password);
        if (!user.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(failure);
        }

        Optional<Message> message = messagesRepository.findMessageByMessageBodyAndUser(msg, user.get());
        messagesRepository.deleteById(message.get().getId());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(success);
    }



}
