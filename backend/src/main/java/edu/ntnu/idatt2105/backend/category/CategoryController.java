package edu.ntnu.idatt2105.backend.category;

import edu.ntnu.idatt2105.backend.category.dto.CategoryResponse;
import edu.ntnu.idatt2105.backend.category.model.Category;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/store/category")
@CrossOrigin
@RequiredArgsConstructor
public class CategoryController {

  private final Logger logger = LoggerFactory.getLogger(CategoryController.class);

  private final CategoryService categoryService;

  @GetMapping()
  public CategoryResponse getCategories() {
    Category category = categoryService.getCategories();
    return new CategoryResponse(category.getCategories());
  }
}
