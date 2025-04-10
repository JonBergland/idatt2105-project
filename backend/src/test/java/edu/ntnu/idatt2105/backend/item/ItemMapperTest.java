package edu.ntnu.idatt2105.backend.item;

import edu.ntnu.idatt2105.backend.dto.item.ItemResponse;
import edu.ntnu.idatt2105.backend.model.Item;
import edu.ntnu.idatt2105.backend.dto.user.AddItemRequest;
import edu.ntnu.idatt2105.backend.dto.user.EditItemRequest;
import edu.ntnu.idatt2105.backend.mapper.ItemMapper;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ItemMapperTest {

  private final ItemMapper itemMapper = ItemMapper.INSTANCE;

  @Test
  void testItemToItemResponse() {
    Item item = new Item();
    item.setItemID(1);
    item.setName("Test Item");
    item.setSeller("Test Seller");
    item.setDescription("Test Description");
    item.setPublished("2025-04-03");
    item.setPrice(100);

    ItemResponse itemResponse = itemMapper.itemToItemResponse(item);

    assertNotNull(itemResponse);
    assertEquals(item.getItemID(), itemResponse.getItemID());
    assertEquals(item.getName(), itemResponse.getName());
    assertEquals(item.getSeller(), itemResponse.getSeller());
    assertEquals(item.getDescription(), itemResponse.getDescription());
    assertEquals(item.getPublished(), itemResponse.getPublished());
    assertEquals(item.getPrice(), itemResponse.getPrice());
  }

  @Test
  void testItemsToItemResponses() {
    Item item1 = new Item();
    item1.setItemID(1);
    item1.setName("Test Item 1");
    item1.setSeller("Test Seller 1");
    item1.setDescription("Test Description 1");
    item1.setPublished("2025-04-01");
    item1.setPrice(100);
    item1.setCategory("Electronics");

    Item item2 = new Item();
    item2.setItemID(2);
    item2.setName("Test Item 2");
    item2.setSeller("Test Seller 2");
    item2.setDescription("Test Description 2");
    item2.setPublished("2025-04-02");
    item2.setPrice(150);
    item2.setCategory("Electronics");

    Item[] items = new Item[]{item1, item2};

    ItemResponse[] itemResponses = itemMapper.itemsToItemResponses(items);

    assertNotNull(itemResponses);
    assertEquals(2, itemResponses.length);
    assertEquals(item1.getItemID(), itemResponses[0].getItemID());
    assertEquals(item2.getItemID(), itemResponses[1].getItemID());
    assertEquals(item1.getName(), itemResponses[0].getName());
    assertEquals(item2.getName(), itemResponses[1].getName());
  }

  @Test
  void testAddItemRequestToItem() {
    AddItemRequest addItemRequest = new AddItemRequest();
    addItemRequest.setCategory("Electronics");
    addItemRequest.setName("Test Item");
    addItemRequest.setPrice(100);
    addItemRequest.setDescription("Test Description");

    Item item = itemMapper.addItemRequestToItem(addItemRequest);

    assertNotNull(item);
    assertEquals(addItemRequest.getCategory(), item.getCategory());
    assertEquals(addItemRequest.getName(), item.getName());
    assertEquals(addItemRequest.getPrice(), item.getPrice());
    assertEquals(addItemRequest.getDescription(), item.getDescription());
  }

  @Test
  void testEditItemRequestToItem() {
    EditItemRequest editItemRequest = new EditItemRequest();
    editItemRequest.setCategory("Electronics");
    editItemRequest.setName("Updated Item");
    editItemRequest.setPrice(150);
    editItemRequest.setDescription("Updated Description");
    editItemRequest.setItemID(1);

    Item item = itemMapper.editItemRequestToItem(editItemRequest);

    assertNotNull(item);
    assertEquals(editItemRequest.getCategory(), item.getCategory());
    assertEquals(editItemRequest.getName(), item.getName());
    assertEquals(editItemRequest.getPrice(), item.getPrice());
    assertEquals(editItemRequest.getDescription(), item.getDescription());
    assertEquals(editItemRequest.getItemID(), item.getItemID());
  }
}
