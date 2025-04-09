package edu.ntnu.idatt2105.backend.user.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * A DTO for getting bookmarked items.
 */
@Getter
@Setter
public class GetBookmarkedItemsRequest {
  int[] segmentOffset;
}
