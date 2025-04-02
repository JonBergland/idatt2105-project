package edu.ntnu.idatt2105.backend.category.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CategoryTest {

  @Test
  void testCategoryInitialization() {
    String[] categories = {"Technology", "Science", "Arts"};
    Category category = new Category(categories);

    assertNotNull(category);
    assertArrayEquals(categories, category.getCategories());
  }

  @Test
  void testCategorySetter() {
    String[] categories = {"Technology", "Science", "Arts"};
    Category category = new Category(null);
    category.setCategories(categories);
    assertNotNull(category);
    assertArrayEquals(categories, category.getCategories());
  }
}
