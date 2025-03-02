package com.example.tailgate.tailgate_backend.repository;

import com.example.tailgate.tailgate_backend.model.TailgatePost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TailgatePostRepository extends JpaRepository<TailgatePost, Long> {
    List<TailgatePost> findByTailgateNameContainingIgnoreCase(String tailgateName);
}
