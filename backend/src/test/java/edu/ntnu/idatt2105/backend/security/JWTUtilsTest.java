package edu.ntnu.idatt2105.backend.security;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentMatchers.argThat;

import com.auth0.jwt.exceptions.JWTVerificationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import jakarta.servlet.http.HttpServletResponse;

@ExtendWith(MockitoExtension.class)
class JWTUtilsTest {

  private static final int TEST_USER_ID = 123;
  private static final String TEST_ROLE = "ROLE_USER";
  private static final String INVALID_TOKEN = "invalid.token.value";

  @InjectMocks
  private JWTUtils jwtUtils;

  @Mock
  private HttpServletResponse response;

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
  void generateToken_ShouldThrowException_WhenRoleIsNull() {
    assertThrows(IllegalArgumentException.class, () -> jwtUtils.generateToken(TEST_USER_ID, null));
  }

  @Test
  void validateTokenAndGetUserId_ShouldReturnUserId_WhenTokenIsValid() {
    String userId = jwtUtils.validateTokenAndGetUserId(validToken);
    assertEquals(Integer.toString(TEST_USER_ID), userId);
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

  @Test
  void setJWTCookie_ShouldSetCookieWithCorrectProperties() {
    jwtUtils.setJWTCookie(validToken, response);

    verify(response).addCookie(argThat(cookie -> {
      assertEquals("JWT", cookie.getName());
      assertEquals(validToken, cookie.getValue());
      assertTrue(cookie.isHttpOnly());
      assertTrue(cookie.getSecure());
      assertEquals("/", cookie.getPath());
      assertEquals(300, cookie.getMaxAge());
      return true;
    }));
  }

  @Test
  void setJWTCookie_ShouldHandleNullTokenGracefully() {
    jwtUtils.setJWTCookie(null, response);

    verify(response).addCookie(argThat(cookie -> {
      assertEquals("JWT", cookie.getName());
      assertNull(cookie.getValue());
      assertTrue(cookie.isHttpOnly());
      assertTrue(cookie.getSecure());
      assertEquals("/", cookie.getPath());
      assertEquals(300, cookie.getMaxAge());
      return true;
    }));
  }
}