package edu.ntnu.idatt2105.backend.service;

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
import edu.ntnu.idatt2105.backend.mapper.BidMapper;
import edu.ntnu.idatt2105.backend.model.Bid;
import edu.ntnu.idatt2105.backend.repository.BidRepository;
import edu.ntnu.idatt2105.backend.repository.ItemRepository;
import java.nio.file.AccessDeniedException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BidService {

  private final BidRepository bidRepository;

  private final ItemRepository itemRepository;

  /**
   * place a bid on an item.
   *
   * @param placeBidRequest the request info
   */
  public void placeBid(PlaceBidRequest placeBidRequest) throws IllegalArgumentException {
    Bid bid = BidMapper.INSTANCE.placeBidReqeustToBid(placeBidRequest);
    String userID = SecurityContextHolder.getContext().getAuthentication().getName();
    bid.setUserID(Integer.parseInt(userID));

    String itemState = itemRepository.getItem(bid.getItemID()).getState();
    if (itemState.equals("available") || itemState.equals("reserved")) {
      bidRepository.placeBid(bid);
    } else {
      throw new IllegalArgumentException("Item not allowed for bidding");
    }
  }

  /**
   * Get bids you have placed on distinct items.
   *
   * @return the bids
   */
  public GetYourUniqueBidsResponse[] getYourBids(GetYourUniqueBidsRequest getYourUniqueBidsRequest) {
    String userID = SecurityContextHolder.getContext().getAuthentication().getName();
    Bid[] bids = bidRepository.getYourUniqueBids(Integer.parseInt(userID), getYourUniqueBidsRequest.getSegmentOffset());
    return BidMapper.INSTANCE.bidArrayToGetBidItemResponseArray(bids);
  }

  /**
   * get bids you have placed on one item.
   *
   * @param getYourItemBidsRequest the request info
   * @return the bids
   */
  public GetYourItemBidsResponse[] getYourItemBids(GetYourItemBidsRequest getYourItemBidsRequest) {
    String userID = SecurityContextHolder.getContext().getAuthentication().getName();
    Bid[] bids = bidRepository.getItemBids(Integer.parseInt(userID), getYourItemBidsRequest);
    return BidMapper.INSTANCE.bidArrayToGetBidsResponseArray(bids);
  }

  /**
   * answer a bid by setting the bid status.
   *
   * @param answerBidRequest the request info
   * @throws AccessDeniedException if user don't own item
   */
  public void answerBid(AnswerBidRequest answerBidRequest)
      throws IllegalArgumentException, AccessDeniedException {
    Bid bid = BidMapper.INSTANCE.answerBidRequestToBid(answerBidRequest);
    String userID = SecurityContextHolder.getContext().getAuthentication().getName();

    int itemID = bidRepository.itemFromBid(bid.getBidID());
    String itemState = itemRepository.getItem(itemID).getState();
    if (itemState.equals("sold")) {
      throw new IllegalArgumentException("Item already sold");
    }
    bidRepository.setBidStatus(bid, Integer.parseInt(userID));

    if (!bidRepository.checkIfUnansweredBid(itemID)) {
      itemRepository.updateState(itemID, 1);
    }
  }

  /**
   * get users who have bid on your items.
   *
   * @return the bids
   */
  public GetYourBidItemsResponse[] getBids(GetYourBidItemsRequest getYourBidItemsRequest) {
    String userID = SecurityContextHolder.getContext().getAuthentication().getName();
    Bid[] bids = bidRepository.getUniqueBids(Integer.parseInt(userID), getYourBidItemsRequest.getSegmentOffset());
    return BidMapper.INSTANCE.bidArrayToGetYourBidItemsResponseArray(bids);
  }

  /**
   * get bids by a user on your item.
   *
   * @param getBidsOnItemByUserRequest the request info
   * @return the bids
   */
  public GetBidsOnItemByUserResponse[] getBidsOnYourItem(
      GetBidsOnItemByUserRequest getBidsOnItemByUserRequest) {
    String userID = SecurityContextHolder.getContext().getAuthentication().getName();
    Bid bid = BidMapper.INSTANCE.getBidsOnItemByUserRequestToBid(getBidsOnItemByUserRequest);
    Bid[] bids = bidRepository.getBidsByUserOnItem(bid, Integer.parseInt(userID), getBidsOnItemByUserRequest.getSegmentOffset());
    return BidMapper.INSTANCE.bidArrayToGetBidsOnItemByUserResponseArray(bids);
  }
}
