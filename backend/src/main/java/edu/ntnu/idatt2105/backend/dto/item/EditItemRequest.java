package edu.ntnu.idatt2105.backend.dto.item;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

/**
 * a DTO for editing a users item.
 */
@Getter
@Setter
@Schema(description = "Data transfer object for editing items")
public class EditItemRequest {
  @Positive
  @Schema(description = "item id of item being edited", example = "34")
  int itemID;
  @NotBlank
  @Schema(description = "new name of item", example = "Genser")
  String name;
  @NotBlank
  @Schema(description = "new description for item", example = "Varmer hele året:)")
  String description;
  @Positive
  @Schema(description = "new price of item", example = "250")
  int price;
  @NotBlank
  @Schema(description = "new category of item", example = "klær")
  String category;
}
