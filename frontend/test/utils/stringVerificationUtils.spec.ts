import { describe, it, expect } from "vitest";
import {
  verifyStringForLetters,
  verifyStringForNumbers,
  verifyStringForEmail,
  verifyStringNotEmpty
} from "@/utils/stringVerificationUtils";

describe("String Verification Utils", () => {

  describe("verifyStringForLetters", () => {
    it("should return true for a string with only letters", () => {
      expect(verifyStringForLetters("Hello")).toBe(true);
      expect(verifyStringForLetters("ÆØÅæøå")).toBe(true);
    });

    it("should return false for strings containing numbers or special characters", () => {
      expect(verifyStringForLetters("Hello123")).toBe(false);
      expect(verifyStringForLetters("Hello!")).toBe(false);
      expect(verifyStringForLetters("123")).toBe(false);
      expect(verifyStringForLetters("")).toBe(false);
      expect(verifyStringForLetters(" ")).toBe(false);
    });
  });

  describe("verifyStringForNumbers", () => {
    it("should return true for a string with only numbers", () => {
      expect(verifyStringForNumbers("12345")).toBe(true);
      expect(verifyStringForNumbers("000")).toBe(true);
    });

    it("should return false for strings containing letters or special characters", () => {
      expect(verifyStringForNumbers("123abc")).toBe(false);
      expect(verifyStringForNumbers("12.34")).toBe(false);
      expect(verifyStringForNumbers(" ")).toBe(false);
      expect(verifyStringForNumbers("")).toBe(false);
    });
  });

  describe("verifyStringForEmail", () => {
    it("should return true for a valid email", () => {
      expect(verifyStringForEmail("test@example.com")).toBe(true);
      expect(verifyStringForEmail("user.name@domain.co")).toBe(true);
    });

    it("should return false for invalid email formats", () => {
      expect(verifyStringForEmail("plainaddress")).toBe(false);
      expect(verifyStringForEmail("missing@dot")).toBe(false);
      expect(verifyStringForEmail("@missingusername.com")).toBe(false);
      expect(verifyStringForEmail("missing@domain.")).toBe(false);
      expect(verifyStringForEmail(" ")).toBe(false);
      expect(verifyStringForEmail("")).toBe(false);
    });
  });

  describe("verifyStringNotEmpty", () => {
    it("should return true for a non-empty string", () => {
      expect(verifyStringNotEmpty("Hello")).toBe(true);
      expect(verifyStringNotEmpty("123")).toBe(true);
      expect(verifyStringNotEmpty("   test   ")).toBe(true); // Trimming should not make it empty
    });

    it("should return false for an empty or whitespace string", () => {
      expect(verifyStringNotEmpty("")).toBe(false);
      expect(verifyStringNotEmpty("    ")).toBe(false);
    });
  });

});
