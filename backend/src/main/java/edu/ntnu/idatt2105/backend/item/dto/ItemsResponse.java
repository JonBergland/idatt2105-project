package edu.ntnu.idatt2105.backend.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
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
