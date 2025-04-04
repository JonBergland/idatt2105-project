package edu.ntnu.idatt2105.backend.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EditItemRequest {
  int itemID;
  String name;
  String description;
  int price;
  String category;
}
