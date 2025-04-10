package edu.ntnu.idatt2105.backend.dto.item;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

/**
 * A DTO for adding items to user.
 */
@Setter
@Getter
@Schema(description = "Data transfer object for adding items")
public class AddItemRequest {
  @NotBlank
  @Schema(description = "Item name", example = "Stol")
  String name;
  @NotBlank
  @Schema(description = "Item description", example = "Beste stolen i byen!")
  String description;
  @Positive
  @Schema(description = "Price of item", example = "199")
  int price;
  @NotBlank
  @Schema(description = "Category of item", example = "Møbler")
  String category;
}
