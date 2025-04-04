package edu.ntnu.idatt2105.backend.item;

import edu.ntnu.idatt2105.backend.item.dto.ItemsRequest;
import edu.ntnu.idatt2105.backend.item.model.Item;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * a repository for getting items.
 */
@Repository
@RequiredArgsConstructor
public class ItemRepository {

  private final JdbcTemplate jdbcTemplate;

  /**
   * get item from id from the database.
   *
   * @param id the item id
   * @return the item
   */
  public Item getItem(int id) {
    return jdbcTemplate.queryForObject(
        "SELECT Item.*, Item.id AS itemID, User.email AS seller, Categories.category_name AS category FROM Item "
            + "LEFT JOIN User ON Item.user_id = User.id "
            + "LEFT JOIN Categories ON Item.category_id = Categories.id "
            + " WHERE Item.id = ?",
        new Object[]{id},
        new BeanPropertyRowMapper<>(Item.class)
    );
  }

  /**
   * get items with filters from the database.
   *
   * @param itemsRequest the filters
   * @return the items
   */
  public Item[] getItems(ItemsRequest itemsRequest) {
    String sql = buildFilterSQL(itemsRequest);
    Object[] params = buildParams(itemsRequest);
    List<Item> itemList = jdbcTemplate.query(sql, params, new BeanPropertyRowMapper<>(Item.class));
    return itemList.toArray(new Item[0]);
  }

  /**
   * Helper method for generating sql query based on passed filters.
   *
   * @param itemsRequest the filters
   * @return the sql query
   */
  private String buildFilterSQL(ItemsRequest itemsRequest) {
    StringBuilder sb = new StringBuilder(
        """
            SELECT Item.*, Item.id AS itemID, User.email AS seller, Categories.category_name AS category FROM Item
            LEFT JOIN User ON Item.user_id = User.id
            LEFT JOIN Categories ON Item.category_id = Categories.id WHERE 1=1""");
    if (itemsRequest.getCategory() != null) {
      sb.append(" AND Categories.category_name = ?");
    }
    if (itemsRequest.getPriceMinMax() != null) {
      sb.append(" AND price BETWEEN ? AND ?");
    }
    if (itemsRequest.getSearchWord() != null) {
      sb.append(" AND Item.name LIKE ?");
    }
    switch (itemsRequest.getSort() != null ? itemsRequest.getSort() : "") {
      case "price_ASC":
        sb.append(" ORDER BY price ASC");
        break;
      case "price_DESC":
        sb.append(" ORDER BY price DESC");
        break;
      case "published_ASC":
        sb.append(" ORDER BY published DESC");
        break;
      case "published_DESC":
        sb.append(" ORDER BY published ASC");
        break;
      default:
        break;
    }
    if (itemsRequest.getSegmentOffset() != null) {
      sb.append(" LIMIT ? OFFSET ?");
    }
    return sb.toString();
  }

  /**
   * helper method for collecting query parameters based on selected filters.
   *
   * @param itemsRequest the filters
   * @return the query parameters
   */
  private Object[] buildParams(ItemsRequest itemsRequest) {
    List<Object> params = new ArrayList<>();
    if (itemsRequest.getCategory() != null) {
      params.add(itemsRequest.getCategory());
    }
    if (itemsRequest.getPriceMinMax() != null) {
      params.add(itemsRequest.getPriceMinMax()[0]);
      params.add(itemsRequest.getPriceMinMax()[1]);
    }
    if (itemsRequest.getSearchWord() != null) {
      params.add("%" + itemsRequest.getSearchWord() + "%");
    }
    if (itemsRequest.getSegmentOffset() != null) {
      params.add(itemsRequest.getSegmentOffset()[1]);
      params.add(itemsRequest.getSegmentOffset()[0] * itemsRequest.getSegmentOffset()[1]);
    }
    return params.toArray();
  }

  public void addItem(Item item) {
    jdbcTemplate.update(
        "INSERT INTO Item (name, description, price, category_id, user_id) "
            + "VALUES (?, ?, ?, (SELECT id FROM Categories WHERE category_name = ?), ?)",
        item.getName(), item.getDescription(), item.getPrice(), item.getCategory(), item.getSellerID()
    );
  }

  public void editItem(Item item) {
    jdbcTemplate.update(
        "UPDATE Item " +
        "SET name = ?, description = ?, price = ?, category_id = (SELECT id FROM Categories WHERE category_name = ?) " +
        "WHERE id = ? AND user_id = ?",
        item.getName(),
        item.getDescription(),
        item.getPrice(),
        item.getCategory(),
        item.getItemID(),
        item.getSellerID());
  }
}