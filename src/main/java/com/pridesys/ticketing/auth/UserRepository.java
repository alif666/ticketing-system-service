package com.pridesys.ticketing.auth;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {
    private final JdbcTemplate jdbc;

    public UserRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public Optional<UserRecord> findByEmail(String email) {
        return jdbc.query("SELECT id,email,password_hash,role,name,mobile,designation,office,active FROM users WHERE email = ?",
                this::map, email.trim().toLowerCase()).stream().findFirst();
    }

    public Optional<UserRecord> findById(long id) {
        return jdbc.query("SELECT id,email,password_hash,role,name,mobile,designation,office,active FROM users WHERE id = ?",
                this::map, id).stream().findFirst();
    }

    public void updateProfile(long id, String name, String mobile, String designation, String office) {
        jdbc.update("UPDATE users SET name=?,mobile=?,designation=?,office=? WHERE id=?", name, mobile, designation, office, id);
    }

    public void updatePassword(long id, String hash) {
        jdbc.update("UPDATE users SET password_hash=? WHERE id=?", hash, id);
    }

    private UserRecord map(ResultSet rs, int row) throws SQLException {
        return new UserRecord(rs.getLong("id"), rs.getString("email"), rs.getString("password_hash"),
                UserRole.valueOf(rs.getString("role")), rs.getString("name"), rs.getString("mobile"),
                rs.getString("designation"), rs.getString("office"), rs.getBoolean("active"));
    }
}
