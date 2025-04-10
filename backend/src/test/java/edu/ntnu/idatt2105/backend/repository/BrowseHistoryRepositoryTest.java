package edu.ntnu.idatt2105.backend.repository;

import edu.ntnu.idatt2105.backend.model.BrowseHistory;
import edu.ntnu.idatt2105.backend.repository.BrowseHistoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class BrowseHistoryRepositoryTest {

  @Mock
  private JdbcTemplate jdbcTemplate;

  @InjectMocks
  private BrowseHistoryRepository browseHistoryRepository;

  private BrowseHistory mockBrowseHistory;

  @BeforeEach
  void setUp() {
    mockBrowseHistory = new BrowseHistory();
    mockBrowseHistory.setUserID(1);
    mockBrowseHistory.setItemID(1);
  }

  @Test
  void testAddUpdateBrowseHistory() {
    browseHistoryRepository.addUpdateBrowseHistory(mockBrowseHistory);
    verify(jdbcTemplate, times(1)).update(
        eq("INSERT INTO Browse_history (user_id, item_id) VALUES (?, ?) ON DUPLICATE KEY UPDATE published = CURRENT_TIMESTAMP"),
        eq(mockBrowseHistory.getUserID()), eq(mockBrowseHistory.getItemID())
    );
  }
}