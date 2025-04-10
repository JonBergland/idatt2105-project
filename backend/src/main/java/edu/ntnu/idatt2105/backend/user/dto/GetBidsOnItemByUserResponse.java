package edu.ntnu.idatt2105.backend.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * a DTO for responding with bids from a user on an item.
 */
@Getter
@Setter
@Schema(description = "Data transfer object for responding with bids from a user on an item")
public class GetBidsOnItemByUserResponse {
  @Schema(description = "bid id of bid", example = "34")
  int bidID;
  @Schema(description = "item id of bid item", example = "39")
  int itemID;
  @Schema(description = "bidding price", example = "299")
  int askingPrice;
  @Schema(description = "bid status, accept = 1, decline = 0, pending = NULL", example = "NULL")
  String status;
  @Schema(description = "bid published", example = "2025-04-02 12:53:20")
  String published;
}
