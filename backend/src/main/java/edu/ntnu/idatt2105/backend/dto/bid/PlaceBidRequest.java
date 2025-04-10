package edu.ntnu.idatt2105.backend.dto.bid;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

/**
 * a DTO for requesting a bid to be placed in item.
 */
@Getter
@Setter
@Schema(description = "Data transfer object for placing bid on an item")
public class PlaceBidRequest {
  @Positive
  @Schema(description = "item id of item", example = "15")
  int itemID;
  @Positive
  @Schema(description = "price to bid on item", example = "540")
  int askingPrice;
}
