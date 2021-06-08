package com.kwetter.postservice.controllers;

import com.kwetter.postservice.entity.Post;
import com.kwetter.postservice.exception.InvalidContentException;
import com.kwetter.postservice.exception.InvalidPostRefenerceExpection;
import com.kwetter.postservice.rabbit.RabbitMQSender;
import com.kwetter.postservice.service.PostService;
import com.kwetter.postservice.service.ProfanityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/post")
@CrossOrigin(origins = {"http://localhost:8080", "*"})
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private RabbitMQSender rabbitMQSender;

    @Autowired
    private ProfanityService profanityService;

    @GetMapping("/getallfromuser/{username}")
    public Iterable<Post> getAllByUser(@PathVariable("username") String username) {
        return postService.getAllByUsername(username);
    }

    @GetMapping("/getallfromuser")
    public Iterable<Post> getAllFromMe(){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return postService.getAllByUsername(userDetails.getUsername());
    }

    @GetMapping("/getpost/{id}")
    public Post getPostById(@PathVariable("id") Long id){
        return postService.getPostById(id)
                .orElseThrow(() -> new InvalidPostRefenerceExpection("Post not found."));
    }

    @PostMapping()
    public Long createPost(@RequestBody Post post) {
        if (post.getContent() == null || post.getContent().length() <= 0){
            throw new InvalidContentException("no content");
        }
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        post.setUsername(userDetails.getUsername());
        post.setCreatedAt(new Date());
        post.setChildFriendly(true);

        postService.savePost(post);

        Thread checkProfanityThread = new Thread(() -> {
            updateProfanity(post);
        });
        checkProfanityThread.start();

        return post.getId();
    }

    @GetMapping("test/{message}")
    public void testRabbitMQ(@PathVariable("message") String message) {
        Post post = new Post();
        post.setId(2L);
        post.setContent(message);
        post.setCreatedAt(new Date());
        post.setChildFriendly(true);
        post.setUsername("nick");

        rabbitMQSender.send(post);
    }

    private void updateProfanity(Post post) {
        Boolean isChildFriendly = profanityService.isContentChildFriendly(post.getContent());
        post.setChildFriendly(isChildFriendly);

        postService.savePost(post);
    }
}
