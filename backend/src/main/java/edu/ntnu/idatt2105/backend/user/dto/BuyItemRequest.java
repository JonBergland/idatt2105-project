package edu.ntnu.idatt2105.backend.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * a DTO for receiving a buy item request.
 */
@Getter
@Setter
@Schema(description = "Data transfer object for buying items")
public class BuyItemRequest {
  @Schema(description = "item id of item being bought", example = "51")
  int itemID;
}
