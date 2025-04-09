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
