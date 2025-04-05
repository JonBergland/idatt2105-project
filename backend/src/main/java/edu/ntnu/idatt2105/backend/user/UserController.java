package edu.ntnu.idatt2105.backend.user;

import edu.ntnu.idatt2105.backend.item.dto.ItemsResponse;
import edu.ntnu.idatt2105.backend.user.dto.AddItemRequest;
import edu.ntnu.idatt2105.backend.user.dto.EditItemRequest;
import edu.ntnu.idatt2105.backend.user.dto.GetUserInfoResponse;
import edu.ntnu.idatt2105.backend.user.dto.UpdateUserInfoRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/**
 * a controller for user specific operations.
 */
@RestController
@RequestMapping(value = "/api/user")
@CrossOrigin
@RequiredArgsConstructor
public class UserController {

  private final Logger logger = LoggerFactory.getLogger(UserController.class);

  private final UserService userService;

  /**
   * endpoint for editing a user's info.
   *
   * @param updateUserInfoRequest new user info
   */
  @PostMapping("/info")
  public void updateUserInfo(@RequestBody UpdateUserInfoRequest updateUserInfoRequest) {
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
  @GetMapping("/info")
  public GetUserInfoResponse getUserInfo() {
    logger.info("get user info request");
    String userID = SecurityContextHolder.getContext().getAuthentication().getName();
    try {
      return userService.getUser(Integer.parseInt(userID));
    } catch (DataAccessException e) {
      logger.warn("Could not retrieve user info: {}", e.getMessage());
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PostMapping("/item/store")
  public void getUserSpecificItemInfo() {

  }

  @GetMapping("/item/recommended")
  public void getRecommendedItems() {

  }

  @PostMapping("/item/bookmark")
  public void bookmarkItem() {

  }

  /**
   * endpoint for getting a user's items.
   *
   * @return the user's items
   */
  @GetMapping("/item")
  public ItemsResponse getUserItems() {
    logger.info("getting all items by user");
    try {
      return userService.getUserItems();
    } catch (DataAccessException e) {
      logger.warn("Could not get items by user: {}", e.getMessage());
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  /**
   * endpoint for user to add item.
   *
   * @param addItemRequest the item to add
   */
  @PostMapping("/item")
  public void addUserItem(@RequestBody AddItemRequest addItemRequest) {
    logger.info("add item for user request");
    try {
      userService.addUserItem(addItemRequest);
    } catch (DataAccessException e) {
      logger.warn("Could not add item for user: {}", e.getMessage());
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
  }

  /**
   * endpoint for editing a user's item.
   *
   * @param editItemRequest the updated item
   */
  @PostMapping("/item/edit")
  public void editUserItem(@RequestBody EditItemRequest editItemRequest) {
    logger.info("edit item for user request");
    try {
      userService.editUserItem(editItemRequest);
    } catch (DataAccessException e) {
      logger.warn("could not edit item for user:", e.getMessage());
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
  }
}
