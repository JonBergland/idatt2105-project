package edu.ntnu.idatt2105.backend.category.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * A DTO for responding to get category request.
 */
@Getter
@AllArgsConstructor
public class CategoryResponse {
  String[] categories;
}
