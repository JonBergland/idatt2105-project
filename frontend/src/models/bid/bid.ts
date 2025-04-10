import type { UserBidItemsResponse } from "@/models/bid/UserBidItems";
import type { BidOnItemByUserResponse } from "./BidOnItemByUser
import type { UsersWithBidOnUserItemResponse } from "./UsersWithBidOnUserItem";
import type { BidsFromUsersOnUserItemResponse } from "./BidsFromUsersOnUserItem";

export interface Bid {
  bidID: number;
  itemID: number;
  userID: number;
  askingPrice: number;
  status: string;
  published: string;
  itemName: string;
  email: string;
}

export interface PlaceBid {
  itemID: number;
  askingPrice: number;
}

interface ItemWithBids {
  item: UserBidItemsResponse;
  bids: BidOnItemByUserResponse[];
}

interface UserWithBids {
  user: UsersWithBidOnUserItemResponse;
  bids: BidsFromUsersOnUserItemResponse[];
}

