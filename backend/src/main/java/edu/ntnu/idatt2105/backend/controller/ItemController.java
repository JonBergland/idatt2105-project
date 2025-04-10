package edu.ntnu.idatt2105.backend.controller;

import edu.ntnu.idatt2105.backend.dto.item.ItemRequest;
import edu.ntnu.idatt2105.backend.dto.item.ItemResponse;
import edu.ntnu.idatt2105.backend.dto.item.ItemsRequest;
import edu.ntnu.idatt2105.backend.dto.item.ItemsResponse;
import edu.ntnu.idatt2105.backend.mapper.ItemMapper;
import edu.ntnu.idatt2105.backend.service.ItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/**
 * A controller for getting items.
 */
@RestController
@RequestMapping(value = "/api/store/item")
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
  @PostMapping("/filter")
  @Operation(summary = "Filter items",
      description = "Get items based on optional filters, sorting and pagination")
  public ItemsResponse getItems(@RequestBody ItemsRequest itemsRequest) {
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
  @PostMapping("/get")
  @Operation(summary = "Get item", description = "Get an item with its general info")
  public ItemResponse itemResponses(@RequestBody ItemRequest itemRequest) {
    try {
      logger.info("getting item with id: {}", itemRequest.getItemID());
      return itemService.getItem(itemRequest);
    } catch (DataAccessException e) {
      logger.warn("could not retrieve item: {}", e.getMessage());
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
  }
}
