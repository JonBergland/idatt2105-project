package edu.ntnu.idatt2105.backend.bid;

import edu.ntnu.idatt2105.backend.bid.model.Bid;
import edu.ntnu.idatt2105.backend.user.dto.GetYourItemBidsRequest;
import java.nio.file.AccessDeniedException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * a repository for managing bids in the database.
 */
@Repository
@RequiredArgsConstructor
public class BidRepository {

  private final JdbcTemplate jdbcTemplate;

  /**
   * place a bid on an item.
   *
   * @param bid the bid to place
   */
  public void placeBid(Bid bid) {
    jdbcTemplate.update(
          "INSERT INTO Bids (user_id, item_id, asking_price) VALUES (?, ?, ?)",
          bid.getUserID(),
          bid.getItemID(),
          bid.getAskingPrice());
  }

  /**
   * get bids you have placed on distinct items.
   *
   * @param userID your user id
   * @return the bids
   */
  public Bid[] getYourUniqueBids(int userID) {
    List<Bid> bidList = jdbcTemplate.query(
        "SELECT DISTINCT user_id AS userID, item_id AS itemID FROM Bids "
        + "WHERE user_id = ?",
        new Object[]{userID},
        new BeanPropertyRowMapper<>(Bid.class));
    return bidList.toArray(new Bid[0]);
  }

  /**
   * get bids you have placed on an item paginated.
   *
   * @param userID your user id
   * @param getYourItemBidsRequest the request info
   * @return the bids
   */
  public Bid[] getItemBids(int userID, GetYourItemBidsRequest getYourItemBidsRequest) {
    List<Bid> bidList = jdbcTemplate.query(
        "SELECT *, item_id AS itemID, id AS bidID FROM Bids WHERE user_id = ? AND item_id = ? "
            + "ORDER BY published DESC "
            + "LIMIT ? OFFSET ?",
        new Object[]{
            userID,
            getYourItemBidsRequest.getItemID(),
            getYourItemBidsRequest.getSegmentOffset()[1],
            getYourItemBidsRequest.getSegmentOffset()[0] * getYourItemBidsRequest.getSegmentOffset()[1]},
        new BeanPropertyRowMapper<>(Bid.class));
    return bidList.toArray(new Bid[0]);
  }

  /**
   * set the bid status of an item you own.
   *
   * @param bid the bid
   * @param userID your user id
   * @throws AccessDeniedException when you don't own the item
   */
  public void setBidStatus(Bid bid, int userID) throws AccessDeniedException {
    if (ownsBidItem(userID, bid.getBidID())) {
      jdbcTemplate.update(
          "UPDATE Bids "
              + "SET status = ? "
              + "WHERE id = ?",
          bid.getStatus(),
          bid.getBidID());
    } else {
      throw new AccessDeniedException("the user don't own this item");
    }
  }

  /**
   * get users who have bid on your items.
   *
   * @param userID your user id
   * @return the bids
   */
  public Bid[] getUniqueBids(int userID) {
    List<Bid> bidList = jdbcTemplate.query(
        "SELECT DISTINCT Bids.item_id AS itemID, Bids.user_id AS userID, Item.name AS itemName, b.email FROM `Bids` "
        + "JOIN Item ON Bids.item_id = Item.id "
        + "JOIN User o ON Item.user_id = o.id "
        + "JOIN User b ON Bids.user_id = b.id "
        + "WHERE o.id = ?",
        new Object[]{userID},
        new BeanPropertyRowMapper<>(Bid.class));
    return bidList.toArray(new Bid[0]);
  }

  /**
   * check if a user owns the item that is bid on.
   *
   * @param userID the user id
   * @param bidID the bid id
   * @return boolean from check
   */
  private boolean ownsBidItem(int userID, int bidID) {
    return jdbcTemplate.queryForObject(
        "SELECT IF(EXISTS( "
            + "SELECT 1 "
            + "FROM Item "
            + "JOIN Bids ON Item.id = Bids.item_id "
            + "WHERE Item.user_id = ? AND Bids.id = ? "
            + "), 'true', 'false') AS finnes",
        new Object[]{userID, bidID},
        Boolean.class);
  }
}
