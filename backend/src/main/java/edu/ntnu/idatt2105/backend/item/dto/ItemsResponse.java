package edu.ntnu.idatt2105.backend.item.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * a DTO for responding to get items with filter.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Data transfer object for responding with filtered items")
public class ItemsResponse {
  @Schema(description = "array of items")
  ItemResponse[] items;
}
