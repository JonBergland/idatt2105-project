import type { User, UserLoginDTO, UserRegistrationDTO } from "@/models/user";
import axiosInstance from "@/services/axiosService";


/**
 * A service class for handling user-related operations such as registration and login.
 */
class UserService {

  /**
   * Retrieves information about the currently authenticated user.
   * @returns A promise that resolves with the user's information.
   */
  async getUserInfo(): Promise<User> {
    const response = await axiosInstance.get<User>('/user/info');
    return response.data;
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
}

const userService = new UserService();
export default userService;
