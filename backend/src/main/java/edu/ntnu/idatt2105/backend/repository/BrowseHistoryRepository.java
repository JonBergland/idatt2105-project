package edu.ntnu.idatt2105.backend.repository;

import edu.ntnu.idatt2105.backend.model.BrowseHistory;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * a repository for managing browse history.
 */
@Repository
@RequiredArgsConstructor
public class BrowseHistoryRepository {

  private final JdbcTemplate jdbcTemplate;

  /**
   * add a browse history to the database.
   *
   * @param browseHistory the browse history
   */
  public void addUpdateBrowseHistory(BrowseHistory browseHistory) {
    jdbcTemplate.update("INSERT INTO Browse_history (user_id, item_id) "
        + "VALUES (?, ?) "
        + "ON DUPLICATE KEY UPDATE "
        + "published = CURRENT_TIMESTAMP",
        browseHistory.getUserID(),
        browseHistory.getItemID());
  }
}
