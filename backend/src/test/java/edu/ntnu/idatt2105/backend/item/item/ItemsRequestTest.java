package edu.ntnu.idatt2105.backend.item.item;

import edu.ntnu.idatt2105.backend.dto.item.ItemsRequest;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ItemsRequestTest {

  @Test
  void testItemsRequest_SettersAndGetters() {
    ItemsRequest request = new ItemsRequest();

    request.setCategory("Electronics");
    request.setSearchWord("Laptop");
    request.setPriceMinMax(new int[]{500, 1500});
    request.setSort("price_asc");
    request.setSegmentOffset(new int[]{0, 10});

    assertEquals("Electronics", request.getCategory());
    assertEquals("Laptop", request.getSearchWord());
    assertArrayEquals(new int[]{500, 1500}, request.getPriceMinMax());
    assertEquals("price_asc", request.getSort());
    assertArrayEquals(new int[]{0, 10}, request.getSegmentOffset());
  }

  @Test
  void testItemsRequest_DefaultValues() {
    ItemsRequest request = new ItemsRequest();

    assertNull(request.getCategory());
    assertNull(request.getSearchWord());
    assertNull(request.getPriceMinMax());
    assertNull(request.getSort());
    assertNull(request.getSegmentOffset());
  }
}
