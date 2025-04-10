package edu.ntnu.idatt2105.backend.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Data transfer object for responding with item with user specific info")
public class GetStoreItemResponse {
  @Schema(description = "item id", example = "74")
  int itemID;
  @Schema(description = "item name", example = "Hammer")
  String name;
  @Schema(description = "item category", example = "verktøy")
  String category;
  @Schema(description = "item state", example = "sold")
  String state;
  @Schema(description = "seller email", example = "seller@sale.com")
  String seller;
  @Schema(description = "item description", example = "Stor hammer")
  String description;
  @Schema(description = "item published", example = "2025-04-02 12:53:20")
  String published;
  @Schema(description = "item price", example = "195")
  int price;
  @Schema(description = "is bookmarked", example = "false")
  boolean bookmark;
}
