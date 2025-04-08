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
import edu.ntnu.idatt2105.backend.security.dto.SigninRequest;
import edu.ntnu.idatt2105.backend.security.dto.SignupRequest;
import edu.ntnu.idatt2105.backend.user.dto.AddItemRequest;
import edu.ntnu.idatt2105.backend.user.dto.AnswerBidRequest;
import edu.ntnu.idatt2105.backend.user.dto.GetYourBidItemsResponse;
import edu.ntnu.idatt2105.backend.user.dto.GetYourUniqueBidsResponse;
import edu.ntnu.idatt2105.backend.user.dto.GetYourItemBidsRequest;
import edu.ntnu.idatt2105.backend.user.dto.GetYourItemBidsResponse;
import edu.ntnu.idatt2105.backend.user.dto.GetStoreItemResponse;
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

  public void placeBid(PlaceBidRequest placeBidRequest) {
    Bid bid = BidMapper.INSTANCE.placeBidReqeustToBid(placeBidRequest);
    String userID = SecurityContextHolder.getContext().getAuthentication().getName();
    bid.setUserID(Integer.parseInt(userID));
    bidRepository.placeBid(bid);
  }

  public GetYourUniqueBidsResponse[] getYourBids() {
    String userID = SecurityContextHolder.getContext().getAuthentication().getName();
    Bid[] bids = bidRepository.getYourUniqueBids(Integer.parseInt(userID));
    return BidMapper.INSTANCE.bidArrayToGetBidItemResponseArray(bids);
  }

  public GetYourItemBidsResponse[] getYourItemBids(GetYourItemBidsRequest getYourItemBidsRequest) {
    String userID = SecurityContextHolder.getContext().getAuthentication().getName();
    Bid[] bids = bidRepository.getItemBids(Integer.parseInt(userID), getYourItemBidsRequest);
    return BidMapper.INSTANCE.bidArrayToGetBidsResponseArray(bids);
  }

  public void answerBid(AnswerBidRequest answerBidRequest) throws AccessDeniedException {
    Bid bid = BidMapper.INSTANCE.answerBidRequestToBid(answerBidRequest);
    String userID = SecurityContextHolder.getContext().getAuthentication().getName();
    bidRepository.setBidStatus(bid, Integer.parseInt(userID));
  }

  public GetYourBidItemsResponse[] getBids() {
    String userID = SecurityContextHolder.getContext().getAuthentication().getName();
    Bid[] bids = bidRepository.getUniqueBids(Integer.parseInt(userID));
    return BidMapper.INSTANCE.bidArrayToGetYourBidItemsResponseArray(bids);
  }

  private String encodePassword(String password) {
    return passwordEncoder.encode(password);
  }
}
