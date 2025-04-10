package edu.ntnu.idatt2105.backend.repository;

import edu.ntnu.idatt2105.backend.model.Bookmark;
import edu.ntnu.idatt2105.backend.repository.BookmarkRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class BookmarkRepositoryTest {

  @Mock
  private JdbcTemplate jdbcTemplate;

  @InjectMocks
  private BookmarkRepository bookmarkRepository;

  private Bookmark mockBookmark;

  @BeforeEach
  void setUp() {
    mockBookmark = new Bookmark(1, 1);
  }

  @Test
  void testAddBookmark() {
    bookmarkRepository.addBookmark(mockBookmark);
    verify(jdbcTemplate, times(1)).update(
        eq("INSERT INTO Bookmark (user_id, item_id) VALUES (?, ?)"),
        eq(mockBookmark.getUserID()), eq(mockBookmark.getItemID())
    );
  }

  @Test
  void testDeleteBookmark() {
    bookmarkRepository.deleteBookmark(mockBookmark);
    verify(jdbcTemplate, times(1)).update(
        eq("DELETE FROM Bookmark WHERE user_id = ? AND item_id = ?"),
        eq(mockBookmark.getUserID()), eq(mockBookmark.getItemID())
    );
  }

  @Test
  void testCheckBookmark() {
    when(jdbcTemplate.queryForObject(
        eq("SELECT IF(EXISTS("
            + "  SELECT 1"
            + "  FROM Bookmark"
            + "  WHERE user_id = ? AND item_id = ?"
            + "), 'true', 'false') AS finnes"),
        any(Object[].class),
        eq(Boolean.class)
    )).thenReturn(true);

    boolean exists = bookmarkRepository.checkBookmark(mockBookmark);
    assertTrue(exists);
  }
}