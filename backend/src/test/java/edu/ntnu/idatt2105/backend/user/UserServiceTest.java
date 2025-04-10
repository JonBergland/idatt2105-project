package edu.ntnu.idatt2105.backend.user;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import edu.ntnu.idatt2105.backend.repository.BookmarkRepository;
import edu.ntnu.idatt2105.backend.model.Bookmark;
import edu.ntnu.idatt2105.backend.repository.ItemRepository;
import edu.ntnu.idatt2105.backend.dto.item.ItemsResponse;
import edu.ntnu.idatt2105.backend.model.Item;
import edu.ntnu.idatt2105.backend.mapper.UserMapper;
import edu.ntnu.idatt2105.backend.repository.UserRepository;
import edu.ntnu.idatt2105.backend.dto.token.SigninRequest;
import edu.ntnu.idatt2105.backend.dto.token.SignupRequest;
import edu.ntnu.idatt2105.backend.service.UserService;
import edu.ntnu.idatt2105.backend.dto.item.AddItemRequest;
import edu.ntnu.idatt2105.backend.dto.item.EditItemRequest;
import edu.ntnu.idatt2105.backend.dto.user.GetUserInfoResponse;
import edu.ntnu.idatt2105.backend.dto.bookmark.ToggleBookmarkRequest;
import edu.ntnu.idatt2105.backend.dto.user.UpdateUserInfoRequest;
import edu.ntnu.idatt2105.backend.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

class UserServiceTest {

  @Mock
  private UserRepository userRepository;

  @Mock
  private ItemRepository itemRepository;

  @Mock
  private BookmarkRepository bookmarkRepository;

  @Mock
  private PasswordEncoder passwordEncoder;

  @Mock
  SecurityContext securityContext;

  @Mock
  Authentication authentication;

  @InjectMocks
  private UserService userService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void getUserByEmail_ShouldReturnUser() {
    User mockUser = new User(123, "test@example.com", 12345678, 123, "John", "Doe", "hashedPassword", "ROLE_USER", 12, 43, "fe", 123, "fe");
    when(userRepository.getUserByEmail("test@example.com")).thenReturn(mockUser);

    User user = userService.getUserByEmail("test@example.com");

    assertNotNull(user);
    assertEquals("test@example.com", user.getEmail());
  }

  @Test
  void checkCredentials_ShouldReturnTrue_WhenPasswordMatches() {
    SigninRequest request = new SigninRequest("test@example.com", "password123");
    when(userRepository.getPasswordByEmail("test@example.com")).thenReturn("hashedPassword");
    when(passwordEncoder.matches("password123", "hashedPassword")).thenReturn(true);

    assertTrue(userService.checkCredentials(request));
  }

  @Test
  void checkCredentials_ShouldReturnFalse_WhenPasswordDoesNotMatch() {
    SigninRequest request = new SigninRequest("test@example.com", "wrongPassword");
    when(userRepository.getPasswordByEmail("test@example.com")).thenReturn("hashedPassword");
    when(passwordEncoder.matches("wrongPassword", "hashedPassword")).thenReturn(false);

    assertFalse(userService.checkCredentials(request));
  }

  @Test
  void createUser_ShouldEncodePasswordAndSaveUser() {
    SignupRequest request = new SignupRequest("test@example.com", "password123", "John", "Doe", 12345678, 54);
    User user = UserMapper.INSTANCE.signupRequestToUser(request);
    user.setRole("ROLE_USER");
    when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");

    userService.createUser(request);

    verify(userRepository).createUser(argThat(savedUser ->
        savedUser.getEmail().equals("test@example.com") &&
            savedUser.getPassword().equals("encodedPassword") &&
            savedUser.getRole().equals("ROLE_USER")
    ));
  }

  @Test
  void getUser_ShouldReturnUserInfoResponse() {
    User mockUser = new User(1, "test@example.com", 12345678, 123, "John", "Doe", "hashedPassword", "ROLE_USER", 12, 43, "fe", 123, "fe");
    when(userRepository.getUser(1)).thenReturn(mockUser);

    GetUserInfoResponse response = userService.getUser(1);

    assertNotNull(response);
    assertEquals(mockUser.getEmail(), response.getEmail());
  }

  @Test
  void editUser_ShouldUpdateUser() {
    UpdateUserInfoRequest updateUserInfoRequest = new UpdateUserInfoRequest();
    updateUserInfoRequest.setName("John");
    updateUserInfoRequest.setSurname("Doe");

    when(securityContext.getAuthentication()).thenReturn(authentication);
    when(authentication.getName()).thenReturn("1");
    SecurityContextHolder.setContext(securityContext);

    userService.editUser(updateUserInfoRequest);

    verify(userRepository, times(1)).updateUser(any(User.class));
  }

  @Test
  void addUserItem_ShouldAddItem() {
    AddItemRequest addItemRequest = new AddItemRequest();
    addItemRequest.setName("Test Item");

    when(securityContext.getAuthentication()).thenReturn(authentication);
    when(authentication.getName()).thenReturn("1");
    SecurityContextHolder.setContext(securityContext);

    userService.addUserItem(addItemRequest);

    verify(itemRepository, times(1)).addItem(any(Item.class));
  }

  @Test
  void editUserItem_ShouldEditItem() {
    EditItemRequest editItemRequest = new EditItemRequest();
    editItemRequest.setName("Updated Item");

    when(securityContext.getAuthentication()).thenReturn(authentication);
    when(authentication.getName()).thenReturn("1");
    SecurityContextHolder.setContext(securityContext);

    userService.editUserItem(editItemRequest);

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

    ItemsResponse response = userService.getUserItems();

    assertNotNull(response);
    assertEquals(1, response.getItems().length);
    assertEquals(mockItem.getName(), response.getItems()[0].getName());
  }

  @Test
  void toggleBookmark_ShouldAddBookmark_WhenNotSet() {
    ToggleBookmarkRequest request = new ToggleBookmarkRequest();
    request.setItemID(1);
    when(securityContext.getAuthentication()).thenReturn(authentication);
    when(authentication.getName()).thenReturn("1");
    SecurityContextHolder.setContext(securityContext);
    when(bookmarkRepository.checkBookmark(any(Bookmark.class))).thenReturn(false);

    userService.toggleBookmark(request);

    verify(bookmarkRepository, times(1)).addBookmark(any(Bookmark.class));
    verify(bookmarkRepository, never()).deleteBookmark(any(Bookmark.class));
  }

  @Test
  void toggleBookmark_ShouldDeleteBookmark_WhenSet() {
    ToggleBookmarkRequest request = new ToggleBookmarkRequest();
    request.setItemID(1);
    when(securityContext.getAuthentication()).thenReturn(authentication);
    when(authentication.getName()).thenReturn("1");
    SecurityContextHolder.setContext(securityContext);
    when(bookmarkRepository.checkBookmark(any(Bookmark.class))).thenReturn(true);

    userService.toggleBookmark(request);

    verify(bookmarkRepository, times(1)).deleteBookmark(any(Bookmark.class));
    verify(bookmarkRepository, never()).addBookmark(any(Bookmark.class));
  }
}
