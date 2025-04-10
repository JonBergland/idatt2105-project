export interface UsersWithBidOnUserItemRequest {
  SegmentOffset: number[];
}

export interface UsersWithBidOnUserItemResponse {
  itemID: number;
  userID: number;
  itemName: string;
  buyer: string;
}
