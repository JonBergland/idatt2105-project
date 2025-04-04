package edu.ntnu.idatt2105.backend.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * A model representing a user.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
  private int userID;
  private String email;
  private int phoneNumber;
  private int countryCode;
  private String name;
  private String surname;
  private String password;
  private String role;
  private double latitude;
  private double longitude;
  private String city;
  private int postalCode;
  private String address;
}
