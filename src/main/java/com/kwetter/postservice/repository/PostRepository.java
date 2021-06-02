package com.kwetter.postservice.repository;

import com.kwetter.postservice.entity.Post;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends CrudRepository<Post, Long> {
    Iterable<Post> findAllByUsername(String username);

    Integer deleteAllByUsername(String username);
}
