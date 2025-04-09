import type { ItemsResponseDTO, ItemRequestDTO, ItemResponseDTO } from "@/models/item";
import type { User, UserLoginDTO, UserRegistrationDTO, AddItemRequest, UpdateItemRequest, ToggleBookmarkRequest, GetBookmarkedItemsRequest } from "@/models/user";
import axiosInstance from "@/services/axiosService";
import axios from 'axios';

/**
 * A service class for handling user-related operations such as registration and login.
 */
class UserService {

  /**
   * Retrieves information about the currently authenticated user.
   * @returns A promise that resolves with the user's information or null if not authenticated.
   */
  async getUserInfo(): Promise<User | null> {
    try {
      const response = await axiosInstance.get<User>('/user/info');
      return response.data;
    } catch (error) {
      if (axios.isAxiosError(error) && (error.response?.status === 401 || error.response?.status === 403)) {
        console.log("User not authenticated");
        return null;
      }
      console.error("Unexpected error in getUserInfo:", error);
      return null;
    }
  }

  /**
   * Updates an existing user in the system with new information.
   * @param user  An {@link User} containing the user details.
   */
  async updateUserInfo(user: User): Promise<void> {
    try {
      await axiosInstance.post('/user/info', user);
    } catch (error) {
      console.error("Unexpected error in updateUserInfo:", error);
    }
  }

  async getUserItems(): Promise<ItemsResponseDTO | null> {
    try {
      const response = await axiosInstance.get<ItemsResponseDTO>('/user/item');
      return response.data;
    } catch (error) {
      console.error("Unexpected error in getUserItem: ", error)
      return null;
    }
  }

  /**
   * Registers a new user in the system.
   * @param user  An {@link UserRegistrationDTO} containing the user's registration details.
   * @returns     A promise containing the status of the signup
   */
  async registerUser(user: UserRegistrationDTO): Promise<boolean> {
    const response = await axiosInstance.post<boolean>('/token/signup', user);
    return response.data;
  }

  /**
   * Logs in a user with the provided credentials.
   * @param user  An {@link UserLoginDTO} containing the user's login credentials.
   * @returns     A promise containing the status of the login
   */
  async loginUser(user: UserLoginDTO): Promise<boolean> {
    const response = await axiosInstance.post<boolean>('/token/signin', user)
    return response.data
  }

  /**
   * Logs out the currently authenticated user.
   *
   * @returns {Promise<void>} A promise that resolves when the logout request is completed.
   */
  async logoutUser(): Promise<void> {
    const resp = await axiosInstance.post('/token/logout')
    console.log(resp);
  }

  /**
   * Sends a POST request to create a new user item.
   *
   * @param request - The payload containing the details of the item to be added.
   * @returns A promise that resolves to the response of the POST request.
   * * @throws Will throw an error if the request fails, logging the error to the console.
   */
  async postItem(request: AddItemRequest): Promise<void>{
    try {
      await axiosInstance.post('/user/item', request);
    } catch (error) {
      console.error('Error creating item:', error);
      throw error;
    }
  }

  /**
   * Updates an item by sending a POST request to the server with the provided request data.
   *
   * @param request - The data required to update the item, adhering to the `UpdateItemRequest` interface.
   * @returns A promise that resolves when the item is successfully updated.
   * @throws Will throw an error if the request fails, logging the error to the console.
   */
  async updateItem(request: UpdateItemRequest): Promise<void> {
    try {
      await axiosInstance.post('/user/item/edit', request);
    } catch (error) {
      console.error('Error updating item:', error);
      throw error;
    }
  }

  /**
   * Retrieves the details of a user item by sending a request to the server.
   *
   * @param request - The data transfer object containing the item request details.
   * @returns A promise that resolves to an `ItemResponseDTO` containing the item details.
   * @throws Will throw an error if the request fails.
   */
  async getUserItemDetails(request: ItemRequestDTO): Promise<ItemResponseDTO> {
    try {
      const response = await axiosInstance.post<ItemResponseDTO>('/user/item/store', request);
      return response.data;
    } catch (error) {
      console.error('Error getting item details:', error);
      throw error;
    }
  }

  /**
   * Sends a POST request to the `/user/item/bookmark` endpoint with the provided
   * request payload to either add or remove a bookmark for a specific item.
   *
   * @param request - The payload containing the necessary data to toggle the bookmark.
   * @returns A promise that resolves to the response data from the server.
   * @throws Will throw an error if the request fails.
   */
  async toggleBookmark(request: ToggleBookmarkRequest) {
    try {
      const response = await axiosInstance.post<ToggleBookmarkRequest>('/user/item/bookmark', request);
      return response.data;
    } catch (error) {
      console.error('Error toggeling bookmark:', error);
      throw error;
    }
  }

  /**
   * Retrieves a list of bookmarked items for a user.
   *
   * @param request - The request object containing the necessary parameters to fetch bookmarked items.
   * @returns A promise that resolves to an `ItemsResponseDTO` containing the bookmarked items,
   *          or `null` if an error occurs during the request.
   *
   * @throws Logs an error message to the console if the request fails.
   */
  async getBookmarkedItems(request: GetBookmarkedItemsRequest): Promise<ItemsResponseDTO | null> {
    try {
      const response = await axiosInstance.post<ItemsResponseDTO>('/user/item/bookmark', request);
      return response.data;
    } catch (error) {
      console.error('Error toggeling bookmark:', error)
      return null;
    }
  }
}

const userService = new UserService();
export default userService;
