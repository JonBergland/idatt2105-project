package edu.ntnu.idatt2105.backend.model;

import lombok.Getter;
import lombok.Setter;

/**
 * A model representing a browse history.
 */
@Setter
@Getter
public class BrowseHistory {
  private int userID;
  private int itemID;
}
