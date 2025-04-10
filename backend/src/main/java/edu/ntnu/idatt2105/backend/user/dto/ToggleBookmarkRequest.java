package edu.ntnu.idatt2105.backend.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Data transfer object for toggling bookmark on item")
public class ToggleBookmarkRequest {
  @Schema(description = "item id of item", example = "10")
  int itemID;
}
