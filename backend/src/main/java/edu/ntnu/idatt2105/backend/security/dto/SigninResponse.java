package edu.ntnu.idatt2105.backend.security.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * A DTO for responding to sign in request.
 */
@Getter
@AllArgsConstructor
public class SigninResponse {
  @NotBlank
  private String token;
}
