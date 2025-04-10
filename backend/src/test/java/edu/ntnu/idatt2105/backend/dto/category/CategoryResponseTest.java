package edu.ntnu.idatt2105.backend.dto.category;

import edu.ntnu.idatt2105.backend.dto.category.CategoryResponse;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CategoryResponseTest {

  @Test
  void testCategoryResponseInitialization() {
    String[] categories = {"Technology", "Science", "Arts"};
    CategoryResponse response = new CategoryResponse(categories);

    assertNotNull(response);
    assertArrayEquals(categories, response.getCategories());
  }
}