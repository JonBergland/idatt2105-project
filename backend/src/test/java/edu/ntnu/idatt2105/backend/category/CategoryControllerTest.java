package edu.ntnu.idatt2105.backend.category;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import edu.ntnu.idatt2105.backend.category.dto.CategoryResponse;
import edu.ntnu.idatt2105.backend.category.model.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;
import org.springframework.web.server.ResponseStatusException;

@ExtendWith(MockitoExtension.class)
class CategoryControllerTests {

  @Mock
  private CategoryService categoryService;

  @InjectMocks
  private CategoryController categoryController;

  private Category category;

  @BeforeEach
  void setUp() {
    category = new Category(new String[]{"Electronics", "Books", "Clothing"});
  }

  @Test
  void getCategories_ShouldReturnCategoryResponse() {
    when(categoryService.getCategories()).thenReturn(category);

    CategoryResponse response = categoryController.getCategories();

    assertNotNull(response);
    assertArrayEquals(category.getCategories(), response.getCategories());
  }

  @Test
  void getCategories_ShouldThrowResponseStatusException_WhenDataAccessExceptionOccurs() {
    when(categoryService.getCategories()).thenThrow(new DataAccessException("Database error") {});

    assertThrows(ResponseStatusException.class, () -> categoryController.getCategories());
  }
}
