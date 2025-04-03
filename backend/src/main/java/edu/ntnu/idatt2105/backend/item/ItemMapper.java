package edu.ntnu.idatt2105.backend.item;

import edu.ntnu.idatt2105.backend.item.dto.ItemResponse;
import edu.ntnu.idatt2105.backend.item.model.Item;
import org.mapstruct.Mapper;
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
  ItemResponse itemToItemResponse(Item item);

  /**
   * maps item array to itemResponse array.
   *
   * @param items the item array
   * @return the itemResponse array
   */
  ItemResponse[] itemsToItemResponses(Item[] items);
}
