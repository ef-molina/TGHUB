package com.example.tailgate.tailgate_backend.repository;

import com.example.tailgate.tailgate_backend.model.Message;
import com.example.tailgate.tailgate_backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface MessagesRepository extends JpaRepository<Message, Long> {

    Message findById(int id);

    void deleteAllByUser(User user);

    @Transactional
    void deleteById(long id);

    @Query("SELECT m FROM Message m WHERE m.user.userName = :userName AND m.user.password = :password")
    Iterable <Message> findAllMessagesByUser(String userName, String password);

    Optional<Message> findMessageByMessageBodyAndUser(String messageBody, User user);


}