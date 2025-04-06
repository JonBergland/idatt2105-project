package edu.ntnu.idatt2105.backend.bookmark.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Bookmark {
  private int userID;
  private int itemID;
}
