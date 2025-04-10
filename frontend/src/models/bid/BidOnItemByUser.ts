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
