package edu.ntnu.idatt2105.backend.security.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * A DTO for user sign in request.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SigninRequest {
  @Email
  @NotBlank
  private String email;
  @NotBlank
  private String password;
}
