import { describe, it, expect, vi, afterEach } from 'vitest';
import bidService from "@/services/bid/bidService";
import axiosInstance from "@/services/axiosService";
import type { PlaceBid } from '@/models/bid/bid';
import type { UserBidItemsRequest, UserBidItemsResponse } from '@/models/bid/UserBidItems';
import type { BidOnItemByUserRequest, BidOnItemByUserResponse } from '@/models/bid/BidOnItemByUser';
import type { UsersWithBidOnUserItemRequest, UsersWithBidOnUserItemResponse } from '@/models/bid/UsersWithBidOnUserItem';
import type { BidsFromUsersOnUserItemRequest, BidsFromUsersOnUserItemResponse } from '@/models/bid/BidsFromUsersOnUserItem';

vi.mock("@/services/axiosService");

describe("BidService", () => {
  afterEach(() => {
    vi.clearAllMocks();
  });

  describe("postUserBid", () => {
    it("should place a bid successfully", async () => {
      const mockBid: PlaceBid = {
        itemID: 1001,
        askingPrice: 2500
      };

      (axiosInstance.post as vi.Mock).mockResolvedValueOnce({});

      const result = await bidService.postUserBid(mockBid);

      expect(axiosInstance.post).toHaveBeenCalledWith('/user/item/bid/place', mockBid);
      expect(result).toBe(1);
    });

    it("should return 0 if placing a bid fails", async () => {
      const mockBid: PlaceBid = {
        itemID: 1001,
        askingPrice: 2500
      };

      (axiosInstance.post as vi.Mock).mockRejectedValueOnce(new Error("Failed to place bid"));

      const result = await bidService.postUserBid(mockBid);

      expect(axiosInstance.post).toHaveBeenCalledWith('/user/item/bid/place', mockBid);
      expect(result).toBe(0);
    });
  });

  describe("getItemsWithUserBids", () => {
    it("should retrieve items with user bids successfully", async () => {
      const mockRequest: UserBidItemsRequest = {
        SegmentOffset: [0, 20]
      };

      const mockResponse: UserBidItemsResponse[] = [
        {
          itemID: 1001,
          userID: 31,
          name: "Vintage Sofa",
          category: "Furniture",
          description: "Beautiful vintage sofa",
          published: "2023-04-01",
          price: 2500,
          state: "AVAILABLE"
        }
      ];

      (axiosInstance.post as vi.Mock).mockResolvedValueOnce({ data: mockResponse });

      const result = await bidService.getItemsWithUserBids(mockRequest);

      expect(axiosInstance.post).toHaveBeenCalledWith('/user/item/bids', mockRequest);
      expect(result).toEqual(mockResponse);
    });

    it("should return null if retrieving items with user bids fails", async () => {
      const mockRequest: UserBidItemsRequest = {
        SegmentOffset: [0, 20]
      };

      (axiosInstance.post as vi.Mock).mockRejectedValueOnce(new Error("Failed to retrieve items"));

      const result = await bidService.getItemsWithUserBids(mockRequest);

      expect(axiosInstance.post).toHaveBeenCalledWith('/user/item/bids', mockRequest);
      expect(result).toBeNull();
    });
  });

  describe("getUserBidsOnItem", () => {
    it("should retrieve user bids on an item successfully", async () => {
      const mockRequest: BidOnItemByUserRequest = {
        itemID: 1001,
        userID: 31,
        segmentOffset: [0, 10]
      };

      const mockResponse: BidOnItemByUserResponse[] = [
        {
          bidID: 1,
          itemID: 1001,
          askingPrice: 2500,
          status: "PENDING",
          published: "2023-04-10"
        }
      ];

      (axiosInstance.post as vi.Mock).mockResolvedValueOnce({ data: mockResponse });

      const result = await bidService.getUserBidsOnItem(mockRequest);

      expect(axiosInstance.post).toHaveBeenCalledWith('/user/item', mockRequest);
      expect(result).toEqual(mockResponse);
    });

    it("should return null if retrieving user bids on an item fails", async () => {
      const mockRequest: BidOnItemByUserRequest = {
        itemID: 1001,
        userID: 31,
        segmentOffset: [0, 10]
      };

      (axiosInstance.post as vi.Mock).mockRejectedValueOnce(new Error("Failed to retrieve bids"));

      const result = await bidService.getUserBidsOnItem(mockRequest);

      expect(axiosInstance.post).toHaveBeenCalledWith('/user/item', mockRequest);
      expect(result).toBeNull();
    });
  });

  describe("getUsersWithBidOnUserItem", () => {
    it("should retrieve users with bid on user item successfully", async () => {
      const mockRequest: UsersWithBidOnUserItemRequest = {
        SegmentOffset: [0, 20]
      };

      const mockResponse: UsersWithBidOnUserItemResponse[] = [
        {
          userID: 102,
          itemID: 1001,
          name: "Erik",
          surname: "Andersen",
          email: "erik.andersen@example.com"
        }
      ];

      (axiosInstance.post as vi.Mock).mockResolvedValueOnce({ data: mockResponse });

      const result = await bidService.getUsersWithBidOnUserItem(mockRequest);

      expect(axiosInstance.post).toHaveBeenCalledWith('/user/item/bid/users', mockRequest);
      expect(result).toEqual(mockResponse);
    });

    it("should return null if retrieving users with bid fails", async () => {
      const mockRequest: UsersWithBidOnUserItemRequest = {
        SegmentOffset: [0, 20]
      };

      (axiosInstance.post as vi.Mock).mockRejectedValueOnce(new Error("Failed to retrieve users"));

      const result = await bidService.getUsersWithBidOnUserItem(mockRequest);

      expect(axiosInstance.post).toHaveBeenCalledWith('/user/item/bid/users', mockRequest);
      expect(result).toBeNull();
    });
  });

  describe("getBidsFromUsersOnUserItem", () => {
    it("should retrieve bids from users on user item successfully", async () => {
      const mockRequest: BidsFromUsersOnUserItemRequest = {
        itemID: 1001,
        userID: 31,
        segmentOffset: [0, 10]
      };

      const mockResponse: BidsFromUsersOnUserItemResponse[] = [
        {
          bidID: 2,
          itemID: 1001,
          userID: 102,
          askingPrice: 2200,
          status: "PENDING",
          published: "2023-04-11"
        }
      ];

      (axiosInstance.post as vi.Mock).mockResolvedValueOnce({ data: mockResponse });

      const result = await bidService.getBidsFromUsersOnUserItem(mockRequest);

      expect(axiosInstance.post).toHaveBeenCalledWith('/user/item/bid/', mockRequest);
      expect(result).toEqual(mockResponse);
    });

    it("should return null if retrieving bids from users fails", async () => {
      const mockRequest: BidsFromUsersOnUserItemRequest = {
        itemID: 1001,
        userID: 31,
        segmentOffset: [0, 10]
      };

      (axiosInstance.post as vi.Mock).mockRejectedValueOnce(new Error("Failed to retrieve bids"));

      const result = await bidService.getBidsFromUsersOnUserItem(mockRequest);

      expect(axiosInstance.post).toHaveBeenCalledWith('/user/item/bid/', mockRequest);
      expect(result).toBeNull();
    });
  });
});
