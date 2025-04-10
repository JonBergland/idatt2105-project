package edu.ntnu.idatt2105.backend.dto.item;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * a DTO for responding to get item.
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Data transfer object for responding to get item info")
public class ItemResponse {
  @Schema(description = "item id", example = "64")
  int itemID;
  @Schema(description = "item name", example = "sofa")
  String name;
  @Schema(description = "item category", example = "Møbler")
  String category;
  @Schema(description = "item state, available, reserved, sold or archived", example = "reserved")
  String state;
  @Schema(description = "seller's email", example = "seller@sale.com")
  String seller;
  @Schema(description = "item description", example = "Myk og god sofa")
  String description;
  @Schema(description = "item published", example = "2025-04-02 12:53:20")
  String published;
  @Schema(description = "item price", example = "2999")
  int price;
}
