import type { UserLoginDTO, UserRegistrationDTO } from "@/models/user";
import axiosInstance from "@/services/axiosService";


class UserService {

  getUserInfo() {
    // TODO
  }

  async registerUser(user: UserRegistrationDTO) {
    return await axiosInstance.post<UserRegistrationDTO>('/token/signup', user)
  }

  async loginUser(user: UserLoginDTO) {
    return await axiosInstance.post<UserLoginDTO>('/token/signin', user)
  }
}

const userService = new UserService();
export default userService;
