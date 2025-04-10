package edu.ntnu.idatt2105.backend.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

/**
 * a DTO for updating the user's info.
 */
@Getter
@Setter
@Schema(description = "Data transfer object for updating user info")
public class UpdateUserInfoRequest {
  @Positive
  @Schema(description = "new phone number", example = "479 640 521")
  int phoneNumber;
  @Positive
  @Schema(description = "new country code", example = "47")
  int countryCode;
  @NotBlank
  @Schema(description = "new user name", example = "Stian")
  String name;
  @NotBlank
  @Schema(description = "new user surname", example = "Stiansen")
  String surname;
  @Schema(description = "new location latitude", example = "63.415547")
  double latitude;
  @Schema(description = "new location longitude", example = "10.404780")
  double longitude;
  @Schema(description = "new city", example = "Trondheim")
  String city;
  @Schema(description = "new postal code", example = "7034")
  int postalCode;
  @Schema(description = "new street address", example = "Sem Sælands vei 10")
  String address;
}
