package edu.ntnu.idatt2105.backend.item.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ItemRequestTest {

  @Test
  void testItemRequestGetterAndSetter() {
    ItemRequest itemRequest = new ItemRequest();
    int testItemID = 123;

    itemRequest.setItemID(testItemID);

    assertEquals(testItemID, itemRequest.getItemID(), "ItemID should be correctly set and retrieved");
  }
}
