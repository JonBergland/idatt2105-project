package edu.ntnu.idatt2105.backend.purchase.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Purchase {
  int itemID;
  int buyerID;
  int finalPrice;
}
