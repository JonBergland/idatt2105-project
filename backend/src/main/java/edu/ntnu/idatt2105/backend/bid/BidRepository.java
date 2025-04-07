package edu.ntnu.idatt2105.backend.bid;

import edu.ntnu.idatt2105.backend.bid.model.Bid;
import edu.ntnu.idatt2105.backend.item.model.Item;
import edu.ntnu.idatt2105.backend.user.dto.GetBidsRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
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

  public Bid[] getUniqueBids(int userID) {
    List<Bid> bidList = jdbcTemplate.query(
        "SELECT DISTINCT user_id AS userID, item_id AS itemID FROM Bids "
        + "WHERE user_id = ?",
        new Object[]{userID},
        new BeanPropertyRowMapper<>(Bid.class));
    return bidList.toArray(new Bid[0]);
  }

  public Bid[] getItemBids(int userID, GetBidsRequest getBidsRequest) {
    List<Bid> bidList = jdbcTemplate.query(
        "SELECT * FROM Bids WHERE user_id = ? AND item_id = ? "
            + "ORDER BY published DESC "
            + "LIMIT ? OFFSET ?",
        new Object[]{
            userID,
            getBidsRequest.getItemID(),
            getBidsRequest.getSegmentOffset()[1],
            getBidsRequest.getSegmentOffset()[0] * getBidsRequest.getSegmentOffset()[1]},
        new BeanPropertyRowMapper<>(Bid.class));
    return bidList.toArray(new Bid[0]);
  }
}
