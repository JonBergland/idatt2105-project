import { defineStore } from 'pinia';
import bidService from "@/services/bid/bidService";
import type { ItemWithBids, PlaceBid, UserWithBids } from "@/models/bid/bid";
import type { UserBidItemsRequest, UserBidItemsResponse } from '@/models/bid/UserBidItems';
import type { BidOnItemByUserRequest, BidOnItemByUserResponse } from '@/models/bid/BidOnItemByUser';
import type { BidsFromUsersOnUserItemRequest, BidsFromUsersOnUserItemResponse } from '@/models/bid/BidsFromUsersOnUserItem';
import type { UsersWithBidOnUserItemRequest, UsersWithBidOnUserItemResponse } from '@/models/bid/UsersWithBidOnUserItem';

export const useBidStore = defineStore('bidStore', {
  state: () => ({
    userBids: [] as ItemWithBids[],
    otherBids: [] as UserWithBids[]
  }),

  actions: {
    /**
     * Places a bid on an item using the provided bid details.
     *
     * @param bid - An object containing the details of the bid to be placed.
     * @returns A promise that resolves to a number indicating the result of the bid operation.
     *          Returns 0 if an error occurs during the process.
     *          Returns 1 if the process was successful.
     */
    async giveBidOnItem(bid: PlaceBid): Promise<number> {
      try {
        const response = bidService.postUserBid(bid);
        return response;
      } catch (error) {
        console.log("Error when giving bid on item: ", error);
        return 0;
      }
    },

    /**
     * Retrieves all items the user has bid on along with the specific bids
     *
     * @returns A promise that resolves to an array of objects containing item and bid information
     */
    async updateUserBidsOnItems() {
      try {
        const itemBidRequest: UserBidItemsRequest = {
          SegmentOffset: [0, 20]
        };

        const items = await this.getItemsWithUserBids(itemBidRequest);

        if (!items.length) {
          return [];
        }

        this.userBids = [] as ItemWithBids[]

        // For each item found, get the bids the user has made on them
        for (const item of items) {
          if (!item.itemID || !item.userID) continue;

          const bidRequest: BidOnItemByUserRequest = {
            itemID: item.itemID,
            userID: item.userID,
            segmentOffset: [0, 10]
          };

          const userBids = await this.getUsersBidOnItem(bidRequest);

          this.userBids.push({
            item: item,
            bids: userBids
          });
        }
      } catch (error) {
        console.error('Error retrieving user items and bids:', error);
        return [];
      }
    },

    /**
     * Retrieves all users who have bid on the current user's items along with their specific bids
     *
     * @returns A promise that resolves to an array of objects containing user and bid information
     */
    async updateBidsOnUserItems() {
      try {
        // Similar structure as above, but for other users' bids
        const usersRequest: UsersWithBidOnUserItemRequest = {
          SegmentOffset: [0, 20]
        };

        const users = await this.getUsersWithBidOnUserItem(usersRequest);

        if (!users.length) {
          return [];
        }

        this.otherBids = [] as UserWithBids[]

        for (const user of users) {
          if (!user.itemID || !user.userID) continue;

          const bidsRequest: BidsFromUsersOnUserItemRequest = {
            itemID: user.itemID,
            userID: user.userID,
            segmentOffset: [0, 10]
          };

          const userBids = await this.getBidsFromUsersOnUserItem(bidsRequest);

          this.otherBids.push({
            user: user,
            bids: userBids
          });
        }
      } catch (error) {
        console.error('Error retrieving users and their bids:', error);
        return [];
      }
    },

    /**
     * Retrieves a user's bid on a specific item
     *
     * @param request - An object containing the request parameters for fetching bids
     * @returns A promise that resolves to a BidsOnItemByUserResponse object with bid information
     */
    async getUsersBidOnItem(request: BidOnItemByUserRequest): Promise<BidOnItemByUserResponse[]> {
      const defaultResponse = [{
        bidID: 0,
        itemID: 0,
        askingPrice: 0,
        status: '',
        published: ''
      }] as BidOnItemByUserResponse[];

      try {
        if (!request || !request.itemID || !request.userID) {
          console.error('Invalid request parameters for getUsersBidOnItem');
          return defaultResponse;
        }

        const response = await bidService.getUserBidsOnItem(request);

        if (response === null) {
          return defaultResponse;
        }

        return response;
      } catch (error) {
        console.error('Error retrieving user bid on item:', error);

        return defaultResponse;
      }
    },

    /**
     * Retrieves items that the current user has placed bids on
     *
     * @param request - An object containing the request parameters for fetching bid items
     * @returns A promise that resolves to an array of UserBidItemsResponse objects
     */
    async getItemsWithUserBids(request: UserBidItemsRequest): Promise<UserBidItemsResponse[]> {
      const defaultResponse = [] as UserBidItemsResponse[];

      try {
        if (!request || !request.SegmentOffset) {
          console.error('Invalid request parameters for getItemsWithUserBids');
          return defaultResponse;
        }

        const response = await bidService.getItemsWithUserBids(request);

        if (response === null) {
          return defaultResponse;
        }

        return response;
      } catch (error) {
        console.error('Error retrieving items with user bids:', error);
        return defaultResponse;
      }
    },

    /**
     * Retrieves users who have placed bids on the current user's items
     *
     * @param request - An object containing the segment offset for pagination
     * @returns A promise that resolves to an array of UsersWithBidOnUserItemResponse objects
     */
    async getUsersWithBidOnUserItem(request: UsersWithBidOnUserItemRequest): Promise<UsersWithBidOnUserItemResponse[]> {
      const defaultResponse = [] as UsersWithBidOnUserItemResponse[];

      try {
        if (!request || !request.SegmentOffset) {
          console.error('Invalid request parameters for getUsersWithBidOnUserItem');
          return defaultResponse;
        }

        const response = await bidService.getUsersWithBidOnUserItem(request);

        if (response === null) {
          return defaultResponse;
        }

        return response;
      } catch (error) {
        console.error('Error retrieving users with bids on user item:', error);
        return defaultResponse;
      }
    },

    /**
     * Retrieves bids from other users on the current user's item
     *
     * @param request - An object containing the item ID, user ID, and segment offset
     * @returns A promise that resolves to an array of BidsFromUsersOnUserItemResponse objects
     */
    async getBidsFromUsersOnUserItem(request: BidsFromUsersOnUserItemRequest): Promise<BidsFromUsersOnUserItemResponse[]> {
      const defaultResponse = [] as BidsFromUsersOnUserItemResponse[];

      try {
        if (!request || !request.itemID || !request.userID || !request.segmentOffset) {
          console.error('Invalid request parameters for getBidsFromUsersOnUserItem');
          return defaultResponse;
        }

        const response = await bidService.getBidsFromUsersOnUserItem(request);

        if (response === null) {
          return defaultResponse;
        }

        return response;
      } catch (error) {
        console.error('Error retrieving bids from users on user item:', error);
        return defaultResponse;
      }
    }
  },
});
