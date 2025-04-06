package com.example.tailgate.tailgate_backend.repository;

import com.example.tailgate.tailgate_backend.model.Tailgate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TailgateRepository extends JpaRepository<Tailgate, Integer> {
    Optional<Tailgate> findByTailgateName(String tailgateName);

    Optional<Tailgate> findByTailgateNameIgnoreCase(String tailgateName); // Case insensitive search
}

