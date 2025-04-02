package edu.ntnu.idatt2105.backend.user;

import edu.ntnu.idatt2105.backend.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class UserRepository {

  private final JdbcTemplate jdbcTemplate;

  public String getPasswordByEmail(String email) throws DataAccessException {
    return jdbcTemplate.queryForObject(
        "SELECT password FROM User WHERE email = ?",
        new Object[] {email},
        String.class
    );
  }

  public User getUserByEmail(String email) throws DataAccessException {
    try {
      return jdbcTemplate.queryForObject(
          "SELECT id, email, role FROM User WHERE email = ?",
          new Object[] {email},
          (rs, rowNum) -> {
            User user = new User();
            user.setUserID(rs.getInt("id"));
            user.setEmail(rs.getString("email"));
            user.setRole(rs.getString("role"));
            return user;
          }
      );
    } catch (EmptyResultDataAccessException e) {
      return null;
    }
  }

  public void createUser(User user) throws DataAccessException {
    jdbcTemplate.update(
        "INSERT INTO User (email, password, role, name, surname, phone_number)"
            + " VALUES (?, ?, ?, ?, ?, ?)",
        user.getEmail(), user.getPassword(), user.getRole(), user.getName(),
        user.getSurname(), user.getPhoneNumber()
    );
  }
}

