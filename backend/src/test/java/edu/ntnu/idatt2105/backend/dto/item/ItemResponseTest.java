package edu.ntnu.idatt2105.backend.dto.item;

import edu.ntnu.idatt2105.backend.dto.item.ItemResponse;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ItemResponseTest {

  @Test
  void testItemResponseConstructorAndGetters() {
    ItemResponse itemResponse = new ItemResponse(1, "Laptop", "Electronics", "available", "JohnDoe@eaxmple.com", "High-end gaming laptop", "2024-03-30", 1500);

    assertEquals(1, itemResponse.getItemID());
    assertEquals("Laptop", itemResponse.getName());
    assertEquals("JohnDoe@eaxmple.com", itemResponse.getSeller());
    assertEquals("High-end gaming laptop", itemResponse.getDescription());
    assertEquals("2024-03-30", itemResponse.getPublished());
    assertEquals(1500, itemResponse.getPrice());
  }

  @Test
  void testSetters() {
    ItemResponse itemResponse = new ItemResponse();

    itemResponse.setItemID(2);
    itemResponse.setName("Smartphone");
    itemResponse.setSeller("JaneDoe");
    itemResponse.setDescription("Latest model smartphone");
    itemResponse.setPublished("2024-04-01");
    itemResponse.setPrice(800);

    assertEquals(2, itemResponse.getItemID());
    assertEquals("Smartphone", itemResponse.getName());
    assertEquals("JaneDoe", itemResponse.getSeller());
    assertEquals("Latest model smartphone", itemResponse.getDescription());
    assertEquals("2024-04-01", itemResponse.getPublished());
    assertEquals(800, itemResponse.getPrice());
  }
}