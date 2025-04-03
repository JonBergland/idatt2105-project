package edu.ntnu.idatt2105.backend.user;

import edu.ntnu.idatt2105.backend.user.model.User;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * Repository for updating, deleting and inserting to the User table.
 */
@RequiredArgsConstructor
@Repository
public class UserRepository {

  private final Logger logger = LoggerFactory.getLogger(UserRepository.class);

  private final JdbcTemplate jdbcTemplate;

  /**
   * gets the password from user based on email from the database.
   *
   * @param email the email of the user
   * @return the password
   * @throws DataAccessException if something goes wrong
   */
  public String getPasswordByEmail(String email) throws DataAccessException {
    return jdbcTemplate.queryForObject(
        "SELECT password FROM `User` WHERE email = ?",
        new Object[] {email},
        String.class
    );
  }

  /**
   * gets user information from user based on email from database.
   *
   * @param email the email of the user
   * @return the user object
   * @throws DataAccessException if something goes wrong
   */
  public User getUserByEmail(String email) throws DataAccessException {
    try {
      return jdbcTemplate.queryForObject(
          "SELECT id, email, role FROM `User` WHERE email = ?",
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

  /**
   * creates a user in the database based on a user object.
   *
   * @param user the user to store
   * @throws DataAccessException if something goes wrong
   */
  public void createUser(User user) throws DataAccessException {
    try {
      jdbcTemplate.update(
        "INSERT INTO `User` (email, password, role, name, surname, country_code, phone_number)"
            + " VALUES (?, ?, ?, ?, ?, ?, ?)",
        user.getEmail(), user.getPassword(), user.getRole(), user.getName(),
        user.getSurname(), user.getCountryCode(), user.getPhoneNumber()
    );
    } catch (DataAccessException e) {
      logger.error("Error inserting user into database: ", e);
      throw e;
    }
  }
}

