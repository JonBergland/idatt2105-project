package edu.ntnu.idatt2105.backend.user;

import edu.ntnu.idatt2105.backend.bid.BidMapper;
import edu.ntnu.idatt2105.backend.bid.BidRepository;
import edu.ntnu.idatt2105.backend.bid.model.Bid;
import edu.ntnu.idatt2105.backend.bookmark.BookmarkMapper;
import edu.ntnu.idatt2105.backend.bookmark.BookmarkRepository;
import edu.ntnu.idatt2105.backend.bookmark.model.Bookmark;
import edu.ntnu.idatt2105.backend.browseHistory.BrowseHistoryRepository;
import edu.ntnu.idatt2105.backend.browseHistory.model.BrowseHistory;
import edu.ntnu.idatt2105.backend.item.ItemMapper;
import edu.ntnu.idatt2105.backend.item.ItemRepository;
import edu.ntnu.idatt2105.backend.item.dto.ItemRequest;
import edu.ntnu.idatt2105.backend.item.dto.ItemsResponse;
import edu.ntnu.idatt2105.backend.item.model.Item;
import edu.ntnu.idatt2105.backend.purchase.PurchaseMapper;
import edu.ntnu.idatt2105.backend.purchase.PurchaseRepository;
import edu.ntnu.idatt2105.backend.purchase.model.Purchase;
import edu.ntnu.idatt2105.backend.security.dto.SigninRequest;
import edu.ntnu.idatt2105.backend.security.dto.SignupRequest;
import edu.ntnu.idatt2105.backend.user.dto.AddItemRequest;
import edu.ntnu.idatt2105.backend.user.dto.AnswerBidRequest;
import edu.ntnu.idatt2105.backend.user.dto.BuyItemFromBidRequest;
import edu.ntnu.idatt2105.backend.user.dto.BuyItemRequest;
import edu.ntnu.idatt2105.backend.user.dto.DeleteItemRequest;
import edu.ntnu.idatt2105.backend.user.dto.GetBidsOnItemByUserRequest;
import edu.ntnu.idatt2105.backend.user.dto.GetBidsOnItemByUserResponse;
import edu.ntnu.idatt2105.backend.user.dto.GetBookmarkedItemsRequest;
import edu.ntnu.idatt2105.backend.user.dto.GetBookmarkedItemsResponse;
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
import edu.ntnu.idatt2105.backend.user.model.User;
import java.nio.file.AccessDeniedException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service class for the User model.
 */
@Service
@RequiredArgsConstructor
public class UserService {

  private final Logger logger = LoggerFactory.getLogger(UserService.class);

  private final UserRepository userRepository;

  private final ItemRepository itemRepository;

  private final BookmarkRepository bookmarkRepository;

  private final BrowseHistoryRepository browseHistoryRepository;

  private final BidRepository bidRepository;

  private final PurchaseRepository purchaseRepository;

  private final PasswordEncoder passwordEncoder;

  /**
   * gets the user information from the users email.
   *
   * @param email the email of the user
   * @return the user object
   */
  public User getUserByEmail(String email) {
    return userRepository.getUserByEmail(email);
  }

  /**
   * checks if the passed password matches the stored password.
   *
   * @param signinRequest the signin request
   * @return do passwords match return boolean
   */
  public boolean checkCredentials(SigninRequest signinRequest) {
    String userPassword = userRepository.getPasswordByEmail(signinRequest.getEmail());
    return passwordEncoder.matches(signinRequest.getPassword(), userPassword);
  }

  /**
   * create user based on a signup request.
   *
   * @param signupRequest the signup request
   */
  public void createUser(SignupRequest signupRequest) {
    User user = UserMapper.INSTANCE.signupRequestToUser(signupRequest);
    user.setRole("ROLE_USER");
    user.setPassword(encodePassword(user.getPassword()));
    userRepository.createUser(user);
  }

  //  public void createAdmin(User user) {
  //    user.setRole("ROLE_ADMIN");
  //    user.setPassword(encodePassword(user.getPassword()));
  //    userRepository.createUser(user);
  //  }

  /**
   * get info of logged in user.
   *
   * @param userID logged in user's id
   * @return the user info response
   */
  public GetUserInfoResponse getUser(int userID) {
    User user = userRepository.getUser(userID);
    return UserMapper.INSTANCE.userToGetUserInfoResponse(user);
  }

  /**
   * edit info of logged-in user.
   *
   * @param updateUserInfoRequest the edit info request
   */
  public void editUser(UpdateUserInfoRequest updateUserInfoRequest) {
    User user = UserMapper.INSTANCE.updateUserInfoRequestToUser(updateUserInfoRequest);
    String userID = SecurityContextHolder.getContext().getAuthentication().getName();
    user.setUserID(Integer.parseInt(userID));
    userRepository.updateUser(user);
  }

