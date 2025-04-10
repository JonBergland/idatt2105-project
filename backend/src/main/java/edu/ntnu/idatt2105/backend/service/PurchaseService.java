package edu.ntnu.idatt2105.backend.service;

import edu.ntnu.idatt2105.backend.dto.purchase.BuyItemFromBidRequest;
import edu.ntnu.idatt2105.backend.dto.purchase.BuyItemRequest;
import edu.ntnu.idatt2105.backend.mapper.PurchaseMapper;
import edu.ntnu.idatt2105.backend.model.Bid;
import edu.ntnu.idatt2105.backend.model.Item;
import edu.ntnu.idatt2105.backend.model.Purchase;
import edu.ntnu.idatt2105.backend.repository.BidRepository;
import edu.ntnu.idatt2105.backend.repository.ItemRepository;
import edu.ntnu.idatt2105.backend.repository.PurchaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PurchaseService {

  private final PurchaseRepository purchaseRepository;

  private final ItemRepository itemRepository;

  private final BidRepository bidRepository;

  /**
   * buy item directly.
   *
   * @param buyItemRequest the request info
   */
  @Transactional
  public void buyItem(BuyItemRequest buyItemRequest) {
    String userID = SecurityContextHolder.getContext().getAuthentication().getName();
    Purchase purchase = PurchaseMapper.INSTANCE.buyItemRequestToPurchase(buyItemRequest);
    purchase.setBuyerID(Integer.parseInt(userID));

    Item item = itemRepository.getItem(purchase.getItemID());
    if (item.getState().equals("sold") || item.getState().equals("reserved")) {
      throw new IllegalArgumentException("Item not purchasable");
    }

    buy(purchase);
  }

  /**
   * buy item from accepted bid.
   *
   * @param buyItemFromBidRequest the request info
   */
  @Transactional
  public void buyItemFromBid(BuyItemFromBidRequest buyItemFromBidRequest) {
    String userID = SecurityContextHolder.getContext().getAuthentication().getName();
    Bid bid = bidRepository.getBid(buyItemFromBidRequest.getBidID());
    Purchase purchase = new Purchase();
    purchase.setItemID(bid.getItemID());
    purchase.setBuyerID(Integer.parseInt(userID));
    purchase.setFinalPrice(bid.getAskingPrice());

    Item item = itemRepository.getItem(purchase.getItemID());
    if (item.getState().equals("sold")) {
      throw new IllegalArgumentException("Item not purchasable");
    }

    if (bid.getUserID() == Integer.parseInt(userID)
        && bid.getStatus() != null
        && bid.getStatus().equals("1")) {
      buy(purchase);
    } else {
      throw new IllegalArgumentException("Bid not users or not valid");
    }
  }

  /**
   * method for buying item based on a purchase object.
   *
   * @param purchase the purchase
   */
  private void buy(Purchase purchase) {
    Item item = itemRepository.getItem(purchase.getItemID());
    purchase.setFinalPrice(item.getPrice());
    itemRepository.updateState(purchase.getItemID(), 4);
    purchaseRepository.addPurchase(purchase);
  }
}
