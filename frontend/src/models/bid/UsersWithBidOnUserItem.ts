export interface UsersWithBidOnUserItemRequest {
  segmentOffset: number[];
}

export interface UsersWithBidOnUserItemResponse {
  itemID: number;
  userID: number;
  itemName: string;
  buyer: string;
}
