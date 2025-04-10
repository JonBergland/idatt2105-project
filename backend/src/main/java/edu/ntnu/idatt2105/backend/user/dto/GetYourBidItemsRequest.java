package edu.ntnu.idatt2105.backend.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Data transfer object for requesting users who have bid on users items")
public class GetYourBidItemsRequest {
  @Schema(description = "pagination, page number then page size", example = "[0, 20]")
  int[] SegmentOffset;
}
