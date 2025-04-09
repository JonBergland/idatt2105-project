/**
 * Represents a user in the application
 */
export interface User {
  userID: number;
  email: string;
  name: string;
  surname: string;
  countryCode: number;
  phoneNumber: number;
  role: string;
  latitude: number;
  longitude: number;
  city: string;
  postalCode: number;
  address: string;
}

/**
 * Data transfer object for user registration
 */
export interface UserRegistrationDTO {
  email: string;
  password: string;
  name: string;
  surname: string;
  countryCode: number;
  phoneNumber: number;
}

/**
 * Data transfer object for user login
 */
export interface UserLoginDTO {
  email: string;
  password: string;
}

export interface AddItemRequest {
  name: string;
  description: string;
  price: number;
  category: string;
}

export interface UpdateItemRequest {
  itemID: number;
  name: string;
  description: string;
  price: number;
  category: string;
}

export interface ToggleBookmarkRequest {
  itemID: number;
}

export interface GetBookmarkedItemsRequest{
  segmentOffset: [number, number] | null;
}
