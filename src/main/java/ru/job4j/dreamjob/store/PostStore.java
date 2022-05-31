package ru.job4j.dreamjob.store;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Repository;
import ru.job4j.dreamjob.model.Post;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@ThreadSafe
@Repository
public class PostStore {

    private final Map<Integer, Post> posts = new ConcurrentHashMap<>();

    private final AtomicInteger size;

    private PostStore() {
        posts.put(1, new Post(1, "Junior Java Job", "Sber", LocalDateTime.now()));
        posts.put(2, new Post(2, "Middle Java Job", "VTB", LocalDateTime.now()));
        posts.put(3, new Post(3, "Senior Java Job", "GAZ", LocalDateTime.now()));
        size = new AtomicInteger(posts.size());
    }

    public Collection<Post> findAll() {
        return posts.values();
    }

    public Post add(Post post) {
        post.setId(size.incrementAndGet());
        return posts.put(post.getId(), post);
    }

    public Post findById(int id) {
        return posts.get(id);
    }

    public boolean update(Post post) {
        return posts.replace(post.getId(), post) != null;
    }
}