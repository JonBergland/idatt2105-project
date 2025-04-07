package edu.ntnu.idatt2105.backend.bid.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Bid {
  private int itemID;
  private int userID;
  private int askingPrice;
  private String status;
  private String published;
}
