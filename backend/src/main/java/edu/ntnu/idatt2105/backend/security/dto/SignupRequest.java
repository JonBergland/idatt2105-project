package edu.ntnu.idatt2105.backend.security.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * A DTO for user signup requests.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SignupRequest {
  @Email
  @NotBlank
  private String email;
  @NotBlank
  private String password;
  @NotBlank
  private String name;
  @NotBlank
  private String surname;
  private int phoneNumber;
  private int countryCode;
}
