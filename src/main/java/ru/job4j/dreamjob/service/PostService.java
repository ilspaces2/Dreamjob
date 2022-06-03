package ru.job4j.dreamjob.service;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;
import ru.job4j.dreamjob.model.Post;
import ru.job4j.dreamjob.store.PostDBStore;

import java.util.Collection;

@ThreadSafe
@Service
public class PostService {

    private final CityService cityService;

    private final PostDBStore store;

    public PostService(CityService cityService, PostDBStore store) {
        this.cityService = cityService;
        this.store = store;
    }

    public Collection<Post> findAll() {
        Collection<Post> posts = store.findAll();
        posts.forEach(post -> post.setCity(cityService.findById(post.getCity().getId())));
        return posts;
    }

    public Post add(Post post) {
        return store.add(post);
    }

    public Post findById(int id) {
        return store.findById(id);
    }

    public boolean update(Post post) {
        return store.update(post);
    }
}