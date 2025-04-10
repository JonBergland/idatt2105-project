package edu.ntnu.idatt2105.backend.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import edu.ntnu.idatt2105.backend.dto.item.AddItemRequest;
import edu.ntnu.idatt2105.backend.dto.item.EditItemRequest;
import edu.ntnu.idatt2105.backend.dto.item.ItemRequest;
import edu.ntnu.idatt2105.backend.dto.item.ItemResponse;
import edu.ntnu.idatt2105.backend.dto.item.ItemsRequest;
import edu.ntnu.idatt2105.backend.dto.item.ItemsResponse;
import edu.ntnu.idatt2105.backend.model.Item;
import edu.ntnu.idatt2105.backend.repository.ItemRepository;
import edu.ntnu.idatt2105.backend.service.ItemService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

@ExtendWith(MockitoExtension.class)
class ItemServiceTest {

  @Mock
  private ItemRepository itemRepository;

  @Mock
  SecurityContext securityContext;

  @Mock
  Authentication authentication;

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

  @Test
  void addUserItem_ShouldAddItem() {
    AddItemRequest addItemRequest = new AddItemRequest();
    addItemRequest.setName("Test Item");

    when(securityContext.getAuthentication()).thenReturn(authentication);
    when(authentication.getName()).thenReturn("1");
    SecurityContextHolder.setContext(securityContext);

    itemService.addUserItem(addItemRequest);

    verify(itemRepository, times(1)).addItem(any(Item.class));
  }

  @Test
  void editUserItem_ShouldEditItem() {
    EditItemRequest editItemRequest = new EditItemRequest();
    editItemRequest.setName("Updated Item");

    when(securityContext.getAuthentication()).thenReturn(authentication);
    when(authentication.getName()).thenReturn("1");
    SecurityContextHolder.setContext(securityContext);

    itemService.editUserItem(editItemRequest);

    verify(itemRepository, times(1)).editItem(any(Item.class));
  }

  @Test
  void getUserItems_ShouldReturnItemsResponse() {
    Item mockItem = new Item();
    mockItem.setName("Test Item");
    Item[] items = new Item[]{mockItem};
    when(itemRepository.getItemsFromUserID(1)).thenReturn(items);

    when(securityContext.getAuthentication()).thenReturn(authentication);
    when(authentication.getName()).thenReturn("1");
    SecurityContextHolder.setContext(securityContext);

    ItemsResponse response = itemService.getUserItems();

    assertNotNull(response);
    assertEquals(1, response.getItems().length);
    assertEquals(mockItem.getName(), response.getItems()[0].getName());
  }
}