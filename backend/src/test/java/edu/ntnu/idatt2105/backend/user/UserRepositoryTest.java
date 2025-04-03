package edu.ntnu.idatt2105.backend.user;

import edu.ntnu.idatt2105.backend.user.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
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
    testUser = new User(1, "test@example.com", 47, 12345678, "John", "Doe", "hashedpassword", "ROLE_USER");
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
}
