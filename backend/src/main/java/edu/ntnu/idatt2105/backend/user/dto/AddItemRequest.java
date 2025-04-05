package edu.ntnu.idatt2105.backend.user.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * A DTO for adding items to user.
 */
@Setter
@Getter
public class AddItemRequest {
  String name;
  String description;
  int price;
  String category;
}
