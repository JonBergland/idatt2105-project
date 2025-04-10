package edu.ntnu.idatt2105.backend.repository;

import edu.ntnu.idatt2105.backend.model.User;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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
  @Transactional
  public void createUser(User user) throws DataAccessException {
    jdbcTemplate.update(
        "INSERT INTO User (email, password, role, name, surname, phone_number, country_code)"
            + " VALUES (?, ?, ?, ?, ?, ?, ?)",
        user.getEmail(), user.getPassword(), user.getRole(), user.getName(),
        user.getSurname(), user.getPhoneNumber(), user.getCountryCode()
    );

    int userID = getUserIDFromEmail(user.getEmail());

    jdbcTemplate.update(
        "INSERT INTO Location (user_id) VALUES (?)", userID);
  }

  /**
   * gets user id from database, based on user email.
   *
   * @param email the user's email
   * @return the linked user id
   */
  private int getUserIDFromEmail(String email) {
    return jdbcTemplate.queryForObject(
        "SELECT id FROM User WHERE email = ?",
        new Object[]{email},
        Integer.class);
  }

  /**
   * gets a user from database, based on user id.
   *
   * @param userID the user's id
   * @return the retrieved user
   */
  public User getUser(int userID) {
    try {
      return jdbcTemplate.queryForObject(
          "SELECT User.*, User.id AS userID, User.phone_number AS phoneNumber, User.country_code AS countryCode, Location.*, Location.postal_code AS postalCode FROM User "
              + "LEFT JOIN Location ON User.id = Location.user_id "
              + "WHERE User.id = ?",
          new Object[]{userID},
          new BeanPropertyRowMapper<>(User.class)
      );
    } catch (DataAccessException e) {
      logger.error("Error retrieving user from database: ", e);
      throw e;
    }
  }

  /**
   * updates certain user info in database.
   *
   * @param user the updated user
   */
  public void updateUser(User user) {
    jdbcTemplate.update(
        "UPDATE User "
        + "LEFT JOIN Location ON User.id = Location.user_id "
        + "SET User.name = ?, User.surname = ?, User.phone_number = ?, User.country_code = ?, "
        + "Location.address = ?, Location.postal_code = ?, Location.city = ?, "
        + "Location.longitude = ?, Location.latitude = ? "
        + "WHERE User.id = ?",
        user.getName(),
        user.getSurname(),
        user.getPhoneNumber(),
        user.getCountryCode(),
        user.getAddress(),
        user.getPostalCode(),
        user.getCity(),
        user.getLongitude(),
        user.getLatitude(),
        user.getUserID());
  }
}