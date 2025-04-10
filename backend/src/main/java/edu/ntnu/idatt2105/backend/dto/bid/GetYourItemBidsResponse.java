package edu.ntnu.idatt2105.backend.dto.bid;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * a DTO for responding with bids user placed on an item.
 */
@Getter
@Setter
@Schema(description = "Data transfer object for responding with bids user placed on an item")
public class GetYourItemBidsResponse {
  @Schema(description = "bid id of bid", example = "84")
  int bidID;
  @Schema(description = "item id of item bid on", example = "74")
  int itemID;
  @Schema(description = "price bid on item", example = "600")
  int askingPrice;
  @Schema(description = "bid status, accept = 1, decline = 0, pending = NULL", example = "0")
  String status;
  @Schema(description = "bid published", example = "2025-04-02 12:53:20")
  String published;
}
