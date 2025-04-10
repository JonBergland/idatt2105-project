package edu.ntnu.idatt2105.backend.controller;

import edu.ntnu.idatt2105.backend.dto.item.ItemRequest;
import edu.ntnu.idatt2105.backend.dto.item.ItemsResponse;
import edu.ntnu.idatt2105.backend.service.UserService;
import edu.ntnu.idatt2105.backend.dto.user.AddItemRequest;
import edu.ntnu.idatt2105.backend.dto.user.AnswerBidRequest;
import edu.ntnu.idatt2105.backend.dto.user.BuyItemFromBidRequest;
import edu.ntnu.idatt2105.backend.dto.user.BuyItemRequest;
import edu.ntnu.idatt2105.backend.dto.user.DeleteItemRequest;
import edu.ntnu.idatt2105.backend.dto.user.EditItemRequest;
import edu.ntnu.idatt2105.backend.dto.user.GetBidsOnItemByUserRequest;
import edu.ntnu.idatt2105.backend.dto.user.GetBidsOnItemByUserResponse;
import edu.ntnu.idatt2105.backend.dto.user.GetBookmarkedItemsRequest;
import edu.ntnu.idatt2105.backend.dto.user.GetBookmarkedItemsResponse;
import edu.ntnu.idatt2105.backend.dto.user.GetStoreItemResponse;
import edu.ntnu.idatt2105.backend.dto.user.GetUserInfoResponse;
import edu.ntnu.idatt2105.backend.dto.user.GetYourBidItemsRequest;
import edu.ntnu.idatt2105.backend.dto.user.GetYourBidItemsResponse;
import edu.ntnu.idatt2105.backend.dto.user.GetYourItemBidsRequest;
import edu.ntnu.idatt2105.backend.dto.user.GetYourItemBidsResponse;
import edu.ntnu.idatt2105.backend.dto.user.GetYourUniqueBidsRequest;
import edu.ntnu.idatt2105.backend.dto.user.GetYourUniqueBidsResponse;
import edu.ntnu.idatt2105.backend.dto.user.PlaceBidRequest;
import edu.ntnu.idatt2105.backend.dto.user.ToggleBookmarkRequest;
import edu.ntnu.idatt2105.backend.dto.user.UpdateUserInfoRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.nio.file.AccessDeniedException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
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
  @PostMapping("/info")
  @Operation(summary = "Edit user", description = "Edit the logged in user's info")
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

  /**
   * endpoint for getting item from store with user specific info.
   *
   * @param itemRequest the item to get
   * @return the item info
   */
  @PostMapping("/item/store")
  @Operation(summary = "Get item", description = "Get item from store with user specific info")
  public GetStoreItemResponse getUserSpecificItemInfo(@RequestBody ItemRequest itemRequest) {
    logger.info("get item with user specific info request");
    try {
      return userService.getUserSpecificItemInfo(itemRequest);
    } catch (DataAccessException e) {
      logger.warn("could not get item: {}", e.getMessage());
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
  }

  /**
   * endpoint for deleting a user's item.
   *
   * @param deleteItemRequest the item
   */
  @DeleteMapping("/item/delete")
  @Operation(summary = "Delete item", description = "Delete an item owned by the logged in user")
  public void deleteUserItem(@RequestBody DeleteItemRequest deleteItemRequest) {
    logger.info("delete user's item request");
    try {
      userService.deleteUserItem(deleteItemRequest);
    } catch (DataAccessException e) {
      logger.warn("could not delete item: {}", e.getMessage());
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
  }

  @GetMapping("/item/recommended")
  public void getRecommendedItems() {

  }

  /**
   * endpoint for toggling bookmark on an item.
   *
   * @param toggleBookmarkRequest the item to toggle
   */
  @PostMapping("/item/bookmark")
  @Operation(
      summary = "Toggle bookmark", description = "Toggle bookmark on item for the logged in user")
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
   * endpoint for getting the items bookmarked by user.
   *
   * @param getBookmarkedItemsRequest the segment and offset
   * @return the items
   */
  @PostMapping("/item/bookmark/get")
  @Operation(
      summary = "Get bookmarked items",
      description = "Get all items bookmarked by the logged in user, paginated")
  public GetBookmarkedItemsResponse[] getBookmarkeditems(
      @RequestBody GetBookmarkedItemsRequest getBookmarkedItemsRequest) {
    logger.info("get bookmarked items request");
    try {
      return userService.getBookmarkedItems(getBookmarkedItemsRequest);
    } catch (DataAccessException e) {
      logger.warn("could not get items: {}", e.getMessage());
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
  }

  /**
   * endpoint for placing a bid.
   *
   * @param placeBidRequest the bid to place
   */
  @PostMapping("/item/bid/place")
  @Operation(summary = "Place bid",
      description = "Place bid on available or reserved item "
          + "for logged in user, makes available item reserved")
  public void placeBid(@RequestBody PlaceBidRequest placeBidRequest) {
    logger.info("place bid request");
    try {
      userService.placeBid(placeBidRequest);
    } catch (DataAccessException | IllegalArgumentException e) {
      logger.warn("could not place bid: {}", e.getMessage());
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
  }

  /**
   * endpoint for getting distinct items you have placed bids on.
   *
   * @return the items
   */
  @PostMapping("/item/bids/item")
  @Operation(summary = "Get bid items", description = "Get items the logged in user has bid on, paginated")
  public GetYourUniqueBidsResponse[] getUniqueBids(
      @RequestBody GetYourUniqueBidsRequest getYourUniqueBidsRequest) {
    logger.info("get distinct items user has placed bids on request");
    try {
      return userService.getYourBids(getYourUniqueBidsRequest);
    } catch (DataAccessException e) {
      logger.warn("could not get distinct items: {}", e.getMessage());
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
  }

  /**
   * endpoint for getting bids you have placed on an item.
   *
   * @param getYourItemBidsRequest the item
   * @return the bids
   */
  @PostMapping("/item/bids")
  @Operation(summary = "Get bids", description = "Get bids the logged in user has placed on a specific item, paginated")
  public GetYourItemBidsResponse[] getBidsOnItem(
      @RequestBody GetYourItemBidsRequest getYourItemBidsRequest) {
    logger.info("get bids user has placed on item request");
    try {
      return userService.getYourItemBids(getYourItemBidsRequest);
    } catch (DataAccessException e) {
      logger.warn("could not get bids: {}", e.getMessage());
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
  }

  /**
   * accept or decline a bid placed on your item.
   *
   * @param answerBidRequest the item to answer
   */
  @PostMapping("/item/bid/answer")
  @Operation(summary = "Answer bid", description = "Accept or decline bid if logged in user owns bid item")
  public void answerBid(@RequestBody AnswerBidRequest answerBidRequest) {
    logger.info("answer bid request");
    try {
      userService.answerBid(answerBidRequest);
    } catch (AccessDeniedException | IllegalArgumentException | DataAccessException e) {
      logger.warn("could not answer bid: {}", e.getMessage());
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
  }

  /**
   * get users who have bid on your items.
   *
   * @return the users
   */
  @PostMapping("/item/bid/users")
  @Operation(summary = "Get bidding users", description = "Get users who have bid on an item owned by the logged in user, paginated")
  public GetYourBidItemsResponse[] getBidsOnYourItems(
      @RequestBody GetYourBidItemsRequest getYourBidItemsRequest) {
    logger.info("get users who bid on item request");
    try {
      return userService.getBids(getYourBidItemsRequest);
    } catch (DataAccessException e) {
      logger.warn("could not get users: {}", e.getMessage());
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
  }

  /**
   * endpoint for getting bids made by a user on an item.
   *
   * @param getBidsOnItemByUserRequest the item and user
   * @return the bids
   */
  @PostMapping("/item/bid/")
  @Operation(summary = "Get bid by user", description = "Get all bids made by a user on an item owned by logged in user, paginated")
  public GetBidsOnItemByUserResponse[] getBidsOnYourItem(
      @RequestBody GetBidsOnItemByUserRequest getBidsOnItemByUserRequest) {
    logger.info("get bids on item by user request");
    try {
      return userService.getBidsOnYourItem(getBidsOnItemByUserRequest);
    } catch (DataAccessException e) {
      logger.warn("could not get bids: {}", e.getMessage());
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
  }

  /**
   * endpoint for getting a user's items.
   *
   * @return the user's items
   */
  @GetMapping("/item")
  @Operation(summary = "Get own items", description = "Get all items owned by the logged in user")
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
  @Operation(summary = "Add item", description = "Add item to store for the logged in user")
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
  @Operation(summary = "Edit item", description = "Edit an item owned by logged in user")
  public void editUserItem(@RequestBody EditItemRequest editItemRequest) {
    logger.info("edit item for user request");
    try {
      userService.editUserItem(editItemRequest);
    } catch (DataAccessException e) {
      logger.warn("could not edit item for user: {}", e.getMessage());
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
  }

  /**
   * endpoint for directly buying an item.
   *
   * @param buyItemRequest the item
   */
  @PostMapping("/item/buy")
  @Operation(summary = "Buy item", description = "Buy an item marked as available for the logged in user")
  public void buyItem(@RequestBody BuyItemRequest buyItemRequest) {
    logger.info("direct buy request");
    try {
      userService.buyItem(buyItemRequest);
    } catch (DataAccessException | IllegalArgumentException e) {
      logger.warn("could not buy item: {}", e.getMessage());
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
  }

  /**
   * endpoint for buying through accepted bid.
   *
   * @param buyItemFromBidRequest the bid
   */
  @PostMapping("/item/bids/buy")
  @Operation(summary = "Buy from bid", description = "Buy an item with price from a bid when item is marked as available or reserved for the logged in user")
  public void buyBidItem(@RequestBody BuyItemFromBidRequest buyItemFromBidRequest) {
    logger.info("buy from bid request");
    try {
      userService.buyItemFromBid(buyItemFromBidRequest);
    } catch (DataAccessException | IllegalArgumentException e) {
      logger.warn("could not buy item: {}", e.getMessage());
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
  }
}
