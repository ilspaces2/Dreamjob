package ru.job4j.dreamjob.store;

import org.junit.After;
import org.junit.Test;
import ru.job4j.dreamjob.Main;
import ru.job4j.dreamjob.model.Candidate;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class CandidateDBStoreTest {

    private static final CandidateDBStore STORE = new CandidateDBStore(new Main().loadPool());

    @After
    public void clearTable() {
        STORE.deleteAll();
    }

    @Test
    public void whenCreatePost() {
        Candidate candidate = new Candidate(0, "Max", "Java developer",
                LocalDateTime.now(), new byte[]{1, 2, 3, 4, 5});
        STORE.add(candidate);
        Candidate candidateInDb = STORE.findById(candidate.getId());
        assertThat(candidateInDb.getName(), is(candidateInDb.getName()));
    }

    @Test
    public void whenUpdatePost() {
        Candidate candidate = new Candidate(0, "Max", "Java developer",
                LocalDateTime.now(), new byte[]{1, 2, 3, 4, 5});
        STORE.add(candidate);
        candidate.setName("Jon");
        candidate.setPhoto(new byte[]{5, 4, 3, 2, 1});
        STORE.update(candidate);
        Candidate candidateInDb = STORE.findById(candidate.getId());
        assertThat(candidateInDb.getName(), is(candidate.getName()));
        assertThat(candidateInDb.getPhoto(), is(candidate.getPhoto()));
    }

    @Test
    public void whenFindAll() {
        List<Candidate> list = List.of(new Candidate(0, "Max", "Java developer",
                        LocalDateTime.now(), new byte[]{1, 2, 3}),
                new Candidate(0, "Boris", "C# developer",
                        LocalDateTime.now(), new byte[]{1, 2, 3, 4}),
                new Candidate(0, "Tony", "C++ developer",
                        LocalDateTime.now(), new byte[]{1, 2, 3, 4, 5})
        );
        list.forEach(STORE::add);
        assertThat(STORE.findAll(), is(list));
    }
}