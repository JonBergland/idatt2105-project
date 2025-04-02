package edu.ntnu.idatt2105.backend.category;

import edu.ntnu.idatt2105.backend.category.model.Category;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryService {

  private final Logger logger = LoggerFactory.getLogger(CategoryService.class);

  private final CategoryRepository categoryRepository;

  public Category getCategories() {
    return categoryRepository.getCategories();
  }
}
