package edu.ntnu.idatt2105.backend.user;

import edu.ntnu.idatt2105.backend.controller.UserController;
import edu.ntnu.idatt2105.backend.dto.item.ItemsResponse;
import edu.ntnu.idatt2105.backend.service.UserService;
import edu.ntnu.idatt2105.backend.dto.user.AddItemRequest;
import edu.ntnu.idatt2105.backend.dto.user.EditItemRequest;
import edu.ntnu.idatt2105.backend.dto.user.GetUserInfoResponse;
import edu.ntnu.idatt2105.backend.dto.user.UpdateUserInfoRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.web.server.ResponseStatusException;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {

  @Mock
  private UserService userService;

  @InjectMocks
  private UserController userController;

  @Mock
  private SecurityContext securityContext;

  @Mock
  private Authentication authentication;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    SecurityContextHolder.setContext(securityContext);
    when(securityContext.getAuthentication()).thenReturn(authentication);
    when(authentication.getName()).thenReturn("1");
  }

  @Test
  void testUpdateUserInfo() {
    UpdateUserInfoRequest updateUserInfoRequest = new UpdateUserInfoRequest();
    userController.updateUserInfo(updateUserInfoRequest);
    verify(userService, times(1)).editUser(updateUserInfoRequest);
  }

  @Test
  void testUpdateUserInfo_ShouldThrowResponseStatusException_WhenDataAccessExceptionOccurs() {
    UpdateUserInfoRequest updateUserInfoRequest = new UpdateUserInfoRequest();
    doThrow(new DataAccessException("DB Error") {}).when(userService).editUser(updateUserInfoRequest);

    ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
      userController.updateUserInfo(updateUserInfoRequest);
    });

    assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
  }

  @Test
  void testGetUserInfo() {
    GetUserInfoResponse getUserInfoResponse = new GetUserInfoResponse();
    when(userService.getUser(1)).thenReturn(getUserInfoResponse);

    ResponseEntity<GetUserInfoResponse> response = userController.getUserInfo();

    assertNotNull(response);
    assertEquals(getUserInfoResponse, response.getBody());
  }

  @Test
  void testGetUserInfo_ShouldThrowResponseStatusException_WhenDataAccessExceptionOccurs() {
    when(userService.getUser(1)).thenThrow(new DataAccessException("DB Error") {});

    ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
      userController.getUserInfo();
    });

    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode());
  }

  @Test
  void testGetUserItems() {
    ItemsResponse itemsResponse = new ItemsResponse();
    when(userService.getUserItems()).thenReturn(itemsResponse);

    ItemsResponse response = userController.getUserItems();

    assertNotNull(response);
    assertEquals(itemsResponse, response);
  }

  @Test
  void testGetUserItems_ShouldThrowResponseStatusException_WhenDataAccessExceptionOccurs() {
    doThrow(new DataAccessException("DB Error") {}).when(userService).getUserItems();

    ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
      userController.getUserItems();
    });

    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode());
  }

  @Test
  void testAddUserItem() {
    AddItemRequest addItemRequest = new AddItemRequest();
    userController.addUserItem(addItemRequest);
    verify(userService, times(1)).addUserItem(addItemRequest);
  }

  @Test
  void testAddUserItem_ShouldThrowResponseStatusException_WhenDataAccessExceptionOccurs() {
    AddItemRequest addItemRequest = new AddItemRequest();
    doThrow(new DataAccessException("DB Error") {}).when(userService).addUserItem(addItemRequest);

    ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
      userController.addUserItem(addItemRequest);
    });

    assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
  }

  @Test
  void testEditUserItem() {
    EditItemRequest editItemRequest = new EditItemRequest();
    userController.editUserItem(editItemRequest);
    verify(userService, times(1)).editUserItem(editItemRequest);
  }

  @Test
  void testEditUserItem_ShouldThrowResponseStatusException_WhenDataAccessExceptionOccurs() {
    EditItemRequest editItemRequest = new EditItemRequest();
    doThrow(new DataAccessException("DB Error") {}).when(userService).editUserItem(editItemRequest);

    ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
      userController.editUserItem(editItemRequest);
    });

    assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
  }
}