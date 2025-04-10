package edu.ntnu.idatt2105.backend.security.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Data transfer object for user sign in")
public class SigninRequest {
  @Email
  @NotBlank
  @Schema(description = "Email of the user", example = "Joe@example.org")
  private String email;
  @NotBlank
  @Schema(description = "Password of the user", example = "Password123")
  private String password;
}