  /**
   * add item for user.
   *
   * @param addItemRequest the item to add
   */
  public void addUserItem(AddItemRequest addItemRequest) {
    Item item = ItemMapper.INSTANCE.addItemRequestToItem(addItemRequest);
    String userID = SecurityContextHolder.getContext().getAuthentication().getName();
    item.setSellerID(Integer.parseInt(userID));
    itemRepository.addItem(item);
  }

  /**
   * edit item for user.
   *
   * @param editItemRequest the edited item
   */
  public void editUserItem(EditItemRequest editItemRequest) {
    Item item = ItemMapper.INSTANCE.editItemRequestToItem(editItemRequest);
    String userID = SecurityContextHolder.getContext().getAuthentication().getName();
    item.setSellerID(Integer.parseInt(userID));
    itemRepository.editItem(item);
  }

  /**
   * get the user's items.
   *
   * @return the users items
   */
  public ItemsResponse getUserItems() {
    String userID = SecurityContextHolder.getContext().getAuthentication().getName();
    Item[] items = itemRepository.getItemsFromUserID(Integer.parseInt(userID));
    return new ItemsResponse(ItemMapper.INSTANCE.itemsToItemResponses(items));
  }

  /**
   * delete item for user.
   *
   * @param deleteItemRequest the request info
   */
  public void deleteUserItem(DeleteItemRequest deleteItemRequest) {
    String userID = SecurityContextHolder.getContext().getAuthentication().getName();
    itemRepository.deleteItem(deleteItemRequest.getItemID(), Integer.parseInt(userID));
  }

  public void toggleBookmark(ToggleBookmarkRequest toggleBookmarkRequest) {
    Bookmark bookmark = BookmarkMapper.INSTANCE.bookmarkRequestToBookmark(toggleBookmarkRequest);
    String userID = SecurityContextHolder.getContext().getAuthentication().getName();
    bookmark.setUserID(Integer.parseInt(userID));

    boolean bookmarkSet = bookmarkRepository.checkBookmark(bookmark);
    if (bookmarkSet) {
      bookmarkRepository.deleteBookmark(bookmark);
    } else {
      bookmarkRepository.addBookmark(bookmark);
    }
  }

  /**
   * get bookmarked items from the user.
   *
   * @param getBookmarkedItemsRequest the request info
   * @return the items
   */
  public GetBookmarkedItemsResponse[] getBookmarkedItems(GetBookmarkedItemsRequest getBookmarkedItemsRequest) {
    String userID = SecurityContextHolder.getContext().getAuthentication().getName();
    Item[] items = itemRepository.getBookmarkedItems(Integer.parseInt(userID),
        getBookmarkedItemsRequest.getSegmentOffset());
    return ItemMapper.INSTANCE.itemArrayToGetBookmarkedItemsResponseArray(items);
  }

  public GetStoreItemResponse getUserSpecificItemInfo(ItemRequest itemRequest) {
    Item item = itemRepository.getItem(itemRequest.getItemID());

    String userID = SecurityContextHolder.getContext().getAuthentication().getName();

    Bookmark bookmark = new Bookmark(Integer.parseInt(userID), itemRequest.getItemID());
    boolean bookmarked = bookmarkRepository.checkBookmark(bookmark);
    item.setBookmark(bookmarked);

    BrowseHistory browseHistory = new BrowseHistory();
    browseHistory.setItemID(itemRequest.getItemID());
    browseHistory.setUserID(Integer.parseInt(userID));
    browseHistoryRepository.addUpdateBrowseHistory(browseHistory);

    return ItemMapper.INSTANCE.itemToGetStoreItemResponse(item);
  }

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

  public GetBidsOnItemByUserResponse[] getBidsOnYourItem(GetBidsOnItemByUserRequest getBidsOnItemByUserRequest) {
    String userID = SecurityContextHolder.getContext().getAuthentication().getName();
    Bid bid = BidMapper.INSTANCE.getBidsOnItemByUserRequestToBid(getBidsOnItemByUserRequest);
    Bid[] bids = bidRepository.getBidsByUserOnItem(bid, Integer.parseInt(userID), getBidsOnItemByUserRequest.getSegmentOffset());
    return BidMapper.INSTANCE.bidArrayToGetBidsOnItemByUserResponseArray(bids);
  }

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

    if (item.getState().equals("sold") || item.getState().equals("reserved")) {
      throw new IllegalArgumentException("Item not purchasable");
    }

    purchase.setFinalPrice(item.getPrice());
    itemRepository.updateState(purchase.getItemID(), 4);
    purchaseRepository.addPurchase(purchase);
  }

  private String encodePassword(String password) {
    return passwordEncoder.encode(password);
  }
}
