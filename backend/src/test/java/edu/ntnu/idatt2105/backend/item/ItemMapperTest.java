package edu.ntnu.idatt2105.backend.item;

import edu.ntnu.idatt2105.backend.item.dto.ItemResponse;
import edu.ntnu.idatt2105.backend.item.model.Item;
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
}
