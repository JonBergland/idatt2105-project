import { describe, it, expect, vi, afterEach, beforeEach } from 'vitest'
import userService from "@/services/user/userService";
import axiosInstance from "@/services/axiosService";
import type { User, UserLoginDTO, UserRegistrationDTO, AddItemRequest, UpdateItemRequest, ToggleBookmarkRequest, GetBookmarkedItemsRequest } from "@/models/user";
import type { ItemsResponseDTO, ItemRequestDTO, ItemResponseDTO } from "@/models/item";
import axios from 'axios';

interface AxiosErrorWithResponse extends Error {
  response?: {
    status: number;
  };
}

vi.mock("@/services/axiosService", () => ({
  default: {
    get: vi.fn(),
    post: vi.fn(),
    put: vi.fn(),
    delete: vi.fn()
  }
}));
vi.mock('axios');

describe("UserService", () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  afterEach(() => {
    vi.clearAllMocks();
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

    it("should return null if fetching user info fails with 401", async () => {
      const error = new Error("Unauthorized") as AxiosErrorWithResponse;
      error.response = { status: 401 };
      (axiosInstance.get as vi.Mocks).mockRejectedValueOnce(error);
      (axios.isAxiosError as vi.Mock).mockReturnValueOnce(true);

      const result = await userService.getUserInfo();

      expect(result).toBeNull();
    });

    it("should return null for other errors", async () => {
      (axiosInstance.get as vi.Mock).mockRejectedValueOnce(new Error("Failed to fetch user info"));
      (axios.isAxiosError as vi.Mock).mockReturnValueOnce(false);

      const result = await userService.getUserInfo();

      expect(result).toBeNull();
    });
  });

  describe("updateUserInfo", () => {
    it("should update user info successfully", async () => {
      const mockUser: User = {
        email: "john.doe@example.com",
        name: "John",
        surname: "Doe",
        countryCode: 47,
        phoneNumber: 12345678,
        role: "ROLE_USER",
      };
      (axiosInstance.post as vi.Mock).mockResolvedValueOnce({ data: {} });

      await userService.updateUserInfo(mockUser);

      expect(axiosInstance.post).toHaveBeenCalledWith("/user/info", mockUser);
    });

    it("should handle error when updating user info fails", async () => {
      const mockUser: User = {
        email: "john.doe@example.com",
        name: "John",
        surname: "Doe",
        countryCode: 47,
        phoneNumber: 12345678,
        role: "ROLE_USER",
      };
      const error = new Error("Failed to update user info");
      (axiosInstance.post as vi.Mock).mockRejectedValueOnce(error);
      const consoleSpy = vi.spyOn(console, 'error').mockImplementation(() => {});

      await userService.updateUserInfo(mockUser);

      expect(consoleSpy).toHaveBeenCalledWith("Unexpected error in updateUserInfo:", error);
      consoleSpy.mockRestore();
    });
  });

  describe("getUserItems", () => {
    it("should fetch user items successfully", async () => {
      const mockItems: ItemsResponseDTO = {
        items: [
          {
            itemID: 1,
            name: "Item 1",
            description: "Description 1",
            price: 100,
            category: "Category 1",
            seller: "john.doe@example.com",
            state: "active"
          }
        ]
      };
      (axiosInstance.get as vi.Mock).mockResolvedValueOnce({ data: mockItems });

      const result = await userService.getUserItems();

      expect(axiosInstance.get).toHaveBeenCalledWith("/user/item");
      expect(result).toEqual(mockItems);
    });

    it("should return null if fetching user items fails", async () => {
      const error = new Error("Failed to fetch user items");
      (axiosInstance.get as vi.Mock).mockRejectedValueOnce(error);
      const consoleSpy = vi.spyOn(console, 'error').mockImplementation(() => {});

      const result = await userService.getUserItems();

      expect(consoleSpy).toHaveBeenCalledWith("Unexpected error in getUserItem: ", error);
      expect(result).toBeNull();
      consoleSpy.mockRestore();
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
      (axiosInstance.post as vi.Mock).mockResolvedValueOnce({ data: true });

      const result = await userService.registerUser(mockUser);

      expect(axiosInstance.post).toHaveBeenCalledWith("/token/signup", mockUser);
      expect(result).toBe(true);
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
      (axiosInstance.post as vi.Mock).mockResolvedValueOnce({ data: true });

      const result = await userService.loginUser(mockUser);

      expect(axiosInstance.post).toHaveBeenCalledWith("/token/signin", mockUser);
      expect(result).toBe(true);
    });

    it("should throw an error if login fails", async () => {
      const mockUser: UserLoginDTO = { email: "jane.doe@example.com", password: "password123" };
      (axiosInstance.post as vi.Mock).mockRejectedValueOnce(new Error("Login failed"));

      await expect(userService.loginUser(mockUser)).rejects.toThrow("Login failed");
    });
  });

  describe("logoutUser", () => {
    it("should logout a user successfully", async () => {
      (axiosInstance.post as vi.Mock).mockResolvedValueOnce({ data: true });
      const consoleSpy = vi.spyOn(console, 'log').mockImplementation(() => {});

      await userService.logoutUser();

      expect(axiosInstance.post).toHaveBeenCalledWith("/token/logout");
      consoleSpy.mockRestore();
    });

    it("should throw an error if logout fails", async () => {
      (axiosInstance.post as vi.Mock).mockRejectedValueOnce(new Error("Logout failed"));

      await expect(userService.logoutUser()).rejects.toThrow("Logout failed");
    });
  });

  describe("postItem", () => {
    it("should post a new item successfully", async () => {
      const mockRequest: AddItemRequest = {
        name: "Test Item",
        description: "A test item description",
        price: 99.99,
        category: "Test Category"
      };
      (axiosInstance.post as vi.Mock).mockResolvedValueOnce({ data: {} });

      await userService.postItem(mockRequest);

      expect(axiosInstance.post).toHaveBeenCalledWith("/user/item", mockRequest);
    });

    it("should throw an error if posting an item fails", async () => {
      const mockRequest: AddItemRequest = {
        name: "Test Item",
        description: "A test item description",
        price: 99.99,
        category: "Test Category"
      };
      const error = new Error("Failed to post item");
      (axiosInstance.post as vi.Mock).mockRejectedValueOnce(error);
      const consoleSpy = vi.spyOn(console, 'error').mockImplementation(() => {});

      await expect(userService.postItem(mockRequest)).rejects.toThrow("Failed to post item");

      expect(consoleSpy).toHaveBeenCalledWith("Error creating item:", error);
      consoleSpy.mockRestore();
    });
  });

  describe("updateItem", () => {
    it("should update an item successfully", async () => {
      const mockRequest: UpdateItemRequest = {
        itemID: 1,
        name: "Updated Item",
        description: "An updated item description",
        price: 129.99,
        category: "Updated Category"
      };
      (axiosInstance.post as vi.Mock).mockResolvedValueOnce({ data: {} });

      await userService.updateItem(mockRequest);

      expect(axiosInstance.post).toHaveBeenCalledWith("/user/item/edit", mockRequest);
    });

    it("should throw an error if updating an item fails", async () => {
      const mockRequest: UpdateItemRequest = {
        itemID: 1,
        name: "Updated Item",
        description: "An updated item description",
        price: 129.99,
        category: "Updated Category"
      };
      const error = new Error("Failed to update item");
      (axiosInstance.post as vi.Mock).mockRejectedValueOnce(error);
      const consoleSpy = vi.spyOn(console, 'error').mockImplementation(() => {});

      await expect(userService.updateItem(mockRequest)).rejects.toThrow("Failed to update item");

      expect(consoleSpy).toHaveBeenCalledWith("Error updating item:", error);
      consoleSpy.mockRestore();
    });
  });

  describe("getUserItemDetails", () => {
    it("should get user item details successfully", async () => {
      const mockRequest: ItemRequestDTO = { itemID: 1 };
      const mockResponse: ItemResponseDTO = {
        itemID: 1,
        name: "Test Item",
        description: "A test item description",
        price: 99.99,
        category: "Test Category",
        seller: "john.doe@example.com",
        state: "active",
        sellerID: 1
      };
      (axiosInstance.post as vi.Mock).mockResolvedValueOnce({ data: mockResponse });

      const result = await userService.getUserItemDetails(mockRequest);

      expect(axiosInstance.post).toHaveBeenCalledWith("/user/item/store", mockRequest);
      expect(result).toEqual(mockResponse);
    });

    it("should throw an error if getting user item details fails", async () => {
      const mockRequest: ItemRequestDTO = { itemID: 1 };
      const error = new Error("Failed to get item details");
      (axiosInstance.post as vi.Mock).mockRejectedValueOnce(error);
      const consoleSpy = vi.spyOn(console, 'error').mockImplementation(() => {});

      await expect(userService.getUserItemDetails(mockRequest)).rejects.toThrow("Failed to get item details");

      expect(consoleSpy).toHaveBeenCalledWith("Error getting item details:", error);
      consoleSpy.mockRestore();
    });
  });

  describe("toggleBookmark", () => {
    it("should toggle bookmark successfully", async () => {
      const mockRequest: ToggleBookmarkRequest = {
        itemID: 1,
        bookmark: true
      };
      (axiosInstance.post as vi.Mock).mockResolvedValueOnce({ data: true });

      const result = await userService.toggleBookmark(mockRequest);

      expect(axiosInstance.post).toHaveBeenCalledWith("/user/item/bookmark", mockRequest);
      expect(result).toBe(true);
    });

    it("should throw an error if toggling bookmark fails", async () => {
      const mockRequest: ToggleBookmarkRequest = {
        itemID: 1,
        bookmark: true
      };
      const error = new Error("Failed to toggle bookmark");
      (axiosInstance.post as vi.Mock).mockRejectedValueOnce(error);
      const consoleSpy = vi.spyOn(console, 'error').mockImplementation(() => {});

      await expect(userService.toggleBookmark(mockRequest)).rejects.toThrow("Failed to toggle bookmark");

      expect(consoleSpy).toHaveBeenCalledWith("Error toggeling bookmark:", error);
      consoleSpy.mockRestore();
    });
  });

  describe("getBookmarkedItems", () => {
    it("should get bookmarked items successfully", async () => {
      const mockRequest: GetBookmarkedItemsRequest = {
        segmentOffset: [0, 10]
      };
      const mockResponse = [
        {
          itemID: 1,
          name: "Bookmarked Item 1",
          description: "A bookmarked item description",
          price: 99.99,
          category: "Test Category",
          seller: "john.doe@example.com",
          state: "active"
        }
      ];
      (axiosInstance.post as vi.Mock).mockResolvedValueOnce({ data: mockResponse });

      const result = await userService.getBookmarkedItems(mockRequest);

      expect(axiosInstance.post).toHaveBeenCalledWith("/user/item/bookmark/get", mockRequest);
      expect(result).toEqual(mockResponse);
    });

    it("should return empty array if getting bookmarked items fails", async () => {
      const mockRequest: GetBookmarkedItemsRequest = {
        segmentOffset: [0, 10]
      };
      const error = new Error("Failed to get bookmarked items");
      (axiosInstance.post as vi.Mock).mockRejectedValueOnce(error);
      const consoleSpy = vi.spyOn(console, 'error').mockImplementation(() => {});

      const result = await userService.getBookmarkedItems(mockRequest);

      expect(consoleSpy).toHaveBeenCalledWith("Error toggeling bookmark:", error);
      expect(result).toEqual([]);
      consoleSpy.mockRestore();
    });
  });
});
