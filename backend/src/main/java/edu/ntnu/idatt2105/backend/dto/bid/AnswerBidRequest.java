package edu.ntnu.idatt2105.backend.dto.bid;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

/**
 * a DTO for answering a bid.
 */
@Getter
@Setter
@Schema(description = "Data transfer object for answering bids")
public class AnswerBidRequest {
  @Positive
  @Schema(description = "id of bid to answer", example = "3")
  int bidID;
  @Pattern(regexp = "^([10])$")
  @Schema(description = "bid status, accept = 1, decline = 0, pending = NULL", example = "1")
  String status;
}
