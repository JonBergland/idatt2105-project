package edu.ntnu.idatt2105.backend.purchase;

import edu.ntnu.idatt2105.backend.purchase.model.Purchase;
import edu.ntnu.idatt2105.backend.user.UserMapper;
import edu.ntnu.idatt2105.backend.user.dto.BuyItemRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * A Mapper for mapping between Purchase and DTOs.
 */
@Mapper
public interface PurchaseMapper {
  PurchaseMapper INSTANCE = Mappers.getMapper(PurchaseMapper.class);

  /**
   * maps buyItemRequest dto to purchase model.
   *
   * @param buyItemRequest the dto
   * @return the mapped purchase
   */
  @Mapping(source = "itemID", target = "itemID")
  Purchase buyItemRequestToPurchase(BuyItemRequest buyItemRequest);
}
