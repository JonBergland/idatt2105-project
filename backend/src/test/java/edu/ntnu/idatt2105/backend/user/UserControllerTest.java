package edu.ntnu.idatt2105.backend.user;

import edu.ntnu.idatt2105.backend.item.dto.ItemsResponse;
import edu.ntnu.idatt2105.backend.user.dto.AddItemRequest;
import edu.ntnu.idatt2105.backend.user.dto.EditItemRequest;
import edu.ntnu.idatt2105.backend.user.dto.GetUserInfoResponse;
import edu.ntnu.idatt2105.backend.user.dto.UpdateUserInfoRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;

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
  void testGetUserInfo() {
    GetUserInfoResponse getUserInfoResponse = new GetUserInfoResponse();
    when(userService.getUser(1)).thenReturn(getUserInfoResponse);

    GetUserInfoResponse response = userController.getUserInfo();

    assertNotNull(response);
    assertEquals(getUserInfoResponse, response);
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
  void testAddUserItem() {
    AddItemRequest addItemRequest = new AddItemRequest();
    userController.addUserItem(addItemRequest);
    verify(userService, times(1)).addUserItem(addItemRequest);
  }

  @Test
  void testEditUserItem() {
    EditItemRequest editItemRequest = new EditItemRequest();
    userController.editUserItem(editItemRequest);
    verify(userService, times(1)).editUserItem(editItemRequest);
  }
}