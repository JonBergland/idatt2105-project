package edu.ntnu.idatt2105.backend.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * a DTO for getting items with filter request.
 */
@Setter
@Getter
public class ItemsRequest {
  String category;
  String searchWord;
  int[] priceMinMax;
  String sort;
  int[] segmentOffset;
}
