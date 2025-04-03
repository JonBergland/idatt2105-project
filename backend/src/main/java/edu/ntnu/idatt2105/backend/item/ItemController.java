package edu.ntnu.idatt2105.backend.item;

import edu.ntnu.idatt2105.backend.item.dto.ItemResponse;
import edu.ntnu.idatt2105.backend.item.dto.ItemsRequest;
import edu.ntnu.idatt2105.backend.item.dto.ItemsResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/store/item")
@CrossOrigin
@RequiredArgsConstructor
public class ItemController {

  private final Logger logger = LoggerFactory.getLogger(ItemController.class);

  private final ItemService itemService;

  @PostMapping("/filter")
  public ItemsResponse getItems(@RequestBody ItemsRequest itemsRequest) {
    ItemResponse[] itemResponses = ItemMapper.INSTANCE.itemsToItemResponses(itemService.getItems(itemsRequest));
    return new ItemsResponse(itemResponses);
  }
}
