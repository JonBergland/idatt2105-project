package edu.ntnu.idatt2105.backend.bid;

import edu.ntnu.idatt2105.backend.bid.model.Bid;
import edu.ntnu.idatt2105.backend.user.dto.GetBidItemResponse;
import edu.ntnu.idatt2105.backend.user.dto.GetBidsResponse;
import edu.ntnu.idatt2105.backend.user.dto.PlaceBidRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BidMapper {
  BidMapper INSTANCE = Mappers.getMapper(BidMapper.class);

  @Mapping(source = "itemID", target = "itemID")
  @Mapping(source = "askingPrice", target = "askingPrice")
  Bid placeBidReqeustToBid(PlaceBidRequest placeBidRequest);

  @Mapping(source = "itemID", target = "itemID")
  @Mapping(source = "userID", target = "userID")
  GetBidItemResponse bidToGetBidItemResponse(Bid bid);

  GetBidItemResponse[] bidArrayToGetBidItemResponseArray(Bid[] bid);

  @Mapping(source = "itemID", target = "itemID")
  @Mapping(source = "askingPrice", target = "askingPrice")
  @Mapping(source = "status", target = "status")
  @Mapping(source = "published", target = "published")
  GetBidsResponse bidToGetBidsResponse(Bid bid);

  GetBidsResponse[] bidArrayToGetBidsResponseArray(Bid[] bid);

}
