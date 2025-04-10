package edu.ntnu.idatt2105.backend.dto.token;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
@Schema(description = "Data transfer object for signing up")
public class SignupRequest {
  @Email
  @NotBlank
  @Schema(description = "unique email, used for signing in", example = "John@site.com")
  private String email;
  @NotBlank
  @Schema(description = "password used for signing in", example = "Password123")
  private String password;
  @Schema(description = "first name of user", example = "John")
  @NotBlank
  private String name;
  @Schema(description = "last name of user", example = "Smith")
  @NotBlank
  private String surname;
  @NotNull
  @Min(value = 1, message = "Phone number must be greater than 0")
  @Schema(description = "user phone number", example = "12345678")
  private int phoneNumber;
  @NotNull
  @Min(value = 1, message = "Country code must be greater than 0")
  @Schema(description = "phone number country code", example = "47")
  private int countryCode;
}
