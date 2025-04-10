package edu.ntnu.idatt2105.backend.dto.bookmark;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

/**
 * a DTO for requesting bookmark in item to toggle.
 */
@Getter
@Setter
@Schema(description = "Data transfer object for toggling bookmark on item")
public class ToggleBookmarkRequest {
  @Positive
  @Schema(description = "item id of item", example = "10")
  int itemID;
}
