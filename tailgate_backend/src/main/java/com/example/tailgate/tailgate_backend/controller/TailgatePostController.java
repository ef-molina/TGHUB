package com.example.tailgate.tailgate_backend.controller;

import com.example.tailgate.tailgate_backend.model.TailgatePost;
import com.example.tailgate.tailgate_backend.service.TailgatePostService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tailgate-posts")
public class TailgatePostController {

    private final TailgatePostService service;

    public TailgatePostController(TailgatePostService service) {
        this.service = service;
    }

    @GetMapping
    public List<TailgatePost> getAllPosts() {
        return service.getAllPosts();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TailgatePost> getPostById(@PathVariable Long id) {
        Optional<TailgatePost> post = service.getPostById(id);
        return post.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<TailgatePost> createPost(@RequestBody TailgatePost post) {
        System.out.println("Received JSON payload: " + post);

        TailgatePost createdPost = service.createPost(post);
        return ResponseEntity.ok(createdPost);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TailgatePost> updatePost(@PathVariable Long id, @RequestBody TailgatePost updatedPost) {
        try {
            TailgatePost post = service.updatePost(id, updatedPost);
            return ResponseEntity.ok(post);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        service.deletePost(id);
        return ResponseEntity.noContent().build();
    }
}
