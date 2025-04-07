package edu.ntnu.idatt2105.backend.bookmark.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * A model representing a bookmark.
 */
@Getter
@Setter
@AllArgsConstructor
public class Bookmark {
  private int userID;
  private int itemID;
}
