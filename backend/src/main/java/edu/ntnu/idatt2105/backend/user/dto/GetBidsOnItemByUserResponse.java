package edu.ntnu.idatt2105.backend.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetBidsOnItemByUserResponse {
  int bidID;
  int itemID;
  int askingPrice;
  String status;
  String published;
}
