package edu.ntnu.idatt2105.backend.item;

import edu.ntnu.idatt2105.backend.item.dto.ItemsRequest;
import edu.ntnu.idatt2105.backend.item.model.Item;
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
    when(jdbcTemplate.queryForObject(eq("SELECT *, id AS itemID FROM Item WHERE id = ?"), any(Object[].class), any(BeanPropertyRowMapper.class)))
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

    List<Item> itemList = Arrays.asList(mockItem);
    when(jdbcTemplate.query(any(String.class), any(Object[].class), any(BeanPropertyRowMapper.class)))
        .thenReturn(itemList);

    Item[] retrievedItems = itemRepository.getItems(itemsRequest);
    assertNotNull(retrievedItems);
    assertEquals(1, retrievedItems.length);
    assertEquals(mockItem.getName(), retrievedItems[0].getName());
  }
}
