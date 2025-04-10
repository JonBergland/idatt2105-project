package edu.ntnu.idatt2105.backend.item.model;

import edu.ntnu.idatt2105.backend.model.Item;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ItemTest {

  @Test
  void testItemGettersAndSetters() {
    Item item = new Item();
    item.setName("Laptop");
    item.setItemID(123);
    item.setSeller("TechStore");
    item.setDescription("A powerful gaming laptop");
    item.setPublished("2025-03-15");
    item.setPrice(1500);

    assertEquals("Laptop", item.getName());
    assertEquals(123, item.getItemID());
    assertEquals("TechStore", item.getSeller());
    assertEquals("A powerful gaming laptop", item.getDescription());
    assertEquals("2025-03-15", item.getPublished());
    assertEquals(1500, item.getPrice());
  }
}
