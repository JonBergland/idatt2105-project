package edu.ntnu.idatt2105.backend.item;

import edu.ntnu.idatt2105.backend.item.dto.ItemRequest;
import edu.ntnu.idatt2105.backend.item.dto.ItemResponse;
import edu.ntnu.idatt2105.backend.item.dto.ItemsRequest;
import edu.ntnu.idatt2105.backend.item.model.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Service class for the Item model.
 */
@Service
@RequiredArgsConstructor
public class ItemService {

  private final ItemRepository itemRepository;

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
}
