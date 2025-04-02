package edu.ntnu.idatt2105.backend.category;

import edu.ntnu.idatt2105.backend.category.model.Category;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * Repository for updating, deleting and inserting to the Category table.
 */
@Repository
@RequiredArgsConstructor
public class CategoryRepository {

  private final JdbcTemplate jdbcTemplate;

  /**
   * gets the categories from the database.
   *
   * @return the categories
   * @throws DataAccessException if something goes wrong
   */
  public Category getCategories() throws DataAccessException {
    List<String> categories = jdbcTemplate.queryForList(
        "SELECT category_name FROM Categories",
        String.class);
    return new Category(categories.toArray(new String[0]));
  }
}
