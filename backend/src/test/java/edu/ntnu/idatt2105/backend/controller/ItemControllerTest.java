package edu.ntnu.idatt2105.backend.controller;

import edu.ntnu.idatt2105.backend.controller.ItemController;
import edu.ntnu.idatt2105.backend.dto.item.AddItemRequest;
import edu.ntnu.idatt2105.backend.dto.item.EditItemRequest;
import edu.ntnu.idatt2105.backend.dto.item.ItemRequest;
import edu.ntnu.idatt2105.backend.dto.item.ItemResponse;
import edu.ntnu.idatt2105.backend.dto.item.ItemsRequest;
import edu.ntnu.idatt2105.backend.dto.item.ItemsResponse;
import edu.ntnu.idatt2105.backend.model.Item;
import edu.ntnu.idatt2105.backend.service.ItemService;
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

  @Test
  void testGetUserItems() {
    ItemsResponse itemsResponse = new ItemsResponse();
    when(itemService.getUserItems()).thenReturn(itemsResponse);

    ItemsResponse response = itemController.getUserItems();

    assertNotNull(response);
    assertEquals(itemsResponse, response);
  }

  @Test
  void testGetUserItems_ShouldThrowResponseStatusException_WhenDataAccessExceptionOccurs() {
    doThrow(new DataAccessException("DB Error") {}).when(itemService).getUserItems();

    ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
      itemController.getUserItems();
    });

    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode());
  }

  @Test
  void testAddUserItem() {
    AddItemRequest addItemRequest = new AddItemRequest();
    itemController.addUserItem(addItemRequest);
    verify(itemService, times(1)).addUserItem(addItemRequest);
  }

  @Test
  void testAddUserItem_ShouldThrowResponseStatusException_WhenDataAccessExceptionOccurs() {
    AddItemRequest addItemRequest = new AddItemRequest();
    doThrow(new DataAccessException("DB Error") {}).when(itemService).addUserItem(addItemRequest);

    ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
      itemController.addUserItem(addItemRequest);
    });

    assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
  }

  @Test
  void testEditUserItem() {
    EditItemRequest editItemRequest = new EditItemRequest();
    itemController.editUserItem(editItemRequest);
    verify(itemService, times(1)).editUserItem(editItemRequest);
  }

  @Test
  void testEditUserItem_ShouldThrowResponseStatusException_WhenDataAccessExceptionOccurs() {
    EditItemRequest editItemRequest = new EditItemRequest();
    doThrow(new DataAccessException("DB Error") {}).when(itemService).editUserItem(editItemRequest);

    ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
      itemController.editUserItem(editItemRequest);
    });

    assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
  }
}
