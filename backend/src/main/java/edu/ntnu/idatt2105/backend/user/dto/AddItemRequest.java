package edu.ntnu.idatt2105.backend.user.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AddItemRequest {
  String name;
  String description;
  int price;
  String category;
}
