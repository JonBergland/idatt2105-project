package edu.ntnu.idatt2105.backend.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * a DTO for responding to get user info request.
 */
@Getter
@Setter
@Schema(description = "Data transfer object for responding with user info")
public class GetUserInfoResponse {
  @Schema(description = "user id", example = "20")
  int userID;
  @Schema(description = "user email", example = "user@site.no")
  String email;
  @Schema(description = "user phone number", example = "123 45 678")
  int phoneNumber;
  @Schema(description = "user country code", example = "47")
  int countryCode;
  @Schema(description = "user name", example = "Joe")
  String name;
  @Schema(description = "user surname", example = "Mama")
  String surname;
  @Schema(description = "user role", example = "ROLE_USER")
  String role;
  @Schema(description = "location latitude", example = "63.415547")
  double latitude;
  @Schema(description = "location longitude", example = "10.404780")
  double longitude;
  @Schema(description = "user city", example = "Trondheim")
  String city;
  @Schema(description = "user postal code", example = "7034")
  int postalCode;
  @Schema(description = "user street address", example = "Sem Sælands vei 10")
  String address;
}
