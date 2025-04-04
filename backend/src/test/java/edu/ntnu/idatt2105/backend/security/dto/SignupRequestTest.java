package edu.ntnu.idatt2105.backend.security.dto;

import static org.junit.jupiter.api.Assertions.*;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class SignupRequestTest {

  private static Validator validator;

  @BeforeAll
  static void setUp() {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
  }

  @Test
  void validSignupRequest_ShouldPassValidation() {
    SignupRequest request = new SignupRequest("test@example.com", "password123", "John", "Doe", 12345678, 2);
    Set<ConstraintViolation<SignupRequest>> violations = validator.validate(request);
    assertTrue(violations.isEmpty(), "Valid request should have no validation errors");
  }

  @Test
  void invalidSignupRequest_ShouldFailValidation_WhenEmailIsInvalid() {
    SignupRequest request = new SignupRequest("invalid-email", "password123", "John", "Doe", 12345678, 4);
    Set<ConstraintViolation<SignupRequest>> violations = validator.validate(request);
    assertFalse(violations.isEmpty(), "Invalid email should trigger validation error");
  }

  @Test
  void invalidSignupRequest_ShouldFailValidation_WhenFieldsAreBlank() {
    SignupRequest request = new SignupRequest("", "", "", "", 0,0);
    Set<ConstraintViolation<SignupRequest>> violations = validator.validate(request);
    assertFalse(violations.isEmpty(), "Blank fields should trigger validation errors");
  }

  @Test
  void invalidSignupRequest_ShouldFailValidation_WhenEmailIsNull() {
    SignupRequest request = new SignupRequest(null, "password123", "John", "Doe", 12345678, 35);
    Set<ConstraintViolation<SignupRequest>> violations = validator.validate(request);
    assertFalse(violations.isEmpty(), "Null email should trigger validation error");
  }

  @Test
  void invalidSignupRequest_ShouldFailValidation_WhenPhoneNumberIsNull() {
    SignupRequest request = new SignupRequest("test@example.com", "password123", "John", "Doe", 0, 47);
    Set<ConstraintViolation<SignupRequest>> violations = validator.validate(request);
    assertFalse(violations.isEmpty(), "Blank phone number should trigger validation error");
  }

  @Test
  void invalidSignupRequest_ShouldFailValidation_WhenLandCodeIsNull() {
    SignupRequest request = new SignupRequest("test@example.com", "password123", "John", "Doe", 12345678, 0);
    Set<ConstraintViolation<SignupRequest>> violations = validator.validate(request);
    assertFalse(violations.isEmpty(), "Blank phone number should trigger validation error");
  }
}