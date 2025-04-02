package edu.ntnu.idatt2105.backend.category;

import edu.ntnu.idatt2105.backend.category.dto.CategoryResponse;
import edu.ntnu.idatt2105.backend.category.model.Category;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/**
 * A controller for getting and modifying categories.
 */
@RestController
@RequestMapping(value = "/api/store/category")
@CrossOrigin
@RequiredArgsConstructor
public class CategoryController {

  private final Logger logger = LoggerFactory.getLogger(CategoryController.class);

  private final CategoryService categoryService;

  /**
   * Endpoint for getting categories.
   *
   * @return the categories
   */
  @GetMapping()
  public CategoryResponse getCategories() {
    try {
      logger.info("retrieves categories from database");
      Category category = categoryService.getCategories();
      return new CategoryResponse(category.getCategories());
    } catch (DataAccessException e) {
      logger.error("could not get categories from db");
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
