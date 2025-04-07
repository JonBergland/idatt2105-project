import type { User, UserLoginDTO, UserRegistrationDTO, AddItemRequest } from "@/models/user";
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

  async postItem(request: AddItemRequest) {
    const response = await axiosInstance.post('/user/item', request);
    console.log(response);
  }
}

const userService = new UserService();
export default userService;
