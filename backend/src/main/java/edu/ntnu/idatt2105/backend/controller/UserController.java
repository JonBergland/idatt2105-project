package edu.ntnu.idatt2105.backend.controller;

import edu.ntnu.idatt2105.backend.dto.user.GetUserInfoResponse;
import edu.ntnu.idatt2105.backend.dto.user.UpdateUserInfoRequest;
import edu.ntnu.idatt2105.backend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/**
 * a controller for endpoints related to user.
 */
@RestController
@RequestMapping(value = "/api/")
@CrossOrigin
@Tag(name = "User controller", description = "User management endpoints")
@RequiredArgsConstructor
public class UserController {

  private final Logger logger = LoggerFactory.getLogger(UserController.class);

  private final UserService userService;

  /**
   * endpoint for editing a user's info.
   *
   * @param updateUserInfoRequest new user info
   */
  @PostMapping("/user/info")
  @Operation(summary = "Edit user", description = "Edit the logged in user's info")
  public void updateUserInfo(@RequestBody @Valid UpdateUserInfoRequest updateUserInfoRequest) {
    logger.info("update user info request");
    try {
      userService.editUser(updateUserInfoRequest);
    } catch (DataAccessException e) {
      logger.warn("Could not update user: {}", e.getMessage());
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
  }

  /**
   * endpoint for getting a user's info.
   *
   * @return the user info to send
   */
  @GetMapping("/user/info")
  @Operation(summary = "Get user info", description = "Get the info of the logged in user")
  public ResponseEntity<GetUserInfoResponse> getUserInfo() {
    String userID = SecurityContextHolder.getContext().getAuthentication().getName();

    // Check if no user is logged in
    if ("anonymousUser".equals(userID)) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    try {
      int userId = Integer.parseInt(userID);
      return ResponseEntity.ok(userService.getUser(userId));
    } catch (DataAccessException e) {
      logger.warn("Could not retrieve user info: {}", e.getMessage());
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
