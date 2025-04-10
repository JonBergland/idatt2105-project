package edu.ntnu.idatt2105.backend.mapper;

import edu.ntnu.idatt2105.backend.dto.item.AddItemRequest;
import edu.ntnu.idatt2105.backend.dto.item.EditItemRequest;
import edu.ntnu.idatt2105.backend.dto.item.GetBookmarkedItemsResponse;
import edu.ntnu.idatt2105.backend.dto.item.GetStoreItemResponse;
import edu.ntnu.idatt2105.backend.dto.item.ItemResponse;
import edu.ntnu.idatt2105.backend.model.Item;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * A Mapper for mapping between Item and DTOs.
 */
@Mapper
public interface ItemMapper {
  ItemMapper INSTANCE = Mappers.getMapper(ItemMapper.class);

  /**
   * maps item model to itemResponse dto.
   *
   * @param item the item to map
   * @return the mapped dto
   */
  @Mapping(source = "category", target = "category")
  @Mapping(source = "itemID", target = "itemID")
  @Mapping(source = "seller", target = "seller")
  @Mapping(source = "name", target = "name")
  @Mapping(source = "published", target = "published")
  @Mapping(source = "price", target = "price")
  @Mapping(source = "description", target = "description")
  @Mapping(source = "state", target = "state")
  ItemResponse itemToItemResponse(Item item);

  /**
   * maps item array to itemResponse array.
   *
   * @param items the item array
   * @return the itemResponse array
   */
  ItemResponse[] itemsToItemResponses(Item[] items);

  /**
   * maps addItemRequest dto to item model.
   *
   * @param addItemRequest the dto
   * @return the item model
   */
  @Mapping(source = "category", target = "category")
  @Mapping(source = "name", target = "name")
  @Mapping(source = "price", target = "price")
  @Mapping(source = "description", target = "description")
  Item addItemRequestToItem(AddItemRequest addItemRequest);

  /**
   * maps editItemRequest dto to item model.
   *
   * @param editItemRequest the dto
   * @return the item model
   */
  @Mapping(source = "category", target = "category")
  @Mapping(source = "name", target = "name")
  @Mapping(source = "price", target = "price")
  @Mapping(source = "description", target = "description")
  @Mapping(source = "itemID", target = "itemID")
  Item editItemRequestToItem(EditItemRequest editItemRequest);

  /**
   * maos item model to getStoreItemResponse dto.
   *
   * @param item the item model
   * @return the dto
   */
  @Mapping(source = "category", target = "category")
  @Mapping(source = "itemID", target = "itemID")
  @Mapping(source = "seller", target = "seller")
  @Mapping(source = "name", target = "name")
  @Mapping(source = "published", target = "published")
  @Mapping(source = "price", target = "price")
  @Mapping(source = "description", target = "description")
  @Mapping(source = "state", target = "state")
  @Mapping(source = "bookmark", target = "bookmark")
  GetStoreItemResponse itemToGetStoreItemResponse(Item item);

  /**
   * maps item to getBookmarkedItemsResponse.
   *
   * @param item the item to map
   * @return the mapped dto
   */
  @Mapping(source = "category", target = "category")
  @Mapping(source = "itemID", target = "itemID")
  @Mapping(source = "seller", target = "seller")
  @Mapping(source = "name", target = "name")
  @Mapping(source = "published", target = "published")
  @Mapping(source = "price", target = "price")
  @Mapping(source = "description", target = "description")
  @Mapping(source = "state", target = "state")
  GetBookmarkedItemsResponse itemToGetBookmarkedItemsResponse(Item item);

  /**
   * maps item array to getBookmarkedItemsResponse array.
   *
   * @param items the items to map
   * @return the mapped dto
   */
  GetBookmarkedItemsResponse[] itemArrayToGetBookmarkedItemsResponseArray(Item[] items);
}
