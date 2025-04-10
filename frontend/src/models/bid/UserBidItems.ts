export interface UserBidItemsRequest {
  SegmentOffset: number[];
}

export interface UserBidItemsResponse {
  itemID: number;
  userID: number;
  itemName: string;
  seller: string;
}
