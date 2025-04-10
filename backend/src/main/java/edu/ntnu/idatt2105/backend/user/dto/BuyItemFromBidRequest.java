package edu.ntnu.idatt2105.backend.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * a DTO for receiving buy item from bid request.
 */
@Getter
@Setter
@Schema(description = "Data transfer object for buying items from bid")
public class BuyItemFromBidRequest {
  @Schema(description = "bid id for bid being purchased", example = "42")
  int bidID;
}
