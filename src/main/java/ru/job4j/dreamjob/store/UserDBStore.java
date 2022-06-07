package ru.job4j.dreamjob.store;

import net.jcip.annotations.ThreadSafe;
import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.dreamjob.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Optional;

@ThreadSafe
@Repository
public class UserDBStore {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserDBStore.class);

    private final BasicDataSource pool;

    public UserDBStore(BasicDataSource pool) {
        this.pool = pool;
    }

    public Optional<User> add(User user) {
        Optional<User> addUser = Optional.empty();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("INSERT INTO users (password, email, name) VALUES (?, ?, ?)",
                     PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, user.getPassword());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getName());
            ps.execute();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    user.setId(rs.getInt(1));
                    addUser = Optional.of(user);
                }
            }
        } catch (Exception e) {
            LOGGER.error("SQL user add error : {}", e.getMessage());
        }
        return addUser;
    }

    public User findById(int id) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("SELECT * FROM users WHERE id = ?")
        ) {
            ps.setInt(1, id);
            try (ResultSet it = ps.executeQuery()) {
                if (it.next()) {
                    return new User(it.getInt("id"),
                            it.getString("password"),
                            it.getString("email"),
                            it.getString("name")
                    );
                }
            }
        } catch (Exception e) {
            LOGGER.error("SQL findById user error : {}", e.getMessage());
        }
        return null;
    }

    public boolean deleteAll() {
        boolean rez = false;
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("DELETE FROM users")
        ) {
            rez = ps.executeUpdate() > 0;
        } catch (Exception e) {
            LOGGER.error("SQL deleteAll error : {}", e.getMessage());
        }
        return rez;
    }

    public Optional<User> findUserByEmailAndPwd(String password, String email) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("SELECT * FROM users WHERE password = ? and email = ?")
        ) {
            ps.setString(1, password);
            ps.setString(2, email);
            try (ResultSet it = ps.executeQuery()) {
                if (it.next()) {
                    return Optional.of(new User(it.getInt("id"),
                            it.getString("password"),
                            it.getString("email"),
                            it.getString("name")
                    ));
                }
            }
        } catch (Exception e) {
            LOGGER.error("SQL findUserByEmailAndPwd user error : {}", e.getMessage());
        }
        return Optional.empty();
    }
}
