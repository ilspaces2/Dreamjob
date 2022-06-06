package ru.job4j.dreamjob.store;

import org.junit.After;
import org.junit.Test;
import ru.job4j.dreamjob.Main;
import ru.job4j.dreamjob.model.User;

import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

public class UserDBStoreTest {

    private static final UserDBStore STORE = new UserDBStore(new Main().loadPool());

    @After
    public void clearTable() {
        STORE.deleteAll();
    }

    @Test
    public void whenAddUser() {
        User user = new User(0, "Boris", "boris@yandex.ru");
        STORE.add(user);
        User userDB = STORE.findById(user.getId());
        assertThat(user.getPassword(), is(userDB.getPassword()));
    }

    @Test
    public void whenAddUsersWithEqualsEmail() {
        User user1 = new User(0, "Max", "Max@yandex.ru");
        Optional<User> userOptional1 = STORE.add(user1);
        User user2 = new User(0, "Joe", "Max@yandex.ru");
        Optional<User> userOptional2 = STORE.add(user2);
        assertFalse(userOptional1.isEmpty());
        assertTrue(userOptional2.isEmpty());
    }

    @Test
    public void whenFindByPasAndEmail() {
        User user = new User(0, "Max", "Max@yandex.ru");
        User user1 = new User(1, "Max1", "Max1@yandex.ru");
        User user2 = new User(2, "Max2", "Max2@yandex.ru");
        STORE.add(user);
        STORE.add(user1);
        STORE.add(user2);
        Optional<User> userOptional = STORE.findUserByEmailAndPwd(user.getPassword(), user.getEmail());
        assertFalse(userOptional.isEmpty());
        assertThat(userOptional.get().getPassword(), is(user.getPassword()));
        assertThat(userOptional.get().getEmail(), is(user.getEmail()));
    }
}