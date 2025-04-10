package edu.ntnu.idatt2105.backend.controller;

import edu.ntnu.idatt2105.backend.dto.item.AddItemRequest;
import edu.ntnu.idatt2105.backend.dto.item.DeleteItemRequest;
import edu.ntnu.idatt2105.backend.dto.item.EditItemRequest;
import edu.ntnu.idatt2105.backend.dto.item.GetBookmarkedItemsRequest;
import edu.ntnu.idatt2105.backend.dto.item.GetBookmarkedItemsResponse;
import edu.ntnu.idatt2105.backend.dto.item.GetStoreItemResponse;
import edu.ntnu.idatt2105.backend.dto.item.ItemRequest;
import edu.ntnu.idatt2105.backend.dto.item.ItemResponse;
import edu.ntnu.idatt2105.backend.dto.item.ItemsRequest;
import edu.ntnu.idatt2105.backend.dto.item.ItemsResponse;
import edu.ntnu.idatt2105.backend.mapper.ItemMapper;
import edu.ntnu.idatt2105.backend.service.ItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/**
 * a controller for endpoints related to items.
 */
@RestController
@RequestMapping(value = "/api/")
@CrossOrigin
@Tag(name = "Item controller", description = "Item management endpoints")
@RequiredArgsConstructor
public class ItemController {

  private final Logger logger = LoggerFactory.getLogger(ItemController.class);

  private final ItemService itemService;

  /**
   * Endpoint for getting items based on filters.
   *
   * @param itemsRequest the request with filters
   * @return the filtered items
   */
  @PostMapping("/store/item/filter")
  @Operation(summary = "Filter items",
      description = "Get items based on optional filters, sorting and pagination")
  public ItemsResponse getItems(@RequestBody @Valid ItemsRequest itemsRequest) {
    ItemResponse[] itemResponses = ItemMapper.INSTANCE.itemsToItemResponses(
        itemService.getItems(itemsRequest));
    return new ItemsResponse(itemResponses);
  }

  /**
   * Endpoint for getting an item from id.
   *
   * @param itemRequest the request with id
   * @return the item
   */
  @PostMapping("/store/item/get")
  @Operation(summary = "Get item", description = "Get an item with its general info")
  public ItemResponse itemResponses(@RequestBody @Valid ItemRequest itemRequest) {
    try {
      logger.info("getting item with id: {}", itemRequest.getItemID());
      return itemService.getItem(itemRequest);
    } catch (DataAccessException e) {
      logger.warn("could not retrieve item: {}", e.getMessage());
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
  }

  /**
   * endpoint for getting item from store with user specific info.
   *
   * @param itemRequest the item to get
   * @return the item info
   */
  @PostMapping("/user/item/store")
  @Operation(summary = "Get item", description = "Get item from store with user specific info")
  public GetStoreItemResponse getUserSpecificItemInfo(@RequestBody @Valid ItemRequest itemRequest) {
    logger.info("get item with user specific info request");
    try {
      return itemService.getUserSpecificItemInfo(itemRequest);
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
  @DeleteMapping("/user/item/delete")
  @Operation(summary = "Delete item", description = "Delete an item owned by the logged in user")
  public void deleteUserItem(@RequestBody @Valid DeleteItemRequest deleteItemRequest) {
    logger.info("delete user's item request");
    try {
      itemService.deleteUserItem(deleteItemRequest);
    } catch (DataAccessException e) {
      logger.warn("could not delete item: {}", e.getMessage());
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
  }

  /**
   * endpoint for getting a user's items.
   *
   * @return the user's items
   */
  @GetMapping("/user/item")
  @Operation(summary = "Get own items", description = "Get all items owned by the logged in user")
  public ItemsResponse getUserItems() {
    logger.info("get all items by user request");
    try {
      return itemService.getUserItems();
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
  @PostMapping("/user/item")
  @Operation(summary = "Add item", description = "Add item to store for the logged in user")
  public void addUserItem(@RequestBody @Valid AddItemRequest addItemRequest) {
    logger.info("add item for user request");
    try {
      itemService.addUserItem(addItemRequest);
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
  @PostMapping("/user/item/edit")
  @Operation(summary = "Edit item", description = "Edit an item owned by logged in user")
  public void editUserItem(@RequestBody @Valid EditItemRequest editItemRequest) {
    logger.info("edit item for user request");
    try {
      itemService.editUserItem(editItemRequest);
    } catch (DataAccessException e) {
      logger.warn("could not edit item for user: {}", e.getMessage());
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
  }

  /**
   * endpoint for getting the items bookmarked by user.
   *
   * @param getBookmarkedItemsRequest the segment and offset
   * @return the items
   */
  @PostMapping("/user/item/bookmark/get")
  @Operation(
      summary = "Get bookmarked items",
      description = "Get all items bookmarked by the logged in user, paginated")
  public GetBookmarkedItemsResponse[] getBookmarkeditems(
      @RequestBody @Valid GetBookmarkedItemsRequest getBookmarkedItemsRequest) {
    logger.info("get bookmarked items request");
    try {
      return itemService.getBookmarkedItems(getBookmarkedItemsRequest);
    } catch (DataAccessException e) {
      logger.warn("could not get items: {}", e.getMessage());
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
  }
}
