package edu.ntnu.idatt2105.backend.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import edu.ntnu.idatt2105.backend.dto.bookmark.ToggleBookmarkRequest;
import edu.ntnu.idatt2105.backend.model.Bookmark;
import edu.ntnu.idatt2105.backend.repository.BidRepository;
import edu.ntnu.idatt2105.backend.repository.BookmarkRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

class BookmarkServiceTest {

  @Mock
  private BookmarkRepository bookmarkRepository;

  @Mock
  SecurityContext securityContext;

  @Mock
  Authentication authentication;

  @InjectMocks
  private BookmarkService bookmarkService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void toggleBookmark_ShouldAddBookmark_WhenNotSet() {
    ToggleBookmarkRequest request = new ToggleBookmarkRequest();
    request.setItemID(1);
    when(securityContext.getAuthentication()).thenReturn(authentication);
    when(authentication.getName()).thenReturn("1");
    SecurityContextHolder.setContext(securityContext);
    when(bookmarkRepository.checkBookmark(any(Bookmark.class))).thenReturn(false);

    bookmarkService.toggleBookmark(request);

    verify(bookmarkRepository, times(1)).addBookmark(any(Bookmark.class));
    verify(bookmarkRepository, never()).deleteBookmark(any(Bookmark.class));
  }

  @Test
  void toggleBookmark_ShouldDeleteBookmark_WhenSet() {
    ToggleBookmarkRequest request = new ToggleBookmarkRequest();
    request.setItemID(1);
    when(securityContext.getAuthentication()).thenReturn(authentication);
    when(authentication.getName()).thenReturn("1");
    SecurityContextHolder.setContext(securityContext);
    when(bookmarkRepository.checkBookmark(any(Bookmark.class))).thenReturn(true);

    bookmarkService.toggleBookmark(request);

    verify(bookmarkRepository, times(1)).deleteBookmark(any(Bookmark.class));
    verify(bookmarkRepository, never()).addBookmark(any(Bookmark.class));
  }
}