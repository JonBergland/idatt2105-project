package edu.ntnu.idatt2105.backend.item.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * a DTO for responding to get items with filter.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemsResponse {
  ItemResponse[] items;
}
