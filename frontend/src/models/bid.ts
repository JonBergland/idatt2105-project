export interface BidsOnItemByUserResponse {
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

export interface BidsOnItemByUserRequest {
  itemID: number;
  userID: number;
  segmentOffset: number[];
}

export interface BidsOnItemByUserResponse {
  bidID: number;
  itemID: number;
  askingPrice: number;
  status: string;
  published: string;
}
