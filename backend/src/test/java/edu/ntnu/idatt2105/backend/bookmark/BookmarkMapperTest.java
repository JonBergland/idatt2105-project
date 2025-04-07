package edu.ntnu.idatt2105.backend.bookmark;

import edu.ntnu.idatt2105.backend.bookmark.model.Bookmark;
import edu.ntnu.idatt2105.backend.user.dto.ToggleBookmarkRequest;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BookmarkMapperTest {

  private final BookmarkMapper bookmarkMapper = BookmarkMapper.INSTANCE;

  @Test
  void testBookmarkRequestToBookmark() {
    ToggleBookmarkRequest toggleBookmarkRequest = new ToggleBookmarkRequest();
    toggleBookmarkRequest.setItemID(1);

    Bookmark bookmark = bookmarkMapper.bookmarkRequestToBookmark(toggleBookmarkRequest);

    assertNotNull(bookmark);
    assertEquals(toggleBookmarkRequest.getItemID(), bookmark.getItemID());
  }
}