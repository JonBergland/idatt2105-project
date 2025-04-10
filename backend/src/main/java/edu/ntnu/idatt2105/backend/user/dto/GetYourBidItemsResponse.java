package edu.ntnu.idatt2105.backend.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Data transfer object for responding with users who bid on user's items")
public class GetYourBidItemsResponse {
  @Schema(description = "item id", example = "44")
  int itemID;
  @Schema(description = "user id of bidder", example = "98")
  int userID;
  @Schema(description = "item name", example = "Sokker")
  String itemName;
  @Schema(description = "buyer email", example = "buyer@sale.com")
  String buyer;
}
