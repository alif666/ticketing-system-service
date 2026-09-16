package com.pridesys.ticketing.repository;

import java.sql.*;
import java.util.Optional;
import java.util.List;
import com.pridesys.ticketing.dto.UsersProjectsDtos;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder; import org.springframework.jdbc.support.KeyHolder; import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.stereotype.Repository;
import com.pridesys.ticketing.entity.UserRecord;
import com.pridesys.ticketing.entity.UserRole;

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

    public Optional<Long> clientId(long id) {
        return jdbc.query("SELECT client_id FROM users WHERE id=?", (rs,n) -> rs.getObject(1, Long.class), id).stream().findFirst();
    }
    public List<UserRecord> search(String search, int offset, int limit) {
        String q = "%" + (search == null ? "" : search.trim().toLowerCase()) + "%";
        return jdbc.query("SELECT id,email,password_hash,role,name,mobile,designation,office,active FROM users WHERE LOWER(email) LIKE ? OR LOWER(name) LIKE ? ORDER BY id LIMIT ? OFFSET ?", this::map, q, q, limit, offset);
    }
    public long count(String search) {
        String q = "%" + (search == null ? "" : search.trim().toLowerCase()) + "%";
        return jdbc.queryForObject("SELECT COUNT(*) FROM users WHERE LOWER(email) LIKE ? OR LOWER(name) LIKE ?", Long.class, q, q);
    }
    public long create(UsersProjectsDtos.CreateUserRequest req, String hash) {
        KeyHolder h=new GeneratedKeyHolder(); jdbc.update((PreparedStatementCreator) con -> {var p=con.prepareStatement("INSERT INTO users(email,password_hash,role,name,client_id) VALUES (?,?,?,?,?)",Statement.RETURN_GENERATED_KEYS);p.setString(1,req.email().trim().toLowerCase());p.setString(2,hash);p.setString(3,req.role().name());p.setString(4,req.name().trim()); if(req.clientId()==null)p.setNull(5,Types.BIGINT);else p.setLong(5,req.clientId());return p;},h); return h.getKey().longValue();
    }
    public void update(long id, UsersProjectsDtos.UpdateUserRequest req) {
        jdbc.update("UPDATE users SET name=?,mobile=?,designation=?,office=?,active=COALESCE(?,active) WHERE id=?", req.name().trim(), req.mobile(), req.designation(), req.office(), req.active(), id);
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
