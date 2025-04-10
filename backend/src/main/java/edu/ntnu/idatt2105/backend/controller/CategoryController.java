package edu.ntnu.idatt2105.backend.controller;

import edu.ntnu.idatt2105.backend.dto.category.CategoryResponse;
import edu.ntnu.idatt2105.backend.model.Category;
import edu.ntnu.idatt2105.backend.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Category controller", description = "Category management endpoints")
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
  @Operation(summary = "Get categories", description = "Gets all categories")
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
