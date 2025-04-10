package edu.ntnu.idatt2105.backend.model;

import lombok.Getter;
import lombok.Setter;

/**
 * a model representing a purchase.
 */
@Getter
@Setter
public class Purchase {
  int itemID;
  int buyerID;
  int finalPrice;
}
