package edu.ntnu.idatt2105.backend.category;

import edu.ntnu.idatt2105.backend.category.model.Category;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
