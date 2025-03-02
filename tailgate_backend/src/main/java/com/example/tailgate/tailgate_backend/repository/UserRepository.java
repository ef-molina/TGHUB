package com.example.tailgate.tailgate_backend.repository;

import com.example.tailgate.tailgate_backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    /** This allows us to interact with the repository */
    Optional<User> findByEmail(String email);

    Optional<User> findByUsernameAndPassword(String username, String password);
}
