package edu.ntnu.idatt2105.backend.user.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * a DTO for responding to get user info request.
 */
@Getter
@Setter
public class GetUserInfoResponse {
  int userID;
  String email;
  int phoneNumber;
  int countryCode;
  String name;
  String surname;
  String role;
  double latitude;
  double longitude;
  String city;
  int postalCode;
  String address;
}
