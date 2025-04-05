package edu.ntnu.idatt2105.backend.item;

import edu.ntnu.idatt2105.backend.item.dto.ItemsRequest;
import edu.ntnu.idatt2105.backend.item.model.Item;
import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemRepositoryTest {

  @Mock
  private JdbcTemplate jdbcTemplate;

  @InjectMocks
  private ItemRepository itemRepository;

  private Item mockItem;

  @BeforeEach
  void setUp() {
    mockItem = new Item();
    mockItem.setItemID(1);
    mockItem.setName("Test Item");
    mockItem.setSeller("test@example.com");
    mockItem.setDescription("Test description");
    mockItem.setPublished("2025-04-01");
    mockItem.setPrice(100);
  }

  @Test
  void testGetItem() {
    when(jdbcTemplate.queryForObject(eq(
        "SELECT Item.*, Item.id AS itemID, User.email AS seller, Categories.category_name AS category FROM Item "
        + "LEFT JOIN User ON Item.user_id = User.id "
        + "LEFT JOIN Categories ON Item.category_id = Categories.id "
        + " WHERE Item.id = ?"
    ), any(Object[].class), any(BeanPropertyRowMapper.class)))
        .thenReturn(mockItem);

    Item retrievedItem = itemRepository.getItem(1);
    assertNotNull(retrievedItem);
    assertEquals(mockItem.getItemID(), retrievedItem.getItemID());
    assertEquals(mockItem.getName(), retrievedItem.getName());
  }

  @Test
  void testGetItems() {
    ItemsRequest itemsRequest = new ItemsRequest();
    itemsRequest.setCategory("Electronics");
    itemsRequest.setSearchWord("Test");
    itemsRequest.setPriceMinMax(new int[]{50, 200});
    itemsRequest.setSort("price_ASC");
    itemsRequest.setSegmentOffset(new int[]{0, 10});

    List<Item> itemList = Collections.singletonList(mockItem);
    when(jdbcTemplate.query(any(String.class), any(Object[].class), any(BeanPropertyRowMapper.class)))
        .thenReturn(itemList);

    Item[] retrievedItems = itemRepository.getItems(itemsRequest);
    assertNotNull(retrievedItems);
    assertEquals(1, retrievedItems.length);
    assertEquals(mockItem.getName(), retrievedItems[0].getName());
  }

  @Test
  void testAddItem() {
    itemRepository.addItem(mockItem);
    verify(jdbcTemplate, times(1)).update(
        eq("INSERT INTO Item (name, description, price, category_id, user_id) VALUES (?, ?, ?, (SELECT id FROM Categories WHERE category_name = ?), ?)"),
        eq(mockItem.getName()), eq(mockItem.getDescription()), eq(mockItem.getPrice()), eq(mockItem.getCategory()), eq(mockItem.getSellerID())
    );
  }

  @Test
  void testEditItem() {
    itemRepository.editItem(mockItem);
    verify(jdbcTemplate, times(1)).update(
        eq("UPDATE Item SET name = ?, description = ?, price = ?, category_id = (SELECT id FROM Categories WHERE category_name = ?) WHERE id = ? AND user_id = ?"),
        eq(mockItem.getName()), eq(mockItem.getDescription()), eq(mockItem.getPrice()), eq(mockItem.getCategory()), eq(mockItem.getItemID()), eq(mockItem.getSellerID())
    );
  }

  @Test
  void testGetItemsFromUserID() {
    List<Item> itemList = Collections.singletonList(mockItem);
    when(jdbcTemplate.query(
        eq("SELECT Item.*, Item.id AS itemID, User.email AS seller, Categories.category_name AS category FROM Item LEFT JOIN User ON Item.user_id = User.id LEFT JOIN Categories ON Item.category_id = Categories.id WHERE user_id = ?"),
        any(Object[].class),
        any(BeanPropertyRowMapper.class)
    )).thenReturn(itemList);

    Item[] retrievedItems = itemRepository.getItemsFromUserID(1);
    assertNotNull(retrievedItems);
    assertEquals(1, retrievedItems.length);
    assertEquals(mockItem.getName(), retrievedItems[0].getName());
  }
}
