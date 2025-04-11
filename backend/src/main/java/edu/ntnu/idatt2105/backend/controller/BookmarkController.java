package edu.ntnu.idatt2105.backend.controller;

import edu.ntnu.idatt2105.backend.dto.bookmark.ToggleBookmarkRequest;
import edu.ntnu.idatt2105.backend.service.BookmarkService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/**
 * a controller for endpoints related to bookmarks.
 */
@RestController
@RequestMapping(value = "/api/")
@CrossOrigin
@Tag(name = "Bookmark controller", description = "Bookmark management endpoints")
@RequiredArgsConstructor
public class BookmarkController {

  private final Logger logger = LoggerFactory.getLogger(BookmarkController.class);

  private final BookmarkService bookmarkService;

  /**
   * endpoint for toggling bookmark on an item.
   *
   * @param toggleBookmarkRequest the item to toggle
   */
  @PostMapping("/user/item/bookmark")
  @Operation(
      summary = "Toggle bookmark", description = "Toggle bookmark on item for the logged in user")
  public void bookmarkItem(@RequestBody @Valid ToggleBookmarkRequest toggleBookmarkRequest) {
    logger.info("toggling bookmark request");
    try {
      bookmarkService.toggleBookmark(toggleBookmarkRequest);
    } catch (DataAccessException e) {
      logger.warn("could not toggle bookmark: {}", e.getMessage());
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
  }
}
