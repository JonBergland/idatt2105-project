package edu.ntnu.idatt2105.backend.dto.bid;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * a DTO for requesting bids user placed on an item.
 */
@Getter
@Setter
@Schema(description = "Data transfer object for requesting bids user placed on an item")
public class GetYourItemBidsRequest {
  @Positive
  @Schema(description = "item id", example = "88")
  int itemID;
  @Size(min = 2, max = 2)
  @Schema(description = "pagination, page number then page size", example = "[0, 20]")
  int[] segmentOffset;
}
