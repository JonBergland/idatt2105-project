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
