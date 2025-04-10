package edu.ntnu.idatt2105.backend.repository;

import edu.ntnu.idatt2105.backend.model.Purchase;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * a repository for managing purchases.
 */
@Repository
@RequiredArgsConstructor
public class PurchaseRepository {

  final private JdbcTemplate jdbcTemplate;

  /**
   * add a purchase to the database.
   *
   * @param purchase the purchase
   */
  public void addPurchase(Purchase purchase) {
    jdbcTemplate.update(
        "INSERT INTO Purchase"
        + "(item_id, buyer_id, final_price)"
        + "VALUES (?, ?, ?)",
        purchase.getItemID(),
        purchase.getBuyerID(),
        purchase.getFinalPrice()
    );
  }
}
