package edu.ntnu.idatt2105.backend.service;

import edu.ntnu.idatt2105.backend.dto.bookmark.ToggleBookmarkRequest;
import edu.ntnu.idatt2105.backend.mapper.BookmarkMapper;
import edu.ntnu.idatt2105.backend.model.Bookmark;
import edu.ntnu.idatt2105.backend.repository.BookmarkRepository;
import edu.ntnu.idatt2105.backend.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * Service class for the Bookmark model.
 */
@Service
@RequiredArgsConstructor
public class BookmarkService {

  private final BookmarkRepository bookmarkRepository;

  private final ItemRepository itemRepository;

  /**
   * toggle the bookmark on an item for user.
   *
   * @param toggleBookmarkRequest the item
   */
  public void toggleBookmark(ToggleBookmarkRequest toggleBookmarkRequest) {
    Bookmark bookmark = BookmarkMapper.INSTANCE.bookmarkRequestToBookmark(toggleBookmarkRequest);
    String userID = SecurityContextHolder.getContext().getAuthentication().getName();
    bookmark.setUserID(Integer.parseInt(userID));

    boolean bookmarkSet = bookmarkRepository.checkBookmark(bookmark);
    if (bookmarkSet) {
      bookmarkRepository.deleteBookmark(bookmark);
    } else {
      bookmarkRepository.addBookmark(bookmark);
    }
  }


}
