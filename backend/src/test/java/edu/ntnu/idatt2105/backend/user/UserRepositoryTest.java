package edu.ntnu.idatt2105.backend.user;

import edu.ntnu.idatt2105.backend.repository.UserRepository;
import edu.ntnu.idatt2105.backend.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserRepositoryTest {

  @Mock
  private JdbcTemplate jdbcTemplate;

  @InjectMocks
  private UserRepository userRepository;

  private User testUser;

  @BeforeEach
  void setUp() {
    testUser = new User(1, "test@example.com", 12345678, 12, "John", "Doe", "hashedpassword", "ROLE_USER", 12, 12, "ds", 12, "jhf");
  }

  @Test
  void getPasswordByEmail_ShouldReturnPassword_WhenUserExists() {
    when(jdbcTemplate.queryForObject(anyString(), any(Object[].class), eq(String.class)))
        .thenReturn("hashedpassword");

    String password = userRepository.getPasswordByEmail("test@example.com");

    assertEquals("hashedpassword", password);
    verify(jdbcTemplate, times(1)).queryForObject(anyString(), any(Object[].class), eq(String.class));
  }

  @Test
  void getPasswordByEmail_ShouldThrowException_WhenUserNotFound() {
    when(jdbcTemplate.queryForObject(anyString(), any(Object[].class), eq(String.class)))
        .thenThrow(new EmptyResultDataAccessException(1));

    assertThrows(DataAccessException.class, () -> userRepository.getPasswordByEmail("nonexistent@example.com"));
    verify(jdbcTemplate, times(1)).queryForObject(anyString(), any(Object[].class), eq(String.class));
  }

  @Test
  void getUserByEmail_ShouldReturnUser_WhenUserExists() {
    when(jdbcTemplate.queryForObject(anyString(), (Object[]) any(Object[].class),
        (RowMapper<Object>) any()))
        .thenReturn(testUser);

    User user = userRepository.getUserByEmail("test@example.com");

    assertNotNull(user);
    assertEquals("test@example.com", user.getEmail());
    verify(jdbcTemplate, times(1)).queryForObject(anyString(), (Object[]) any(Object[].class),
        (RowMapper<Object>) any());
  }

  @Test
  void getUserByEmail_ShouldReturnNull_WhenUserNotFound() {
    when(jdbcTemplate.queryForObject(anyString(), (Object[]) any(Object[].class),
        (RowMapper<Object>) any()))
        .thenThrow(new EmptyResultDataAccessException(1));

    User user = userRepository.getUserByEmail("nonexistent@example.com");

    assertNull(user);
    verify(jdbcTemplate, times(1)).queryForObject(anyString(), (Object[]) any(Object[].class),
        (RowMapper<Object>) any());
  }

  @Test
  void createUser_ShouldInsertUserAndLocation() {
    when(jdbcTemplate.queryForObject(anyString(), any(Object[].class), eq(Integer.class)))
        .thenReturn(1);

    userRepository.createUser(testUser);

    verify(jdbcTemplate, times(1)).update(
        eq("INSERT INTO User (email, password, role, name, surname, phone_number, country_code) VALUES (?, ?, ?, ?, ?, ?, ?)"),
        eq(testUser.getEmail()), eq(testUser.getPassword()), eq(testUser.getRole()), eq(testUser.getName()),
        eq(testUser.getSurname()), eq(testUser.getPhoneNumber()), eq(testUser.getCountryCode())
    );
    verify(jdbcTemplate, times(1)).update(
        eq("INSERT INTO Location (user_id) VALUES (?)"), eq(1)
    );
  }

  @Test
  void getUser_ShouldReturnUser_WhenUserExists() {
    when(jdbcTemplate.queryForObject(anyString(), any(Object[].class), any(BeanPropertyRowMapper.class)))
        .thenReturn(testUser);

    User user = userRepository.getUser(1);

    assertNotNull(user);
    assertEquals(testUser.getEmail(), user.getEmail());
    verify(jdbcTemplate, times(1)).queryForObject(
        eq("SELECT User.*, User.id AS userID, User.phone_number AS phoneNumber, User.country_code AS countryCode, Location.*, Location.postal_code AS postalCode FROM User LEFT JOIN Location ON User.id = Location.user_id WHERE User.id = ?"),
        any(Object[].class), any(BeanPropertyRowMapper.class)
    );
  }

  @Test
  void getUser_ShouldThrowException_WhenDataAccessExceptionOccurs() {
    when(jdbcTemplate.queryForObject(anyString(), any(Object[].class), any(BeanPropertyRowMapper.class)))
        .thenThrow(new DataAccessException("DB Error") {});

    assertThrows(DataAccessException.class, () -> userRepository.getUser(1));
    verify(jdbcTemplate, times(1)).queryForObject(
        eq("SELECT User.*, User.id AS userID, User.phone_number AS phoneNumber, User.country_code AS countryCode, Location.*, Location.postal_code AS postalCode FROM User LEFT JOIN Location ON User.id = Location.user_id WHERE User.id = ?"),
        any(Object[].class), any(BeanPropertyRowMapper.class)
    );
  }

  @Test
  void updateUser_ShouldUpdateUserAndLocation() {
    userRepository.updateUser(testUser);

    verify(jdbcTemplate, times(1)).update(
        eq("UPDATE User LEFT JOIN Location ON User.id = Location.user_id SET User.name = ?, User.surname = ?, User.phone_number = ?, User.country_code = ?, Location.address = ?, Location.postal_code = ?, Location.city = ?, Location.longitude = ?, Location.latitude = ? WHERE User.id = ?"),
        eq(testUser.getName()), eq(testUser.getSurname()), eq(testUser.getPhoneNumber()), eq(testUser.getCountryCode()),
        eq(testUser.getAddress()), eq(testUser.getPostalCode()), eq(testUser.getCity()), eq(testUser.getLongitude()),
        eq(testUser.getLatitude()), eq(testUser.getUserID())
    );
  }
}
