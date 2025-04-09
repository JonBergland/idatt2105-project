package edu.ntnu.idatt2105.backend.user.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * a DTO for deleting an item.
 */
@Getter
@Setter
public class DeleteItemRequest {
  int itemID;
}
