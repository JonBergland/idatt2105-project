package edu.ntnu.idatt2105.backend.mapper;

import edu.ntnu.idatt2105.backend.dto.bid.AnswerBidRequest;
import edu.ntnu.idatt2105.backend.dto.bid.GetBidsOnItemByUserRequest;
import edu.ntnu.idatt2105.backend.dto.bid.GetBidsOnItemByUserResponse;
import edu.ntnu.idatt2105.backend.dto.bid.GetYourBidItemsResponse;
import edu.ntnu.idatt2105.backend.dto.bid.GetYourItemBidsResponse;
import edu.ntnu.idatt2105.backend.dto.bid.GetYourUniqueBidsResponse;
import edu.ntnu.idatt2105.backend.dto.bid.PlaceBidRequest;
import edu.ntnu.idatt2105.backend.model.Bid;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * A mapper for mapping between bid model and dto.
 */
@Mapper
public interface BidMapper {
  BidMapper INSTANCE = Mappers.getMapper(BidMapper.class);

  /**
   * Maps PlaceBidRequest dto to bid model.
   *
   * @param placeBidRequest the dto
   * @return the bid model
   */
  @Mapping(source = "itemID", target = "itemID")
  @Mapping(source = "askingPrice", target = "askingPrice")
  Bid placeBidReqeustToBid(PlaceBidRequest placeBidRequest);

  /**
   * Maps bid model to GetYourUniqueBidsResponse dto.
   *
   * @param bid the bid model
   * @return the GetYourUniqueBidsResponse dto
   */
  @Mapping(source = "email", target = "seller")
  @Mapping(source = "itemName", target = "itemName")
  @Mapping(source = "itemID", target = "itemID")
  @Mapping(source = "userID", target = "userID")
  GetYourUniqueBidsResponse bidToGetBidItemResponse(Bid bid);

  /**
   * Maps bid model array to GetYourUniqueBidsResponse dto array.
   *
   * @param bid the bid model array
   * @return the GetYourUniqueBidsResponse dto array
   */
  GetYourUniqueBidsResponse[] bidArrayToGetBidItemResponseArray(Bid[] bid);

  /**
   * Maps bid model to GetYourItemBidsResponse dto.
   *
   * @param bid the bid model
   * @return the GetYourItemBidsResponse dto
   */
  @Mapping(source = "bidID", target = "bidID")
  @Mapping(source = "itemID", target = "itemID")
  @Mapping(source = "askingPrice", target = "askingPrice")
  @Mapping(source = "status", target = "status")
  @Mapping(source = "published", target = "published")
  GetYourItemBidsResponse bidToGetBidsResponse(Bid bid);

  /**
   * Maps bid model array to GetYourItemBidsResponse dto array.
   *
   * @param bid the bid model array
   * @return the GetYourItemBidsResponse dto array
   */
  GetYourItemBidsResponse[] bidArrayToGetBidsResponseArray(Bid[] bid);

  /**
   * Maps AnswerBidRequest dto to bid model.
   *
   * @param answerBidRequest the dto
   * @return the bid model
   */
  @Mapping(source = "bidID", target = "bidID")
  @Mapping(source = "status", target = "status")
  Bid answerBidRequestToBid(AnswerBidRequest answerBidRequest);

  /**
   * Maps bid model to GetYourBidItemsResponse dto.
   *
   * @param bid the bid model
   * @return the GetYourBidItemsResponse dto
   */
  @Mapping(source = "itemID", target = "itemID")
  @Mapping(source = "userID", target = "userID")
  @Mapping(source = "itemName", target = "itemName")
  @Mapping(source = "email", target = "buyer")
  GetYourBidItemsResponse bidToGetYourBidItemsResponse(Bid bid);

  /**
   * Maps bid model array to GetYourBidItemsResponse dto array.
   *
   * @param bid the bid model array
   * @return the GetYourBidItemsResponse dto array
   */
  GetYourBidItemsResponse[] bidArrayToGetYourBidItemsResponseArray(Bid[] bid);

  /**
   * Maps GetBidsOnItemByUserRequest dto to bid model.
   *
   * @param getBidsOnItemByUserRequest the dto
   * @return the bid model
   */
  @Mapping(source = "itemID", target = "itemID")
  @Mapping(source = "userID", target = "userID")
  Bid getBidsOnItemByUserRequestToBid(GetBidsOnItemByUserRequest getBidsOnItemByUserRequest);

  /**
   * Maps bid model to GetBidsOnItemByUserResponse dto.
   *
   * @param bid the bid model
   * @return the GetBidsOnItemByUserResponse dto
   */
  @Mapping(source = "bidID", target = "bidID")
  @Mapping(source = "itemID", target = "itemID")
  @Mapping(source = "published", target = "published")
  @Mapping(source = "status", target = "status")
  @Mapping(source = "askingPrice", target = "askingPrice")
  GetBidsOnItemByUserResponse bidToGetBidsOnItemByUserResponse(Bid bid);

  /**
   * Maps bid model array to GetBidsOnItemByUserResponse dto array.
   *
   * @param bid the bid model array
   * @return the GetBidsOnItemByUserResponse dto array
   */
  GetBidsOnItemByUserResponse[] bidArrayToGetBidsOnItemByUserResponseArray(Bid[] bid);
}
