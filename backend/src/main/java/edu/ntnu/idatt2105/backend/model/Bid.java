package edu.ntnu.idatt2105.backend.model;

import lombok.Getter;
import lombok.Setter;

/**
 * A model representing a bid.
 */
@Getter
@Setter
public class Bid {
  private int bidID;
  private int itemID;
  private int userID;
  private int askingPrice;
  private String status;
  private String published;
  private String itemName;
  private String email;
}
