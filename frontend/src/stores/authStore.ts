import type { User, UserLoginDTO, UserRegistrationDTO } from "@/models/user";
import { defineStore } from "pinia";
import userService from "@/services/user/userService"


/**
 * Store for authenticating the user with login and singup
 */
export const useAuthStore = defineStore('auth', {
  state: () => ({
    isAuth: false,
    userData: null as User | null
  }),

  actions: {

    async checkIfAuth() {
      try {
        this.userData = await userService.getUserInfo()
        console.log("User data from backend: ", this.userData)

        if (this.userData) {
          this.isAuth = true
          return true
        } else {
          throw new Error("User data is empty")
        }
      } catch (error) {
        console.log("Authorization check failed: ", error);
        this.isAuth = false
        this.userData = null
        return false;
      }
    },

    async login(user: UserLoginDTO) {
      try {
        const resp = await userService.loginUser(user)
        console.log(resp);

        // Test to see if the user is authenticated
        if (resp) {
          await this.checkIfAuth()
          if (this.userData) {
            return true
          }
        } else {
          throw new Error("Login was not successful");
        }
        return false
      } catch (error) {
        console.log("Error when login in to Yard: ", error);
      }
    },

    async signup(user: UserRegistrationDTO) {
      try {
        const resp = await userService.registerUser(user)

        if (resp) {
          await this.checkIfAuth()

          if (resp) {
            await this.checkIfAuth()
            if (this.userData) {
              return true
            }
          } else {
            throw new Error("Signing up was not successful");
          }
        }
      } catch (error) {
        console.log("Error when signing up to Yard: ", error);
      }
    }

  }
})
