import { describe, it, expect, vi, afterEach } from 'vitest'
import userService from "@/services/user/userService";
import axiosInstance from "@/services/axiosService";
import type { User, UserLoginDTO, UserRegistrationDTO } from "@/models/user";

vi.mock("@/services/axiosService");

describe("UserService", () => {
  afterEach(() => {
    vi.clearAllMocks(); // Clear mocks after each test
  });

  describe("getUserInfo", () => {
    it("should fetch user info successfully", async () => {
      const mockUser: User = {
        email: "john.doe@example.com",
        name: "John",
        surname: "Doe",
        countryCode: 47,
        phoneNumber: 12345678,
        role: "ROLE_USER",
      };
      (axiosInstance.get as vi.Mock).mockResolvedValueOnce({ data: mockUser });

      const result = await userService.getUserInfo();

      expect(axiosInstance.get).toHaveBeenCalledWith("/user/info");
      expect(result).toEqual(mockUser);
    });

    it("should throw an error if fetching user info fails", async () => {
      (axiosInstance.get as vi.Mock).mockRejectedValueOnce(new Error("Failed to fetch user info"));

      expect(await userService.getUserInfo()).toEqual(null);
    });
  });

  describe("registerUser", () => {
    it("should register a user successfully", async () => {
      const mockUser: UserRegistrationDTO = {
        email: "jane.doe@example.com",
        password: "password123",
        name: "Jane",
        surname: "Doe",
        countryCode: 47,
        phoneNumber: 23456789,
      };
      (axiosInstance.post as vi.Mock).mockResolvedValueOnce({ data: mockUser });

      const result = await userService.registerUser(mockUser);

      expect(axiosInstance.post).toHaveBeenCalledWith("/token/signup", mockUser);
      expect(result).toEqual(mockUser);
    });

    it("should throw an error if registration fails", async () => {
      const mockUser: UserRegistrationDTO = {
        email: "jane.doe@example.com",
        password: "password123",
        name: "Jane",
        surname: "Doe",
        countryCode: 47,
        phoneNumber: 23456789,
      };

      (axiosInstance.post as vi.Mock).mockRejectedValueOnce(new Error("Registration failed"));

      await expect(userService.registerUser(mockUser)).rejects.toThrow("Registration failed");
    });
  });

  describe("loginUser", () => {
    it("should log in a user successfully", async () => {
      const mockUser: UserLoginDTO = { email: "jane.doe@example.com", password: "password123" };
      (axiosInstance.post as vi.Mock).mockResolvedValueOnce({ data: mockUser });

      const result = await userService.loginUser(mockUser);

      expect(axiosInstance.post).toHaveBeenCalledWith("/token/signin", mockUser);
      expect(result).toEqual(mockUser);
    });

    it("should throw an error if login fails", async () => {
      const mockUser: UserLoginDTO = { email: "jane.doe@example.com", password: "password123" };
      (axiosInstance.post as vi.Mock).mockRejectedValueOnce(new Error("Login failed"));

      await expect(userService.loginUser(mockUser)).rejects.toThrow("Login failed");
    });
  });
});
