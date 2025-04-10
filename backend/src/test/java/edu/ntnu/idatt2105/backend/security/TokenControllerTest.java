package edu.ntnu.idatt2105.backend.security;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import edu.ntnu.idatt2105.backend.controller.TokenController;
import edu.ntnu.idatt2105.backend.dto.token.SigninRequest;
import edu.ntnu.idatt2105.backend.dto.token.SignupRequest;
import edu.ntnu.idatt2105.backend.service.UserService;
import edu.ntnu.idatt2105.backend.model.User;
import edu.ntnu.idatt2105.backend.utils.JWTUtils;
import jakarta.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

@ExtendWith(MockitoExtension.class)
class TokenControllerTest {

  @Mock
  private UserService userService;

  @Mock
  private JWTUtils jwtUtils;

  @Mock
  private HttpServletResponse response;

  @InjectMocks
  private TokenController tokenController;

  private SigninRequest signinRequest;
  private SignupRequest signupRequest;
  private User user;
  private static final String EMAIL = "test@example.com";
  private static final String PASSWORD = "password123";
  private static final String NAME = "Joe";
  private static final String SURNAME = "Doe";
  private static final int PHONE = 12345678;
  private static final int COUNTRY_CODE = 2;
  private static final int USER_ID = 123;
  private static final String ROLE = "ROLE_USER";
  private static final String TOKEN = "valid.jwt.token";
  private static double LATITUDE;
  private static double LONGITUDE;
  private static String CITY;
  private static int POSTAL_CODE;
  private static String ADDRESS;

  @BeforeEach
  void setUp() {
    signinRequest = new SigninRequest(EMAIL, PASSWORD);
    signupRequest = new SignupRequest(EMAIL, PASSWORD, NAME, SURNAME, PHONE, COUNTRY_CODE);
    user = new User(USER_ID, EMAIL, PHONE, COUNTRY_CODE, NAME, SURNAME, PASSWORD, ROLE, LATITUDE, LONGITUDE, CITY, POSTAL_CODE, ADDRESS);
  }

  @Test
  void signIn_ShouldReturnToken_WhenCredentialsAreValid() {
    when(userService.checkCredentials(signinRequest)).thenReturn(true);
    when(userService.getUserByEmail(EMAIL)).thenReturn(user);
    when(jwtUtils.generateToken(USER_ID, ROLE)).thenReturn(TOKEN);

    boolean result = tokenController.signIn(signinRequest, this.response);

    assertTrue(result);
    verify(userService).getUserByEmail(EMAIL);
    verify(jwtUtils).generateToken(USER_ID, ROLE);
    verify(jwtUtils).setJWTCookie(TOKEN, response);
  }

  @Test
  void signIn_ShouldThrowUnauthorized_WhenCredentialsAreInvalid() {
    when(userService.checkCredentials(signinRequest)).thenReturn(false);

    assertThrows(ResponseStatusException.class, () -> tokenController.signIn(signinRequest, this.response));
    assertThrows(ResponseStatusException.class, () -> tokenController.signIn(signinRequest, this.response));
  }

  @Test
  void signIn_ShouldThrowUnauthorized_WhenDatabaseErrorOccurs() {
    when(userService.checkCredentials(signinRequest)).thenThrow(new DataAccessException("DB Error") {});

    assertThrows(ResponseStatusException.class, () -> tokenController.signIn(signinRequest, this.response));
    assertThrows(ResponseStatusException.class, () -> tokenController.signIn(signinRequest, this.response));
  }

  @Test
  void registerAccount_ShouldReturnToken_WhenUserIsCreated() {
    when(userService.getUserByEmail(EMAIL)).thenReturn(user);
    when(jwtUtils.generateToken(USER_ID, ROLE)).thenReturn(TOKEN);

    boolean result = tokenController.registerAccount(signupRequest, response);

    assertTrue(result);
    verify(userService).createUser(signupRequest);
    verify(userService).getUserByEmail(EMAIL);
    verify(jwtUtils).generateToken(USER_ID, ROLE);
    verify(jwtUtils).setJWTCookie(TOKEN, response);
  }

  @Test
  void registerAccount_ShouldThrowBadRequest_WhenDatabaseErrorOccurs() {
    doThrow(new DataAccessException("DB Error") {}).when(userService).createUser(signupRequest);

    assertThrows(ResponseStatusException.class, () -> tokenController.registerAccount(signupRequest, response));
  }

  @Test
  void logout_ShouldSetLogoutCookieAndReturnSuccessMessage() {
      ResponseEntity<?> responseEntity = tokenController.logout(response);

      assertEquals("Logged out successfully", responseEntity.getBody());
      assertEquals(200, responseEntity.getStatusCodeValue());
      verify(jwtUtils).setLogOutJWTCookie(response);
      verifyNoMoreInteractions(jwtUtils);
  }
}
