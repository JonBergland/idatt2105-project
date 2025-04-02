package edu.ntnu.idatt2105.backend.security;

import edu.ntnu.idatt2105.backend.security.dto.SigninRequest;
import edu.ntnu.idatt2105.backend.security.dto.SigninResponse;
import edu.ntnu.idatt2105.backend.security.dto.SignupRequest;
import edu.ntnu.idatt2105.backend.user.UserService;
import edu.ntnu.idatt2105.backend.user.model.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping(value = "/api/token")
@CrossOrigin
@RequiredArgsConstructor
public class TokenController {

  private final UserService userService;

  private final JWTUtils jwtUtils;

  private final Logger logger = LoggerFactory.getLogger(TokenController.class);

  @PostMapping("/signin")
  @ResponseStatus(value = HttpStatus.CREATED)
  public SigninResponse signIn(final @Valid @RequestBody SigninRequest signinRequest)
      throws ResponseStatusException {
    logger.info("token request for user: {}", signinRequest.getEmail());
    try {
      if (userService.checkCredentials(signinRequest)) {
        User user = userService.getUserByEmail(signinRequest.getEmail());
        logger.info("role: {}", user.getRole());
        String token = jwtUtils.generateToken(user.getUserID(), user.getRole());
        logger.info("token: {}", token);
        return new SigninResponse(token);
      }
      logger.info("Access denied, wrong credentials for user {}", signinRequest.getEmail());
    } catch (DataAccessException e) {
      logger.warn("could not sign in user, {}", e.getMessage());
    }
    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
  }

  @PostMapping("/signup")
  @ResponseStatus(value = HttpStatus.CREATED)
  public SigninResponse registerAccount(final @Valid @RequestBody SignupRequest signupRequest) {
    logger.info("signup request with email: {}", signupRequest.getEmail());
    try {
      userService.createUser(signupRequest);
      User createdUser = userService.getUserByEmail(signupRequest.getEmail());
      String token = jwtUtils.generateToken(createdUser.getUserID(), createdUser.getRole());
      return new SigninResponse(token);
    } catch (DataAccessException e) {
      logger.warn("could not create user, {}", e.getMessage());
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
  }
}
