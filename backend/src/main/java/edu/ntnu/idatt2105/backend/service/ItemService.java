package edu.ntnu.idatt2105.backend.service;

import edu.ntnu.idatt2105.backend.dto.item.ItemsResponse;
import edu.ntnu.idatt2105.backend.dto.item.AddItemRequest;
import edu.ntnu.idatt2105.backend.dto.item.DeleteItemRequest;
import edu.ntnu.idatt2105.backend.dto.item.EditItemRequest;
import edu.ntnu.idatt2105.backend.dto.item.GetBookmarkedItemsRequest;
import edu.ntnu.idatt2105.backend.dto.item.GetBookmarkedItemsResponse;
import edu.ntnu.idatt2105.backend.dto.item.GetStoreItemResponse;
import edu.ntnu.idatt2105.backend.mapper.ItemMapper;
import edu.ntnu.idatt2105.backend.model.Bookmark;
import edu.ntnu.idatt2105.backend.model.BrowseHistory;
import edu.ntnu.idatt2105.backend.repository.BookmarkRepository;
import edu.ntnu.idatt2105.backend.repository.BrowseHistoryRepository;
import edu.ntnu.idatt2105.backend.repository.ItemRepository;
import edu.ntnu.idatt2105.backend.dto.item.ItemRequest;
import edu.ntnu.idatt2105.backend.dto.item.ItemResponse;
import edu.ntnu.idatt2105.backend.dto.item.ItemsRequest;
import edu.ntnu.idatt2105.backend.model.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * Service class for the Item model.
 */
@Service
@RequiredArgsConstructor
public class ItemService {

  private final ItemRepository itemRepository;

  private final BrowseHistoryRepository browseHistoryRepository;

  private final BookmarkRepository bookmarkRepository;

  /**
   * gets items based on filter.
   *
   * @param itemsRequest the filter
   * @return the items
   */
  public Item[] getItems(ItemsRequest itemsRequest) {
    return itemRepository.getItems(itemsRequest);
  }

  /**
   * get item based on id.
   *
   * @param itemRequest the id
   * @return the item
   */
  public ItemResponse getItem(ItemRequest itemRequest) {
    Item item = itemRepository.getItem(itemRequest.getItemID());
    return ItemMapper.INSTANCE.itemToItemResponse(item);
  }

  /**
   * get the item info with user specific info.
   *
   * @param itemRequest the request info
   * @return the item info
   */
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
   * delete item for user.
   *
   * @param deleteItemRequest the request info
   */
  public void deleteUserItem(DeleteItemRequest deleteItemRequest) {
    String userID = SecurityContextHolder.getContext().getAuthentication().getName();
    itemRepository.deleteItem(deleteItemRequest.getItemID(), Integer.parseInt(userID));
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
   * get bookmarked items from the user.
   *
   * @param getBookmarkedItemsRequest the request info
   * @return the items
   */
  public GetBookmarkedItemsResponse[] getBookmarkedItems(
      GetBookmarkedItemsRequest getBookmarkedItemsRequest) {
    String userID = SecurityContextHolder.getContext().getAuthentication().getName();
    Item[] items = itemRepository.getBookmarkedItems(Integer.parseInt(userID),
        getBookmarkedItemsRequest.getSegmentOffset());
    return ItemMapper.INSTANCE.itemArrayToGetBookmarkedItemsResponseArray(items);
  }
}
