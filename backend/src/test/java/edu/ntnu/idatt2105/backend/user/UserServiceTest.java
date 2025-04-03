package edu.ntnu.idatt2105.backend.user;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import edu.ntnu.idatt2105.backend.security.dto.SigninRequest;
import edu.ntnu.idatt2105.backend.security.dto.SignupRequest;
import edu.ntnu.idatt2105.backend.user.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

class UserServiceTest {

  @Mock
  private UserRepository userRepository;

  @Mock
  private PasswordEncoder passwordEncoder;

  @InjectMocks
  private UserService userService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void getUserByEmail_ShouldReturnUser() {
    User mockUser = new User(123, "test@example.com", 47, 12345678, "hashedPassword", "John", "Doe", "ROLE_USER");
    when(userRepository.getUserByEmail("test@example.com")).thenReturn(mockUser);

    User user = userService.getUserByEmail("test@example.com");

    assertNotNull(user);
    assertEquals("test@example.com", user.getEmail());
  }

  @Test
  void checkCredentials_ShouldReturnTrue_WhenPasswordMatches() {
    SigninRequest request = new SigninRequest("test@example.com", "password123");
    when(userRepository.getPasswordByEmail("test@example.com")).thenReturn("hashedPassword");
    when(passwordEncoder.matches("password123", "hashedPassword")).thenReturn(true);

    assertTrue(userService.checkCredentials(request));
  }

  @Test
  void checkCredentials_ShouldReturnFalse_WhenPasswordDoesNotMatch() {
    SigninRequest request = new SigninRequest("test@example.com", "wrongPassword");
    when(userRepository.getPasswordByEmail("test@example.com")).thenReturn("hashedPassword");
    when(passwordEncoder.matches("wrongPassword", "hashedPassword")).thenReturn(false);

    assertFalse(userService.checkCredentials(request));
  }

  @Test
  void createUser_ShouldEncodePasswordAndSaveUser() {
    SignupRequest request = new SignupRequest("test@example.com", "password123", "John", "Doe", 47, 12345678);
    User user = UserMapper.INSTANCE.signupRequestToUser(request);
    user.setRole("ROLE_USER");
    when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");

    userService.createUser(request);

    verify(userRepository).createUser(argThat(savedUser ->
        savedUser.getEmail().equals("test@example.com") &&
            savedUser.getPassword().equals("encodedPassword") &&
            savedUser.getRole().equals("ROLE_USER")
    ));
  }
}
