package edu.ntnu.idatt2105.backend.purchase;

import edu.ntnu.idatt2105.backend.purchase.model.Purchase;
import edu.ntnu.idatt2105.backend.user.UserMapper;
import edu.ntnu.idatt2105.backend.user.dto.BuyItemRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PurchaseMapper {
  PurchaseMapper INSTANCE = Mappers.getMapper(PurchaseMapper.class);

  @Mapping(source = "itemID", target = "itemID")
  Purchase buyItemRequestToPurchase(BuyItemRequest buyItemRequest);
}
