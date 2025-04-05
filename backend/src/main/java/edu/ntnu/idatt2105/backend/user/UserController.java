package edu.ntnu.idatt2105.backend.user;

import edu.ntnu.idatt2105.backend.user.dto.AddItemRequest;
import edu.ntnu.idatt2105.backend.user.dto.EditItemRequest;
import edu.ntnu.idatt2105.backend.user.dto.GetUserInfoResponse;
import edu.ntnu.idatt2105.backend.user.dto.UpdateUserInfoRequest;
import edu.ntnu.idatt2105.backend.user.model.User;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

@RestController
@RequestMapping(value = "/api/user")
@CrossOrigin
@RequiredArgsConstructor
public class UserController {

  private final Logger logger = LoggerFactory.getLogger(UserController.class);

  private final UserService userService;

  @PostMapping("/info")
  public void updateUserInfo(@RequestBody UpdateUserInfoRequest updateUserInfoRequest) {
    logger.info("hei");
    userService.editUser(updateUserInfoRequest);
  }

  @GetMapping("/info")
  public ResponseEntity<GetUserInfoResponse> getUserInfo() {
    String userID = SecurityContextHolder.getContext().getAuthentication().getName();

    // Check if no user is logged in
    if ("anonymousUser".equals(userID)) {
        return ResponseEntity.ok(null);
    }

    try {
      int userId = Integer.parseInt(userID);
      return ResponseEntity.ok(userService.getUser(userId));
    } catch (NumberFormatException e) {
        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, 
            "Invalid user ID format: " + userID);
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

  @GetMapping("/item")
  public void getUserItems() {

  }

  @PostMapping("/item")
  public void addUserItem(@RequestBody AddItemRequest addItemRequest) {
    userService.addUserItem(addItemRequest);
  }

  @PostMapping("/item/edit")
  public void editUserItem(@RequestBody EditItemRequest editItemRequest) {
    userService.editUserItem(editItemRequest);
  }
}
