package edu.ntnu.idatt2105.backend.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetStoreItemResponse {
  int itemID;
  String name;
  String category;
  String state;
  String seller;
  String description;
  String published;
  int price;
  boolean bookmark;
}
