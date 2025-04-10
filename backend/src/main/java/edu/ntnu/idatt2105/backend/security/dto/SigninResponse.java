package edu.ntnu.idatt2105.backend.security.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * A DTO for responding to sign in request.
 */
@Getter
@AllArgsConstructor
@Schema(description = "For deletion")
public class SigninResponse {
  @NotBlank
  private String token;
}
