package edu.ntnu.idatt2105.backend.repository;

import edu.ntnu.idatt2105.backend.model.Category;
import edu.ntnu.idatt2105.backend.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryRepositoryTest {

  @Mock
  private JdbcTemplate jdbcTemplate;

  @InjectMocks
  private CategoryRepository categoryRepository;

  private List<String> mockCategories;

  @BeforeEach
  void setUp() {
    mockCategories = Arrays.asList("Electronics", "Books", "Clothing");
  }

  @Test
  void getCategories_ReturnsCategoryList() {
    // Arrange
    when(jdbcTemplate.queryForList(anyString(), eq(String.class))).thenReturn(mockCategories);

    // Act
    Category category = categoryRepository.getCategories();

    // Assert
    assertNotNull(category);
    assertArrayEquals(mockCategories.toArray(), category.getCategories());
    verify(jdbcTemplate, times(1)).queryForList(anyString(), eq(String.class));
  }

  @Test
  void getCategories_ThrowsDataAccessException() {
    // Arrange
    when(jdbcTemplate.queryForList(anyString(), eq(String.class))).thenThrow(new DataAccessException("Database error") {});

    // Act & Assert
    assertThrows(DataAccessException.class, () -> categoryRepository.getCategories());
    verify(jdbcTemplate, times(1)).queryForList(anyString(), eq(String.class));
  }
}
