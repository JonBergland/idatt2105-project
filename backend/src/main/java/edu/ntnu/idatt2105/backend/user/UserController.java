package edu.ntnu.idatt2105.backend.user;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/user")
@CrossOrigin
@RequiredArgsConstructor
public class UserController {

  private final Logger logger = LoggerFactory.getLogger(UserController.class);

  @PostMapping("/info")
  public void updateUserInfo() {

  }

  @GetMapping("/info")
  public void getUserInfo() {

  }

  @GetMapping("/item")
  public void getUserItems() {

  }

  @PostMapping("/item")
  public void addUserItem() {

  }

  @PostMapping("/item/edit")
  public void editUserItem() {

  }
}
