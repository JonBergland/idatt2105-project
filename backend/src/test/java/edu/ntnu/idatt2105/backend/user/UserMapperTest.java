package edu.ntnu.idatt2105.backend.user;

import static org.junit.jupiter.api.Assertions.*;

import edu.ntnu.idatt2105.backend.security.dto.SigninRequest;
import edu.ntnu.idatt2105.backend.security.dto.SignupRequest;
import edu.ntnu.idatt2105.backend.user.model.User;
import org.junit.jupiter.api.Test;

class UserMapperTest {

  private final UserMapper userMapper = UserMapper.INSTANCE;

  @Test
  void signupRequestToUser_ShouldMapCorrectly() {
    SignupRequest request = new SignupRequest("test@example.com", "password123", "John", "Doe", 47, 12345678);
    User user = userMapper.signupRequestToUser(request);

    assertNotNull(user, "Mapped user should not be null");
    assertEquals(request.getEmail(), user.getEmail(), "Email should be mapped correctly");
    assertEquals(request.getPassword(), user.getPassword(), "Password should be mapped correctly");
    assertEquals(request.getName(), user.getName(), "Name should be mapped correctly");
    assertEquals(request.getSurname(), user.getSurname(), "Surname should be mapped correctly");
    assertEquals(request.getLandCode(), user.getLandCode(), "Land code should be mapped correctly");
    assertEquals(request.getPhoneNumber(), user.getPhoneNumber(), "Phone number should be mapped correctly");
  }

  @Test
  void signinRequestToUser_ShouldMapCorrectly() {
    SigninRequest request = new SigninRequest("test@example.com", "password123");
    User user = userMapper.signinRequestToUser(request);

    assertNotNull(user, "Mapped user should not be null");
    assertEquals(request.getEmail(), user.getEmail(), "Email should be mapped correctly");
    assertEquals(request.getPassword(), user.getPassword(), "Password should be mapped correctly");
  }
}
