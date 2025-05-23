package edu.ntnu.idatt2105.backend.dto.item;

import edu.ntnu.idatt2105.backend.dto.item.ItemResponse;
import edu.ntnu.idatt2105.backend.dto.item.ItemsResponse;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ItemsResponseTest {

  @Test
  void testItemsResponseConstructorAndGetters() {
    ItemResponse
        item1 = new ItemResponse(1, "Laptop", "Electronics", "available", "Alice", "A gaming laptop", "2024-03-15", 1500);
    ItemResponse item2 = new ItemResponse(2, "Phone", "Electronics", "available", "Bob", "A smartphone", "2024-03-16", 800);
    ItemResponse[] itemsArray = {item1, item2};

    ItemsResponse itemsResponse = new ItemsResponse(itemsArray);

    assertNotNull(itemsResponse);
    assertArrayEquals(itemsArray, itemsResponse.getItems());
  }

  @Test
  void testSetItems() {
    ItemResponse item1 = new ItemResponse(1, "Tablet", "Electronics", "available", "Charlie", "A new tablet", "2024-03-17", 600);
    ItemResponse[] itemsArray = {item1};

    ItemsResponse itemsResponse = new ItemsResponse();
    itemsResponse.setItems(itemsArray);

    assertArrayEquals(itemsArray, itemsResponse.getItems());
  }
}
