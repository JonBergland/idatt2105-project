package edu.ntnu.idatt2105.backend.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * A DTO for getting bookmarked items.
 */
@Getter
@Setter
@Schema(description = "Data transfer object for requesting bookmarked items")
public class GetBookmarkedItemsRequest {
  @Schema(description = "pagination, page number then page size", example = "[0, 20]")
  int[] segmentOffset;
}
