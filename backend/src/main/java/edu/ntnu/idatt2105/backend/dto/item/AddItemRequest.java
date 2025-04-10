package edu.ntnu.idatt2105.backend.dto.item;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * A DTO for adding items to user.
 */
@Setter
@Getter
@Schema(description = "Data transfer object for adding items")
public class AddItemRequest {
  @Schema(description = "Item name", example = "Stol")
  String name;
  @Schema(description = "Item description", example = "Beste stolen i byen!")
  String description;
  @Schema(description = "Price of item", example = "199")
  int price;
  @Schema(description = "Category of item", example = "Møbler")
  String category;
}
