package edu.ntnu.idatt2105.backend.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Data transfer object for requesting items user has bid on")
public class GetYourUniqueBidsRequest {
  @Schema(description = "pagination, page number then page size", example = "[0, 20]")
  int[] SegmentOffset;
}