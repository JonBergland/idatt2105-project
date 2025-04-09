package edu.ntnu.idatt2105.backend.purchase;

import edu.ntnu.idatt2105.backend.purchase.model.Purchase;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PurchaseRepository {

  final private JdbcTemplate jdbcTemplate;

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
