package edu.ntnu.idatt2105.backend.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Data transfer object for placing bid on an item")
public class PlaceBidRequest {
  @Schema(description = "item id of item", example = "15")
  int itemID;
  @Schema(description = "price to bid on item", example = "540")
  int askingPrice;
}
