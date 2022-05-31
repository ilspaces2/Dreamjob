package ru.job4j.dreamjob.store;

import org.springframework.stereotype.Repository;
import ru.job4j.dreamjob.model.Candidate;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class CandidateStore {

    private final Map<Integer, Candidate> candidates = new ConcurrentHashMap<>();

    private final AtomicInteger size;

    private CandidateStore() {
        candidates.put(1, new Candidate(1, "Ivan", "Java", LocalDateTime.now()));
        candidates.put(2, new Candidate(2, "Max", "C++", LocalDateTime.now()));
        candidates.put(3, new Candidate(3, "Anna", "Python", LocalDateTime.now()));
        size = new AtomicInteger(candidates.size());
    }

    public Collection<Candidate> findAll() {
        return candidates.values();
    }

    public Candidate add(Candidate candidate) {
        candidate.setId(size.incrementAndGet());
        return candidates.put(candidate.getId(), candidate);
    }

    public Candidate findById(int id) {
        return candidates.get(id);
    }

    public boolean update(Candidate candidate) {
        return candidates.replace(candidate.getId(), candidate) != null;
    }
}
