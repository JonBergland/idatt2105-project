package edu.ntnu.idatt2105.backend.security;

import edu.ntnu.idatt2105.backend.security.dto.SigninRequest;
import edu.ntnu.idatt2105.backend.security.dto.SignupRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import edu.ntnu.idatt2105.backend.user.UserService;
import edu.ntnu.idatt2105.backend.user.model.User;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/**
 * A controller for password authentication and token generation.
 */
@RestController
@RequestMapping(value = "/api/token")
@CrossOrigin
@Tag(name = "Token controller", description = "Authentication and JWT distribution")
@RequiredArgsConstructor
public class TokenController {

  private final UserService userService;

  private final JWTUtils jwtUtils;

  private final Logger logger = LoggerFactory.getLogger(TokenController.class);

  /**
   * Endpoint for signing in.
   * This method generates a JWT-cookie, sets the token to the response and returns a boolean
   * representing the success of the sign in
   *
   * @param signinRequest contains signin information
   * @param response      the HTTP response object coming with the request
   * @return              a boolean representing the success of the sing in
   * @throws ResponseStatusException if wrong credentials were passed in
   */
  @PostMapping("/signin")
  @ResponseStatus(value = HttpStatus.CREATED)
  @Operation(summary = "Sign in", description = "Sign in a user and provide a JWT cookie")
  public boolean signIn(
      final @Valid @RequestBody SigninRequest signinRequest, HttpServletResponse response)
      throws ResponseStatusException {
    logger.info("token request for user: {}", signinRequest.getEmail());
    try {
      if (userService.checkCredentials(signinRequest)) {
        User user = userService.getUserByEmail(signinRequest.getEmail());
        logger.info("role: {}", user.getRole());
        String token = jwtUtils.generateToken(user.getUserID(), user.getRole());
        logger.info("token: {}", token);
        jwtUtils.setJWTCookie(token, response);
        return true;
      }
      logger.info("Access denied, wrong credentials for user {}", signinRequest.getEmail());
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
    } catch (DataAccessException e) {
      logger.warn("could not sign in user, {}", e.getMessage());
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    }
  }

  /**
   * Endpoint for signing up.
   * This method generates a JWT-cookie, sets the token to the response and returns a boolean
   * representing the success of the sign-up
   *
   * @param signupRequest contains signup information
   * @param response      the HTTP response object coming with the request
   * @return              a boolean representing the success of the sing up
   * @throws ResponseStatusException if wrong credentials were passed in
   */
  @PostMapping("/signup")
  @ResponseStatus(value = HttpStatus.CREATED)
  @Operation(summary = "Signup", description = "Signup a new user and provide a JWT cookie")
  public boolean registerAccount(
      final @Valid @RequestBody SignupRequest signupRequest, HttpServletResponse response) {
    logger.info("signup request with email: {}", signupRequest.getEmail());
    try {
      userService.createUser(signupRequest);
      User createdUser = userService.getUserByEmail(signupRequest.getEmail());
      String token = jwtUtils.generateToken(createdUser.getUserID(), createdUser.getRole());
      jwtUtils.setJWTCookie(token, response);
      return true;
    } catch (DataAccessException e) {
      logger.warn("could not create user, {}", e.getMessage());
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
  }

  /**
   * Endpoint for logging out a user.
   * This method set an expired JWT cookie to the response, thereby logging out the user.
   *
   * @param response the HTTP response object comming with the request
   * @return         a {@link ResponseEntity} containing a confirmation message of successful logout
   */
  @PostMapping("/logout")
  @Operation(summary = "Logout", description = "Logout the user and set logout JWT cookie")
  public ResponseEntity<?> logout(HttpServletResponse response) {
    jwtUtils.setLogOutJWTCookie(response);
    logger.info("User logged out successfully.");
    return ResponseEntity.ok("Logged out successfully");
  }
}
