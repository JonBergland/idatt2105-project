package edu.ntnu.idatt2105.backend.item;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import edu.ntnu.idatt2105.backend.item.dto.ItemRequest;
import edu.ntnu.idatt2105.backend.item.dto.ItemResponse;
import edu.ntnu.idatt2105.backend.item.dto.ItemsRequest;
import edu.ntnu.idatt2105.backend.item.model.Item;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ItemServiceTest {

  @Mock
  private ItemRepository itemRepository;

  @InjectMocks
  private ItemService itemService;

  private Item testItem;

  @BeforeEach
  void setUp() {
    testItem = new Item();
    testItem.setItemID(1);
    testItem.setName("Test Item");
    testItem.setSeller("Seller Name");
    testItem.setDescription("Item Description");
    testItem.setPublished("2024-01-01");
    testItem.setPrice(100);
  }

  @Test
  void getItem_ShouldReturnItemResponse_WhenItemExists() {
    ItemRequest itemRequest = new ItemRequest();
    itemRequest.setItemID(1);

    when(itemRepository.getItem(1)).thenReturn(testItem);

    ItemResponse itemResponse = itemService.getItem(itemRequest);

    assertNotNull(itemResponse);
    assertEquals(1, itemResponse.getItemID());
    assertEquals("Test Item", itemResponse.getName());
    verify(itemRepository, times(1)).getItem(1);
  }

  @Test
  void getItems_ShouldReturnItems_WhenFiltersApplied() {
    ItemsRequest itemsRequest = new ItemsRequest();
    Item[] itemsArray = new Item[]{testItem};

    when(itemRepository.getItems(itemsRequest)).thenReturn(itemsArray);

    Item[] result = itemService.getItems(itemsRequest);

    assertNotNull(result);
    assertEquals(1, result.length);
    assertEquals("Test Item", result[0].getName());
    verify(itemRepository, times(1)).getItems(itemsRequest);
  }
}