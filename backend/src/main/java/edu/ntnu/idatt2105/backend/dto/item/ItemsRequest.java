package edu.ntnu.idatt2105.backend.dto.item;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * a DTO for getting items with filter request.
 */
@Setter
@Getter
@Schema(description = "Data transfer object for requesting filtered items")
public class ItemsRequest {
  @Schema(description = "category to filter by", example = "Hage")
  String category;
  @Schema(description = "Filter items with names containing search word", example = "kjøleskap")
  String searchWord;
  @Schema(description = "filter pris fra og til", example = "[100, 500]")
  int[] priceMinMax;
  @Schema(description = "sort by following: price_ASC, price_DESC, published_ASC, published_DESC", example = "price_ASC")
  String sort;
  @Schema(description = "pagination, page number then page size", example = "[0, 20]")
  int[] segmentOffset;
}
