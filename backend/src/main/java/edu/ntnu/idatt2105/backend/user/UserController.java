package edu.ntnu.idatt2105.backend.user;

import edu.ntnu.idatt2105.backend.item.dto.ItemRequest;
import edu.ntnu.idatt2105.backend.item.dto.ItemsResponse;
import edu.ntnu.idatt2105.backend.user.dto.AddItemRequest;
import edu.ntnu.idatt2105.backend.user.dto.AnswerBidRequest;
import edu.ntnu.idatt2105.backend.user.dto.GetBidsOnItemByUserRequest;
import edu.ntnu.idatt2105.backend.user.dto.GetBidsOnItemByUserResponse;
import edu.ntnu.idatt2105.backend.user.dto.GetYourBidItemsRequest;
import edu.ntnu.idatt2105.backend.user.dto.GetYourBidItemsResponse;
import edu.ntnu.idatt2105.backend.user.dto.GetYourUniqueBidsResponse;
import edu.ntnu.idatt2105.backend.user.dto.GetYourItemBidsRequest;
import edu.ntnu.idatt2105.backend.user.dto.GetYourItemBidsResponse;
import edu.ntnu.idatt2105.backend.user.dto.GetStoreItemResponse;
import edu.ntnu.idatt2105.backend.user.dto.GetYourUniqueBidsRequest;
import edu.ntnu.idatt2105.backend.user.dto.PlaceBidRequest;
import edu.ntnu.idatt2105.backend.user.dto.ToggleBookmarkRequest;
import edu.ntnu.idatt2105.backend.user.dto.EditItemRequest;
import edu.ntnu.idatt2105.backend.user.dto.GetUserInfoResponse;
import edu.ntnu.idatt2105.backend.user.dto.UpdateUserInfoRequest;
import java.nio.file.AccessDeniedException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.dao.DataAccessException;
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
  public ResponseEntity<GetUserInfoResponse> getUserInfo() {
    String userID = SecurityContextHolder.getContext().getAuthentication().getName();

    // Check if no user is logged in
    if ("anonymousUser".equals(userID)) {
        return ResponseEntity.ok(null);
    }

    try {
      int userId = Integer.parseInt(userID);
      return ResponseEntity.ok(userService.getUser(userId));
    } catch (DataAccessException e) {
      logger.warn("Could not retrieve user info: {}", e.getMessage());
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  /**
   * endpoint for getting item from store with user specific info.
   *
   * @param itemRequest the item to get
   * @return the item info
   */
  @PostMapping("/item/store")
  public GetStoreItemResponse getUserSpecificItemInfo(@RequestBody ItemRequest itemRequest) {
    logger.info("get item with user specific info request");
    try {
      return userService.getUserSpecificItemInfo(itemRequest);
    } catch (DataAccessException e) {
      logger.warn("could not get item: {}", e.getMessage());
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
  }

  @GetMapping("/item/recommended")
  public void getRecommendedItems() {

  }

  @PostMapping("/item/bookmark")
  public void bookmarkItem(@RequestBody ToggleBookmarkRequest toggleBookmarkRequest) {
    logger.info("toggling bookmark request");
    try {
      userService.toggleBookmark(toggleBookmarkRequest);
    } catch (DataAccessException e) {
      logger.warn("could not toggle bookmark: {}", e.getMessage());
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
  }

  /**
   * endpoint for placing a bid.
   *
   * @param placeBidRequest the bid to place
   */
  @PostMapping("/item/bid/place")
  public void placeBid(@RequestBody PlaceBidRequest placeBidRequest) {
    try {
      userService.placeBid(placeBidRequest);
    } catch (Exception e) {

    }
  }

  /**
   * endpoint for getting distinct items you have placed bids on.
   *
   * @return the items
   */
  @PostMapping("/item/bids/item")
  public GetYourUniqueBidsResponse[] getUniqueBids(@RequestBody
                                                   GetYourUniqueBidsRequest getYourUniqueBidsRequest) {
    return userService.getYourBids(getYourUniqueBidsRequest);
  }

  /**
   * endpoint for getting bids you have placed on an item.
   *
   * @param getYourItemBidsRequest the item
   * @return the bids
   */
  @PostMapping("/item/bids")
  public GetYourItemBidsResponse[] getBidsOnItem(@RequestBody GetYourItemBidsRequest getYourItemBidsRequest) {
    return userService.getYourItemBids(getYourItemBidsRequest);
  }

  /**
   * accept or decline a bid placed on your item.
   *
   * @param answerBidRequest the item to answer
   */
  @PostMapping("/item/bid/answer")
  public void answerBid(@RequestBody AnswerBidRequest answerBidRequest) {
    try {
      userService.answerBid(answerBidRequest);
    } catch (AccessDeniedException e) {
      logger.warn("permission denied: {}", e.getMessage());
    } catch (Exception e) {

    }
  }

  /**
   * get users who have bid on your items.
   *
   * @return the users
   */
  @PostMapping("/item/bid/users")
  public GetYourBidItemsResponse[] getBidsOnYourItems(@RequestBody GetYourBidItemsRequest getYourBidItemsRequest) {
    return userService.getBids(getYourBidItemsRequest);
  }

  @PostMapping("/item/bid/")
  public GetBidsOnItemByUserResponse[] getBidsOnYourItem(@RequestBody GetBidsOnItemByUserRequest getBidsOnItemByUserRequest) {
    return userService.getBidsOnYourItem(getBidsOnItemByUserRequest);
  }

  /**
   * endpoint for getting a user's items.
   *
   * @return the user's items
   */
  @GetMapping("/item")
  public ItemsResponse getUserItems() {
    logger.info("get all items by user request");
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
      logger.warn("could not edit item for user: {}", e.getMessage());
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
  }
}
