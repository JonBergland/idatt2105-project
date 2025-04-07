package edu.ntnu.idatt2105.backend.item;

import edu.ntnu.idatt2105.backend.item.dto.ItemRequest;
import edu.ntnu.idatt2105.backend.item.dto.ItemResponse;
import edu.ntnu.idatt2105.backend.item.dto.ItemsRequest;
import edu.ntnu.idatt2105.backend.item.dto.ItemsResponse;
import edu.ntnu.idatt2105.backend.item.model.Item;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class ItemControllerTest {

  @Mock
  private ItemService itemService;

  @InjectMocks
  private ItemController itemController;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testGetItems() {
    ItemsRequest itemsRequest = new ItemsRequest();
    itemsRequest.setCategory("Electronics");

    when(itemService.getItems(itemsRequest)).thenReturn(new Item[] {new Item(), new Item()});

    ItemsResponse response = itemController.getItems(itemsRequest);

    assertNotNull(response);
    assertEquals(2, response.getItems().length);
  }

  @Test
  void testGetItem() {
    ItemRequest itemRequest = new ItemRequest();
    itemRequest.setItemID(1);

    ItemResponse itemResponse = new ItemResponse(1, "Laptop", "Electronics", "availabe", "testSeller", "A great laptop", "2025-04-03", 300);

    when(itemService.getItem(eq(itemRequest))).thenReturn(itemResponse);

    ItemResponse response = itemController.itemResponses(itemRequest);

    assertNotNull(response);
    assertEquals(itemResponse.getItemID(), response.getItemID());
    assertEquals(itemResponse.getName(), response.getName());
  }

  @Test
  void testGetItem_DataAccessException() {
    ItemRequest itemRequest = new ItemRequest();
    itemRequest.setItemID(1);

    doThrow(new DataAccessException("Database error") {}).when(itemService).getItem(eq(itemRequest));

    ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
      itemController.itemResponses(itemRequest);
    });

    assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
  }

}
