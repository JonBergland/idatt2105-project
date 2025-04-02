package edu.ntnu.idatt2105.backend.security;

import static org.junit.jupiter.api.Assertions.*;

import com.auth0.jwt.exceptions.JWTVerificationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class JWTUtilsTest {

  private static final String TEST_USER_ID = "123";
  private static final String TEST_ROLE = "ROLE_USER";
  private static final String INVALID_TOKEN = "invalid.token.value";

  @InjectMocks
  private JWTUtils jwtUtils;

  private String validToken;

  @BeforeEach
  void setUp() {
    validToken = jwtUtils.generateToken(TEST_USER_ID, TEST_ROLE);
  }

  @Test
  void generateToken_ShouldReturnValidJWT() {
    assertNotNull(validToken);
    assertFalse(validToken.isBlank());
  }

  @Test
  void generateToken_ShouldThrowException_WhenUserIdIsNull() {
    assertThrows(IllegalArgumentException.class, () -> jwtUtils.generateToken(null, TEST_ROLE));
  }

  @Test
  void generateToken_ShouldThrowException_WhenRoleIsNull() {
    assertThrows(IllegalArgumentException.class, () -> jwtUtils.generateToken(TEST_USER_ID, null));
  }

  @Test
  void validateTokenAndGetUserId_ShouldReturnUserId_WhenTokenIsValid() {
    String userId = jwtUtils.validateTokenAndGetUserId(validToken);
    assertEquals(TEST_USER_ID, userId);
  }

  @Test
  void validateTokenAndGetUserId_ShouldThrowException_WhenTokenIsInvalid() {
    assertThrows(JWTVerificationException.class, () -> jwtUtils.validateTokenAndGetUserId(INVALID_TOKEN));
  }

  @Test
  void validateTokenAndGetRole_ShouldReturnRole_WhenTokenIsValid() {
    String role = jwtUtils.validateTokenAndGetRole(validToken);
    assertEquals(TEST_ROLE, role);
  }

  @Test
  void validateTokenAndGetRole_ShouldThrowException_WhenTokenIsInvalid() {
    assertThrows(JWTVerificationException.class, () -> jwtUtils.validateTokenAndGetRole(INVALID_TOKEN));
  }
}