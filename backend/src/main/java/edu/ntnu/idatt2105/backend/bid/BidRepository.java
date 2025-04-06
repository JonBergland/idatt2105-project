package edu.ntnu.idatt2105.backend.bid;

import edu.ntnu.idatt2105.backend.bid.model.Bid;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BidRepository {

  private final JdbcTemplate jdbcTemplate;

  public void placeBid(Bid bid) {
    jdbcTemplate.update(
          "INSERT INTO Bids (user_id, item_id, asking_price) VALUES (?, ?, ?)",
          bid.getUserID(),
          bid.getItemID(),
          bid.getAskingPrice());
  }
}
