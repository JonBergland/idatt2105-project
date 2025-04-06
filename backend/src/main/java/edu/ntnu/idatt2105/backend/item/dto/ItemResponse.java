package edu.ntnu.idatt2105.backend.item.dto;

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
public class ItemResponse {
  int itemID;
  String name;
  String category;
  String state;
  String seller;
  String description;
  String published;
  int price;
}
