package edu.ntnu.idatt2105.backend.category;

import edu.ntnu.idatt2105.backend.category.model.Category;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CategoryRepository {

  private final JdbcTemplate jdbcTemplate;

  public Category getCategories() {
    List<String> categories = jdbcTemplate.queryForList(
        "SELECT category_name FROM Categories",
        String.class);
    return new Category(categories.toArray(new String[0]));
  }
}
