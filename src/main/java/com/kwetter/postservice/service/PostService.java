package com.kwetter.postservice.service;

import com.kwetter.postservice.entity.Post;
import com.kwetter.postservice.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    public void savePost(Post post){
        postRepository.save(post);
    }

    public Optional<Post> getPostById(Long id){
        return postRepository.findById(id);
    }

    public Iterable<Post> getAllByUsername(String username) {
        return postRepository.findAllByUsername(username);
    }

    public void deleteById(Long id) {
        postRepository.deleteById(id);
    }

    @Transactional
    public Integer deleteAllByUsername(String username) {
        return postRepository.deleteAllByUsername(username);
    }
}
