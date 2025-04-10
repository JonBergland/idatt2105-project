package edu.ntnu.idatt2105.backend.repository;

import edu.ntnu.idatt2105.backend.model.Bid;
import edu.ntnu.idatt2105.backend.dto.user.GetYourItemBidsRequest;
import java.nio.file.AccessDeniedException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * a repository for managing bids in the database.
 */
@Repository
@RequiredArgsConstructor
public class BidRepository {

  private final JdbcTemplate jdbcTemplate;

  /**
   * place a bid on an item and mark it reserved.
   *
   * @param bid the bid to place
   */
  @Transactional
  public void placeBid(Bid bid) {
    jdbcTemplate.update(
          "INSERT INTO Bids (user_id, item_id, asking_price) VALUES (?, ?, ?)",
          bid.getUserID(),
          bid.getItemID(),
          bid.getAskingPrice());
    jdbcTemplate.update(
        "UPDATE Item "
        + "SET state_id = 3 "
        + "WHERE id = ?",
        bid.getItemID()
    );
  }

  /**
   * get bids you have placed on distinct items.
   *
   * @param userID your user id
   * @return the bids
   */
  public Bid[] getYourUniqueBids(int userID, int[] segmentOffset) {
    List<Bid> bidList = jdbcTemplate.query(
        "SELECT DISTINCT Bids.user_id AS userID, Bids.item_id AS itemID, User.email, Item.name AS itemName FROM Bids "
        + "JOIN Item ON Bids.item_id = Item.id "
        + "JOIN User ON Item.user_id = User.id "
        + "WHERE Bids.user_id = ? "
        + "GROUP BY Bids.user_id, Bids.item_id, User.email, Item.name "
        + "ORDER BY MAX(Bids.published) DESC "
        + "LIMIT ? OFFSET ?",
        new Object[]{userID,
        segmentOffset[1],
        segmentOffset[0] * segmentOffset[1]},
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
  public Bid[] getUniqueBids(int userID, int[] segmentOffset) {
    List<Bid> bidList = jdbcTemplate.query(
        "SELECT DISTINCT Bids.item_id AS itemID, Bids.user_id AS userID, Item.name AS itemName, b.email, Bids.published FROM `Bids` "
        + "JOIN Item ON Bids.item_id = Item.id "
        + "JOIN User o ON Item.user_id = o.id "
        + "JOIN User b ON Bids.user_id = b.id "
        + "WHERE o.id = ? "
        + "ORDER BY Bids.published DESC "
        + "LIMIT ? OFFSET ?",
        new Object[]{
            userID,
            segmentOffset[1],
            segmentOffset[0] * segmentOffset[1]},
        new BeanPropertyRowMapper<>(Bid.class));
    return bidList.toArray(new Bid[0]);
  }

  /**
   * get bids by a user on an item.
   *
   * @param bid the bid
   * @param userID the user id of item owner
   * @param segmentOffset the segment offset
   * @return the bids
   */
  public Bid[] getBidsByUserOnItem(Bid bid, int userID, int[] segmentOffset) {
    List<Bid> bidList = jdbcTemplate.query(
        "SELECT Bids.id AS bidID, Bids.item_id AS itemID, Bids.asking_price, Bids.status, Bids.published FROM Bids "
            + "JOIN Item On Bids.item_id = Item.id "
            + "WHERE Bids.item_id = ? AND Bids.user_id = ? AND Item.user_id = ? "
            + "ORDER BY Bids.published DESC "
            + "LIMIT ? OFFSET ? ",
        new Object[]{
            bid.getItemID(),
            bid.getUserID(),
            userID,
            segmentOffset[1],
            segmentOffset[0] * segmentOffset[1]},
        new BeanPropertyRowMapper<>(Bid.class));
    return bidList.toArray(new Bid[0]);
  }

  /**
   * check if a bid is unanswered or accepted.
   *
   * @param itemID the item id
   * @return true if the bid is unanswered or accepted, false otherwise
   */
  public boolean checkIfUnansweredBid(int itemID) {
    return jdbcTemplate.queryForObject(
        "SELECT IF(EXISTS( "
            + "SELECT 1 "
            + "FROM Bids "
            + "WHERE Bids.item_id = ? AND (Bids.status IS NULL OR Bids.status = 1) "
            + "), 'true', 'false') AS finnes",
        new Object[]{itemID},
        Boolean.class);
  }

  /**
   * get the item id from a bid id.
   *
   * @param bidID the bid id
   * @return the item id
   */
  public int itemFromBid(int bidID) {
    return jdbcTemplate.queryForObject(
        "SELECT item_id FROM Bids "
        + "WHERE id = ?",
        new Object[]{bidID},
        Integer.class
    );
  }

  /**
   * get a bid from bid id.
   *
   * @param bidID the bid id
   * @return the bid
   */
  public Bid getBid(int bidID) {
    return jdbcTemplate.queryForObject(
        "SELECT Bids.*, Bids.id AS bidID, Bids.item_id AS itemID, Bids.user_id AS userID FROM Bids "
        + "WHERE id = ?",
        new Object[]{bidID},
        new BeanPropertyRowMapper<>(Bid.class)
    );
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
