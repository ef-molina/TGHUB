package com.example.tailgate.tailgate_backend.service;

import com.example.tailgate.tailgate_backend.model.TailgatePost;
import com.example.tailgate.tailgate_backend.repository.TailgatePostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TailgatePostService {

    private final TailgatePostRepository tailgatePostRepository;

    @Autowired
    public TailgatePostService(TailgatePostRepository tailgatePostRepository) {
        this.tailgatePostRepository = tailgatePostRepository;
    }

    public List<TailgatePost> getAllPosts() {
        return tailgatePostRepository.findAll();
    }

    public Optional<TailgatePost> getPostById(Long id) {
        return tailgatePostRepository.findById(id);
    }

    public TailgatePost createPost(TailgatePost post) {
        return tailgatePostRepository.save(post);
    }

    public TailgatePost updatePost(Long id, TailgatePost updatedPost) {
        return tailgatePostRepository.findById(id)
                .map(post -> {
                    post.setTailgateName(updatedPost.getTailgateName());
                    post.setDescription(updatedPost.getDescription());
                    post.setLocation(updatedPost.getLocation());
                    post.setIsPublic(updatedPost.getIsPublic());
                    return tailgatePostRepository.save(post);
                }).orElseThrow(() -> new RuntimeException("Post not found"));
    }

    public void deletePost(Long id) {
        tailgatePostRepository.deleteById(id);
    }
}
