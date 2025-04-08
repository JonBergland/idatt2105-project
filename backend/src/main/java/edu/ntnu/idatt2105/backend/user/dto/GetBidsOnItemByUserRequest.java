package edu.ntnu.idatt2105.backend.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetBidsOnItemByUserRequest {
  int itemID;
  int userID;
  int[] segmentOffset;
}
