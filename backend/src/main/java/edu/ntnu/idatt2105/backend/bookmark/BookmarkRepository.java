package edu.ntnu.idatt2105.backend.bookmark;

import edu.ntnu.idatt2105.backend.bookmark.model.Bookmark;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BookmarkRepository {

  private final JdbcTemplate jdbcTemplate;

  public void addBookmark(Bookmark bookmark) {
    jdbcTemplate.update("INSERT INTO Bookmark (user_id, item_id) VALUES (?, ?)",
        bookmark.getUserID(),
        bookmark.getItemID());
  }

  public void deleteBookmark(Bookmark bookmark) {
    jdbcTemplate.update("DELETE FROM Bookmark WHERE user_id = ? AND item_id = ?",
        bookmark.getUserID(),
        bookmark.getItemID());
  }

  public boolean checkBookmark(Bookmark bookmark) {
    return jdbcTemplate.queryForObject(
        "SELECT IF(EXISTS("
        + "  SELECT 1"
        + "  FROM Bookmark"
        + "  WHERE user_id = ? AND item_id = ?"
        + "), 'true', 'false') AS finnes",
        new Object[]{bookmark.getUserID(), bookmark.getItemID()},
        Boolean.class);
  }
}
