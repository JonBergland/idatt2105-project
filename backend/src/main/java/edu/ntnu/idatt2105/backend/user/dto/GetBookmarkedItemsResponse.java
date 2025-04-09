package edu.ntnu.idatt2105.backend.user.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * A DTO for responding to bookmarked items request.
 */
@Getter
@Setter
public class GetBookmarkedItemsResponse {
  int itemID;
  String name;
  String category;
  String state;
  String seller;
  String description;
  String published;
  int price;
}
