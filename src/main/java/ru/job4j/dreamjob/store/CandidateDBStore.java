package ru.job4j.dreamjob.store;

import net.jcip.annotations.ThreadSafe;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.stereotype.Repository;
import ru.job4j.dreamjob.model.Candidate;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@ThreadSafe
@Repository
public class CandidateDBStore {

    private final BasicDataSource pool;

    public CandidateDBStore(BasicDataSource pool) {
        this.pool = pool;
    }

    public List<Candidate> findAll() {
        List<Candidate> candidates = new ArrayList<>();
        try (Connection connection = pool.getConnection();
             PreparedStatement ps = connection.prepareStatement("SELECT * from candidate")) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    candidates.add(new Candidate(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("description"),
                            rs.getTimestamp("created").toLocalDateTime(),
                            rs.getBytes("photo")
                    ));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return candidates;
    }

    public Candidate add(Candidate candidate) {
        try (Connection connection = pool.getConnection();
             PreparedStatement ps = connection.prepareStatement("INSERT into candidate "
                     + "(name, description ,created ,photo) VALUES (?,?,?,?)", PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, candidate.getName());
            ps.setString(2, candidate.getDesc());
            ps.setTimestamp(3, Timestamp.valueOf(candidate.getCreated()));
            ps.setBytes(4, candidate.getPhoto());
            ps.execute();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    candidate.setId(rs.getInt(1));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return candidate;
    }

    public boolean update(Candidate candidate) {
        boolean rzl = false;
        try (Connection connection = pool.getConnection();
             PreparedStatement ps = connection.prepareStatement("UPDATE candidate set "
                     + "name=?, description=? ,created=? ,photo=? WHERE id=?")) {
            ps.setString(1, candidate.getName());
            ps.setString(2, candidate.getDesc());
            ps.setTimestamp(3, Timestamp.valueOf(candidate.getCreated()));
            ps.setBytes(4, candidate.getPhoto());
            ps.setInt(5, candidate.getId());
            rzl = ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rzl;
    }

    public Candidate findById(int id) {
        try (Connection connection = pool.getConnection();
             PreparedStatement ps = connection.prepareStatement("SELECT * from candidate WHERE id=?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Candidate(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("description"),
                            rs.getTimestamp("created").toLocalDateTime(),
                            rs.getBytes("photo")
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
