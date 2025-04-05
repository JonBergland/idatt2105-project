package edu.ntnu.idatt2105.backend.user.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * a DTO for editing a users item.
 */
@Getter
@Setter
public class EditItemRequest {
  int itemID;
  String name;
  String description;
  int price;
  String category;
}
