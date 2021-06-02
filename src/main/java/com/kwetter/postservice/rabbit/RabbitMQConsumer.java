package com.kwetter.postservice.rabbit;

import com.kwetter.postservice.entity.Post;
import com.kwetter.postservice.rabbit.RabbitMQConfig;
import com.kwetter.postservice.service.PostService;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQConsumer {

    @Autowired
    private PostService postService;

    @RabbitListener(queues = RabbitMQConfig.queuePost)
    public void receivedMessages(Post post) {
        System.out.println("Received: " + post.toString());
    }

    @RabbitListener(queuesToDeclare = @Queue(name = RabbitMQConfig.queueForget, durable = "false"))
    public void receivedForget(String username) {
        System.out.println("Forget: received " + username);
        postService.deleteAllByUsername(username);
    }
}
