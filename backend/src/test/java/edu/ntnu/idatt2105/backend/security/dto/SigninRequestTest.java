package edu.ntnu.idatt2105.backend.security.dto;

import static org.junit.jupiter.api.Assertions.*;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class SigninRequestTest {

  private static Validator validator;

  @BeforeAll
  static void setUp() {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
  }

  @Test
  void validSigninRequest_ShouldPassValidation() {
    SigninRequest request = new SigninRequest("test@example.com", "password123");
    Set<ConstraintViolation<SigninRequest>> violations = validator.validate(request);
    assertTrue(violations.isEmpty(), "Valid request should have no validation errors");
  }

  @Test
  void invalidSigninRequest_ShouldFailValidation_WhenEmailIsInvalid() {
    SigninRequest request = new SigninRequest("invalid-email", "password123");
    Set<ConstraintViolation<SigninRequest>> violations = validator.validate(request);
    assertFalse(violations.isEmpty(), "Invalid email should trigger validation error");
  }

  @Test
  void invalidSigninRequest_ShouldFailValidation_WhenPasswordIsBlank() {
    SigninRequest request = new SigninRequest("test@example.com", "");
    Set<ConstraintViolation<SigninRequest>> violations = validator.validate(request);
    assertFalse(violations.isEmpty(), "Blank password should trigger validation error");
  }

  @Test
  void invalidSigninRequest_ShouldFailValidation_WhenEmailIsBlank() {
    SigninRequest request = new SigninRequest("", "password123");
    Set<ConstraintViolation<SigninRequest>> violations = validator.validate(request);
    assertFalse(violations.isEmpty(), "Blank email should trigger validation error");
  }
}
