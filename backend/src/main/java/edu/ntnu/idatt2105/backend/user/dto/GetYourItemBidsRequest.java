package edu.ntnu.idatt2105.backend.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * a DTO for requesting bids user placed on an item.
 */
@Getter
@Setter
@Schema(description = "Data transfer object for requesting bids user placed on an item")
public class GetYourItemBidsRequest {
  @Schema(description = "item id", example = "88")
  int itemID;
  @Schema(description = "pagination, page number then page size", example = "[0, 20]")
  int[] segmentOffset;
}
