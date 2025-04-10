package edu.ntnu.idatt2105.backend.controller;

import edu.ntnu.idatt2105.backend.dto.bid.AnswerBidRequest;
import edu.ntnu.idatt2105.backend.dto.bid.GetBidsOnItemByUserRequest;
import edu.ntnu.idatt2105.backend.dto.bid.GetBidsOnItemByUserResponse;
import edu.ntnu.idatt2105.backend.dto.bid.GetYourBidItemsRequest;
import edu.ntnu.idatt2105.backend.dto.bid.GetYourBidItemsResponse;
import edu.ntnu.idatt2105.backend.dto.bid.GetYourItemBidsRequest;
import edu.ntnu.idatt2105.backend.dto.bid.GetYourItemBidsResponse;
import edu.ntnu.idatt2105.backend.dto.bid.GetYourUniqueBidsRequest;
import edu.ntnu.idatt2105.backend.dto.bid.GetYourUniqueBidsResponse;
import edu.ntnu.idatt2105.backend.dto.bid.PlaceBidRequest;
import edu.ntnu.idatt2105.backend.service.BidService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import java.nio.file.AccessDeniedException;
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

@RestController
@RequestMapping(value = "/api/")
@CrossOrigin
@RequiredArgsConstructor
public class BidController {

  private final Logger logger = LoggerFactory.getLogger(BidController.class);

  private final BidService bidService;

  /**
   * endpoint for placing a bid.
   *
   * @param placeBidRequest the bid to place
   */
  @PostMapping("/user/item/bid/place")
  @Operation(summary = "Place bid",
      description = "Place bid on available or reserved item "
          + "for logged in user, makes available item reserved")
  public void placeBid(@RequestBody @Valid PlaceBidRequest placeBidRequest) {
    logger.info("place bid request");
    try {
      bidService.placeBid(placeBidRequest);
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
  @PostMapping("/user/item/bids/item")
  @Operation(summary = "Get bid items", description = "Get items the logged in user has bid on, paginated")
  public GetYourUniqueBidsResponse[] getUniqueBids(
      @RequestBody @Valid GetYourUniqueBidsRequest getYourUniqueBidsRequest) {
    logger.info("get distinct items user has placed bids on request");
    try {
      return bidService.getYourBids(getYourUniqueBidsRequest);
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
  @PostMapping("/user/item/bids")
  @Operation(summary = "Get bids", description = "Get bids the logged in user has placed on a specific item, paginated")
  public GetYourItemBidsResponse[] getBidsOnItem(
      @RequestBody @Valid GetYourItemBidsRequest getYourItemBidsRequest) {
    logger.info("get bids user has placed on item request");
    try {
      return bidService.getYourItemBids(getYourItemBidsRequest);
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
  @PostMapping("/user/item/bid/answer")
  @Operation(summary = "Answer bid", description = "Accept or decline bid if logged in user owns bid item")
  public void answerBid(@RequestBody @Valid AnswerBidRequest answerBidRequest) {
    logger.info("answer bid request");
    try {
      bidService.answerBid(answerBidRequest);
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
  @PostMapping("/user/item/bid/users")
  @Operation(summary = "Get bidding users", description = "Get users who have bid on an item owned by the logged in user, paginated")
  public GetYourBidItemsResponse[] getBidsOnYourItems(
      @RequestBody @Valid GetYourBidItemsRequest getYourBidItemsRequest) {
    logger.info("get users who bid on item request");
    try {
      return bidService.getBids(getYourBidItemsRequest);
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
  @PostMapping("/user/item/bid/")
  @Operation(summary = "Get bid by user", description = "Get all bids made by a user on an item owned by logged in user, paginated")
  public GetBidsOnItemByUserResponse[] getBidsOnYourItem(
      @RequestBody @Valid GetBidsOnItemByUserRequest getBidsOnItemByUserRequest) {
    logger.info("get bids on item by user request");
    try {
      return bidService.getBidsOnYourItem(getBidsOnItemByUserRequest);
    } catch (DataAccessException e) {
      logger.warn("could not get bids: {}", e.getMessage());
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
  }
}
