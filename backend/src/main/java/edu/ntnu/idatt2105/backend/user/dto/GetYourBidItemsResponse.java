package edu.ntnu.idatt2105.backend.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetYourBidItemsResponse {
  int itemID;
  int userID;
  String itemName;
  String buyer;
}
