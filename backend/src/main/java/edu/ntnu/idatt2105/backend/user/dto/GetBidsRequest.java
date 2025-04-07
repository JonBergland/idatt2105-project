package edu.ntnu.idatt2105.backend.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetBidsRequest {
  int itemID;
  int[] segmentOffset;
}
