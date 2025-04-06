package com.example.tailgate.tailgate_backend.repository;

import com.example.tailgate.tailgate_backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    User findById(int id);

    @Transactional
    void deleteById(long id);

    Optional<User> findByUserNameAndPassword(String userName, String password);

    Optional<User> findByEmailIdOrUserName(String emailId, String userName);

    @Transactional
    void deleteByEmailIdOrUserName(String emailId, String userName);
}


