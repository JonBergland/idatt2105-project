package edu.ntnu.idatt2105.backend.security.dto;

import static org.junit.jupiter.api.Assertions.*;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class SigninResponseTest {

  private static Validator validator;

  @BeforeAll
  static void setUp() {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
  }

  @Test
  void validSigninResponse_ShouldPassValidation() {
    SigninResponse response = new SigninResponse("validToken123");
    Set<ConstraintViolation<SigninResponse>> violations = validator.validate(response);
    assertTrue(violations.isEmpty(), "Valid response should have no validation errors");
  }

  @Test
  void invalidSigninResponse_ShouldFailValidation_WhenTokenIsBlank() {
    SigninResponse response = new SigninResponse("");
    Set<ConstraintViolation<SigninResponse>> violations = validator.validate(response);
    assertFalse(violations.isEmpty(), "Blank token should trigger validation error");
  }

  @Test
  void invalidSigninResponse_ShouldFailValidation_WhenTokenIsNull() {
    SigninResponse response = new SigninResponse(null);
    Set<ConstraintViolation<SigninResponse>> violations = validator.validate(response);
    assertFalse(violations.isEmpty(), "Null token should trigger validation error");
  }
}