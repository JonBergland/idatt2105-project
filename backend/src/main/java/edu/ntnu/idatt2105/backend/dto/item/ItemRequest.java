package edu.ntnu.idatt2105.backend.dto.item;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

/**
 * a DTO for get item request.
 */
@Getter
@Setter
@Schema(description = "Data transfer object for requesting an items info")
public class ItemRequest {
  @Positive
  @Schema(description = "item id", example = "12")
  int itemID;
}
