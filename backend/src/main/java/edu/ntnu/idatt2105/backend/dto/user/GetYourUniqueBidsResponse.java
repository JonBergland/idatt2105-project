package edu.ntnu.idatt2105.backend.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * a DTO for responding with items user has bid on
 */
@Getter
@Setter
@Schema(description = "Data transfer object for responding with items user has bid on")
public class GetYourUniqueBidsResponse {
  @Schema(description = "item id of item", example = "42")
  int itemID;
  @Schema(description = "user id of user", example = "95")
  int userID;
  @Schema(description = "name of item bid on", example = "Gaffel")
  String itemName;
  @Schema(description = "seller's email", example = "seller@sale.com")
  String seller;
}
