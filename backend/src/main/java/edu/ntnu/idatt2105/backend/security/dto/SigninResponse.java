package edu.ntnu.idatt2105.backend.security.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SigninResponse {
  @NotBlank
  private String token;
}
