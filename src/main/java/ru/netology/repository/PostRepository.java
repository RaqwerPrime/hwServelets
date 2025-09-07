package ru.netology.repository;

import org.springframework.stereotype.Repository;
import ru.netology.model.Post;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class PostRepository {
    private final Map<Long, Post> posts = new ConcurrentHashMap<>();
    private final AtomicLong currentId = new AtomicLong(1);

    public List<Post> all() {
        return new ArrayList<>(posts.values());
    }

    public Optional<Post> getById(long id) {
        return Optional.ofNullable(posts.get(id));
    }

    public Post save(Post post) {
        if (post.getId() == 0) {
            long newId = currentId.getAndIncrement();
            Post newPost = new Post(newId, post.getContent());
            posts.put(newId, newPost);
            return newPost;
        } else {
            if (posts.containsKey(post.getId())) {
                posts.put(post.getId(), post);
                return post;
            } else {
                posts.put(post.getId(), post);
                currentId.updateAndGet(current -> Math.max(current, post.getId() + 1));
                return post;
            }
        }
    }

    public void removeById(long id) {
        posts.remove(id);
    }
}