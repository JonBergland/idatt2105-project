package edu.ntnu.idatt2105.backend.service;

import edu.ntnu.idatt2105.backend.model.Category;
import edu.ntnu.idatt2105.backend.repository.CategoryRepository;
import edu.ntnu.idatt2105.backend.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

  @Mock
  private CategoryRepository categoryRepository;

  @InjectMocks
  private CategoryService categoryService;

  private Category mockCategory;

  @BeforeEach
  void setUp() {
    mockCategory = new Category(new String[]{"Electronics", "Books"});
  }

  @Test
  void getCategories_ShouldReturnCategories_WhenRepositoryReturnsData() {
    when(categoryRepository.getCategories()).thenReturn(mockCategory);

    Category result = categoryService.getCategories();

    assertNotNull(result);
    assertArrayEquals(new String[]{"Electronics", "Books"}, result.getCategories());
    verify(categoryRepository, times(1)).getCategories();
  }
}
