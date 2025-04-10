package edu.ntnu.idatt2105.backend.dto.item;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

/**
 * a DTO for deleting an item.
 */
@Getter
@Setter
@Schema(description = "Data transfer object for deleting items")
public class DeleteItemRequest {
  @Positive
  @Schema(description = "item id of item being deleted", example = "74")
  int itemID;
}
