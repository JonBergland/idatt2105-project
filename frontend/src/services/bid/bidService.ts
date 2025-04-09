import type { BidOnItemByUserRequest, BidOnItemByUserResponse, BidsFromUsersOnUserItemRequest, BidsFromUsersOnUserItemResponse, PlaceBid, UserBidItemsRequest, UserBidItemsResponse, UsersWithBidOnUserItemRequest, UsersWithBidOnUserItemResponse } from "@/models/bid";
import axiosInstance from "@/services/axiosService";

class BidService {
   /**
   * Places a bid on an item for the user.
   *
   * @param bid - An object containing the bid details to be placed.
   * @returns A promise that resolves to a number:
   *          - `1` if the bid was successfully placed.
   *          - `0` if an error occurred while placing the bid.
   */
   async postUserBid(bid: PlaceBid): Promise<number> {
    try {
      await axiosInstance.post('/user/item/bid/place', bid);
      return 1;
    } catch (error) {
      console.error('Error placing bid on item:', error);
      return 0;
    }
  }

  /**
   * Retrieves a list of items that the user has placed bids on.
   *
   * @param request  An object containing the request parameters for fetching user bid items.
   * @returns        A promise that resolves to an array of (@link UserBidItemsResponse) objects or
   *                 empty response in case of an error
   *
   */
  async getItemsWithUserBids(request: UserBidItemsRequest): Promise<UserBidItemsResponse[] | null> {
    try {
      const response = await axiosInstance.post<UserBidItemsResponse[]>('/user/item/bids', request);
      return response.data
    } catch (error) {
      console.error('Error getting items with user bids: ', error)
      return null;
    }
  }

  /**
   * Retrieves bids placed by a user on an item.
   *
   * @param request The request object containing the item details.
   * @returns       A promise that resolves to a (@link BidsOnItemByUserResponse) or an empty response in case of an error.
   */
  async getUserBidsOnItem(request: BidOnItemByUserRequest): Promise<BidOnItemByUserResponse[] | null> {
    try {
      const response = await axiosInstance.post<BidOnItemByUserResponse[]>('/user/item', request);
      return response.data;
    } catch (error) {
      console.error('Error getting bids on item by User: ', error);
      return null;
    }
  }

  /**
   * Retrieves users with bid on an user item.
   *
   * @param request The request object containing the segment offset.
   * @returns       A promise that resolves to a (@link UsersWithBidOnUserItemResponse) or an empty response in case of an error.
   */
  async getUsersWithBidOnUserItem(request: UsersWithBidOnUserItemRequest): Promise<UsersWithBidOnUserItemResponse[] | null> {
    try {
      const response = await axiosInstance.post<UsersWithBidOnUserItemResponse[]>('/user/item/bid/users', request);
      return response.data;
    } catch (error) {
      console.error('Error getting Users with bids user items: ', error);
      return null;
    }
  }

  /**
   * Retrieves bids from other users on the logged in user's item.
   *
   * @param request The request object containing the segment offset.
   * @returns       A promise that resolves to a (@link BidsFromUsersOnUserItemResponse) or an empty response in case of an error.
   */
  async getBidsFromUsersOnUserItem(request: BidsFromUsersOnUserItemRequest): Promise<BidsFromUsersOnUserItemResponse[] | null> {
    try {
      const response = await axiosInstance.post<BidsFromUsersOnUserItemResponse[]>('/user/item/bid/users', request);
      return response.data;
    } catch (error) {
      console.error('Error getting bids from users on user items: ', error);
      return null;
    }
  }
}

const bidService = new BidService();
export default bidService;
