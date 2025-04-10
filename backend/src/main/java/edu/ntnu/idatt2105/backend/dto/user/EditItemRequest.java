package edu.ntnu.idatt2105.backend.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * a DTO for editing a users item.
 */
@Getter
@Setter
@Schema(description = "Data transfer object for editing items")
public class EditItemRequest {
  @Schema(description = "item id of item being edited", example = "34")
  int itemID;
  @Schema(description = "new name of item", example = "Genser")
  String name;
  @Schema(description = "new description for item", example = "Varmer hele året:)")
  String description;
  @Schema(description = "new price of item", example = "250")
  int price;
  @Schema(description = "new category of item", example = "klær")
  String category;
}
