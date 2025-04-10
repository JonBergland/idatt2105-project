package edu.ntnu.idatt2105.backend.dto.bid;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * a DTO for requesting users who have bid on user's items.
 */
@Getter
@Setter
@Schema(description = "Data transfer object for requesting users who have bid on users items")
public class GetYourBidItemsRequest {
  @Size(min = 2, max = 2)
  @Schema(description = "pagination, page number then page size", example = "[0, 20]")
  int[] SegmentOffset;
}
