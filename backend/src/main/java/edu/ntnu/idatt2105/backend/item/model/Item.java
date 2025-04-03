package edu.ntnu.idatt2105.backend.item.model;

import lombok.Getter;
import lombok.Setter;

/**
 * A model representing an item.
 */
@Getter
@Setter
public class Item {
  private String name;
  private int itemID;
  private String category;
  private String seller;
  private String description;
  private String published;
  private int price;
}
