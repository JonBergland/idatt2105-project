package edu.ntnu.idatt2105.backend.category.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * A DTO for responding to get category request.
 */
@Getter
@AllArgsConstructor
@Schema(description = "Data transfer object for responding with all categories")
public class CategoryResponse {
  @Schema(description = "array of categories", example = "[Møbler, Hage, Elektronikk]")
  String[] categories;
}
