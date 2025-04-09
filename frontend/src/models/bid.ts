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

export interface BidOnItemByUserRequest {
  itemID: number;
  userID: number;
  segmentOffset: number[];
}

export interface BidOnItemByUserResponse {
  bidID: number;
  itemID: number;
  askingPrice: number;
  status: string;
  published: string;
}

export interface UserBidItemsRequest {
  SegmentOffset: number[];
}

export interface UserBidItemsResponse {
  itemID: number;
  userID: number;
  itemName: string;
  seller: string;
}

export interface UsersWithBidOnUserItemRequest {
  SegmentOffset: number[];
}

export interface UsersWithBidOnUserItemResponse {
  itemID: number;
  userID: number;
  itemName: string;
  buyer: string;
}

export interface BidsFromUsersOnUserItemRequest {
  itemID: number;
  userID: number;
  segmentOffset: number[];
}

export interface BidsFromUsersOnUserItemResponse {
  bidID: number;
  itemID: number;
  askingPrice: number;
  status: string;
  published: string;
}
