package edu.ntnu.idatt2105.backend.bid;

import edu.ntnu.idatt2105.backend.bid.model.Bid;
import edu.ntnu.idatt2105.backend.user.dto.AnswerBidRequest;
import edu.ntnu.idatt2105.backend.user.dto.GetBidsOnItemByUserRequest;
import edu.ntnu.idatt2105.backend.user.dto.GetBidsOnItemByUserResponse;
import edu.ntnu.idatt2105.backend.user.dto.GetYourBidItemsResponse;
import edu.ntnu.idatt2105.backend.user.dto.GetYourItemBidsResponse;
import edu.ntnu.idatt2105.backend.user.dto.GetYourUniqueBidsResponse;
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
  GetYourUniqueBidsResponse bidToGetBidItemResponse(Bid bid);

  GetYourUniqueBidsResponse[] bidArrayToGetBidItemResponseArray(Bid[] bid);

  @Mapping(source = "bidID", target = "bidID")
  @Mapping(source = "itemID", target = "itemID")
  @Mapping(source = "askingPrice", target = "askingPrice")
  @Mapping(source = "status", target = "status")
  @Mapping(source = "published", target = "published")
  GetYourItemBidsResponse bidToGetBidsResponse(Bid bid);

  GetYourItemBidsResponse[] bidArrayToGetBidsResponseArray(Bid[] bid);

  @Mapping(source = "bidID", target = "bidID")
  @Mapping(source = "itemID", target = "itemID")
  @Mapping(source = "userID", target = "userID")
  @Mapping(source = "status", target = "status")
  Bid answerBidRequestToBid(AnswerBidRequest answerBidRequest);

  @Mapping(source = "itemID", target = "itemID")
  @Mapping(source = "userID", target = "userID")
  @Mapping(source = "itemName", target = "itemName")
  @Mapping(source = "email", target = "buyer")
  GetYourBidItemsResponse bidToGetYourBidItemsResponse(Bid bid);

  GetYourBidItemsResponse[] bidArrayToGetYourBidItemsResponseArray(Bid[] bid);

  @Mapping(source = "itemID", target = "itemID")
  @Mapping(source = "userID", target = "userID")
  Bid getBidsOnItemByUserRequestToBid(GetBidsOnItemByUserRequest getBidsOnItemByUserRequest);

  @Mapping(source = "bidID", target = "bidID")
  @Mapping(source = "itemID", target = "itemID")
  @Mapping(source = "published", target = "published")
  @Mapping(source = "status", target = "status")
  @Mapping(source = "askingPrice", target = "askingPrice")
  GetBidsOnItemByUserResponse bidToGetBidsOnItemByUserResponse(Bid bid);

  GetBidsOnItemByUserResponse[] bidArrayToGetBidsOnItemByUserResponseArray(Bid[] bid);
}
