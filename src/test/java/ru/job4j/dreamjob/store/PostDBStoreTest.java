package ru.job4j.dreamjob.store;

import org.junit.After;
import org.junit.Test;
import ru.job4j.dreamjob.Main;
import ru.job4j.dreamjob.model.City;
import ru.job4j.dreamjob.model.Post;


import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class PostDBStoreTest {

    private static final PostDBStore STORE = new PostDBStore(new Main().loadPool());

    @After
    public void clearTable() {
        STORE.deleteAll();
    }

    @Test
    public void whenCreatePost() {
        Post post = new Post(0, "Java Job", "OAO",
                LocalDateTime.now(), true, new City(1, null));
        STORE.add(post);
        Post postInDb = STORE.findById(post.getId());
        assertThat(postInDb.getName(), is(post.getName()));
    }

    @Test
    public void whenUpdatePost() {
        Post post = new Post(0, "Java Job", "OAO",
                LocalDateTime.now(), true, new City(1, null));
        STORE.add(post);
        post.setName("Junior");
        post.setCity(new City(3, null));
        STORE.update(post);
        Post postInDb = STORE.findById(post.getId());
        assertThat(postInDb.getName(), is(post.getName()));
        assertThat(postInDb.getCity(), is(post.getCity()));
    }

    @Test
    public void whenFindAll() {
        List<Post> list = List.of(new Post(0, "Java Job", "OAO",
                        LocalDateTime.now(), true, new City(1, null)),
                new Post(0, "C Job", "OOO",
                        LocalDateTime.now(), true, new City(2, null)),
                new Post(0, "Js Job", "ZAO",
                        LocalDateTime.now(), true, new City(3, null))
        );
        list.forEach(STORE::add);
        assertThat(STORE.findAll(), is(list));
    }
}