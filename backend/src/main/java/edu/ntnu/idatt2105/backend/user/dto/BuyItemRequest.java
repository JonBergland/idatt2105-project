package edu.ntnu.idatt2105.backend.user.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * a DTO for receiving a buy item request.
 */
@Getter
@Setter
public class BuyItemRequest {
  int itemID;
}
