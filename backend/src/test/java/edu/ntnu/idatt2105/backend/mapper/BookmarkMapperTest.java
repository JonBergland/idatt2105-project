package edu.ntnu.idatt2105.backend.mapper;

import edu.ntnu.idatt2105.backend.model.Bookmark;
import edu.ntnu.idatt2105.backend.dto.bookmark.ToggleBookmarkRequest;
import edu.ntnu.idatt2105.backend.mapper.BookmarkMapper;
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