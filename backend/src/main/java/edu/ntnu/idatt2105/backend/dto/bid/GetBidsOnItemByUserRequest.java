package edu.ntnu.idatt2105.backend.dto.bid;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * a DTO for requesting bids by a user on an item.
 */
@Getter
@Setter
@Schema(description = "Data transfer object for requesting bids by a user on an item")
public class GetBidsOnItemByUserRequest {
  @Positive
  @Schema(description = "item id of item with bids", example = "234")
  int itemID;
  @Positive
  @Schema(description = "user id of bidding user", example = "49")
  int userID;
  @Size(min = 2, max = 2)
  @Positive
  @Schema(description = "pagination, page number then page size", example = "[0, 20]")
  int[] segmentOffset;
}
