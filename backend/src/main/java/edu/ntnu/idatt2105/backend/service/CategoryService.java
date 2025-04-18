package edu.ntnu.idatt2105.backend.service;

import edu.ntnu.idatt2105.backend.model.Category;
import edu.ntnu.idatt2105.backend.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Service class for the Category model.
 */
@Service
@RequiredArgsConstructor
public class CategoryService {

  private final CategoryRepository categoryRepository;

  /**
   * gets all categories.
   *
   * @return the category object
   */
  public Category getCategories() {
    return categoryRepository.getCategories();
  }
}
