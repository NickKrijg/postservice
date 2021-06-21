package com.kwetter.postservice.rabbit;

import com.kwetter.postservice.entity.Post;
import com.kwetter.postservice.service.PostService;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@EnableScheduling
public class RabbitMQSender {

    @Autowired
    private AmqpTemplate rabbitTemplate;

    @Autowired
    private PostService postService;

    private String exchange = RabbitMQConfig.topicExchangeName;

    private String postRoutingKey = RabbitMQConfig.postRoutingKey;
    private String feedRoutingKey = RabbitMQConfig.feedRoutingKey;

    public void send(Post post) {
        rabbitTemplate.convertAndSend(exchange, postRoutingKey, post);
        System.out.println("Send msg = " + post);
    }

    @Scheduled(initialDelay = 0, fixedRate = (1000 * 60)) // every minute
    public void updateNewestPosts() {
        Iterable<Post> posts = postService.getTop10();
        rabbitTemplate.convertAndSend(exchange, feedRoutingKey, posts);
    }

}
