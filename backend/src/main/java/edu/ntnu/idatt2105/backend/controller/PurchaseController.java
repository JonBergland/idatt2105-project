package edu.ntnu.idatt2105.backend.controller;

import edu.ntnu.idatt2105.backend.dto.purchase.BuyItemFromBidRequest;
import edu.ntnu.idatt2105.backend.dto.purchase.BuyItemRequest;
import edu.ntnu.idatt2105.backend.service.PurchaseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/**
 * a controller for endpoints related to purchases.
 */
@RestController
@RequestMapping(value = "/api/")
@CrossOrigin
@Tag(name = "Purchase controller", description = "Purchase management endpoints")
@RequiredArgsConstructor
public class PurchaseController {

  private final Logger logger = LoggerFactory.getLogger(PurchaseController.class);

  private final PurchaseService purchaseService;

  /**
   * endpoint for directly buying an item.
   *
   * @param buyItemRequest the item
   */
  @PostMapping("/user/item/buy")
  @Operation(summary = "Buy item",
      description = "Buy an item marked as available for the logged in user")
  public void buyItem(@RequestBody @Valid BuyItemRequest buyItemRequest) {
    logger.info("direct buy request");
    try {
      purchaseService.buyItem(buyItemRequest);
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
  @PostMapping("/user/item/bids/buy")
  @Operation(summary = "Buy from bid",
      description = "Buy an item with price from a bid when item is marked as "
          + "available or reserved for the logged in user")
  public void buyBidItem(@RequestBody @Valid BuyItemFromBidRequest buyItemFromBidRequest) {
    logger.info("buy from bid request");
    try {
      purchaseService.buyItemFromBid(buyItemFromBidRequest);
    } catch (DataAccessException | IllegalArgumentException e) {
      logger.warn("could not buy item: {}", e.getMessage());
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
  }
}
