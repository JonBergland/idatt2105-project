package edu.ntnu.idatt2105.backend.user.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * a DTO for updating the user's info.
 */
@Getter
@Setter
public class UpdateUserInfoRequest {
  int phoneNumber;
  int countryCode;
  String name;
  String surname;
  double latitude;
  double longitude;
  String city;
  int postalCode;
  String address;
}
