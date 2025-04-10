package edu.ntnu.idatt2105.backend.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * A DTO for responding to bookmarked items request.
 */
@Getter
@Setter
@Schema(description = "Data transfer object for responding with bookmarked items")
public class GetBookmarkedItemsResponse {
  @Schema(description = "item id of item", example = "83")
  int itemID;
  @Schema(description = "name of item", example = "Kjøleskap")
  String name;
  @Schema(description = "category of item", example = "Elektronikk")
  String category;
  @Schema(description = "state of item", example = "Available")
  String state;
  @Schema(description = "Seller's email", example = "seller@sale.com")
  String seller;
  @Schema(description = "item description", example = "kaldt kjøleskap")
  String description;
  @Schema(description = "item published", example = "2025-04-02 12:53:20")
  String published;
  @Schema(description = "item price", example = "1999")
  int price;
}
