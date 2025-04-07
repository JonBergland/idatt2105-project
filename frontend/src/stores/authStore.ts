import type { User, UserLoginDTO, UserRegistrationDTO } from "@/models/user";
import { defineStore } from "pinia";
import userService from "@/services/user/userService"
import { useUserStore } from "@/stores/userStore";


/**
 * Store for authenticating the user with login and signup
 */
export const useAuthStore = defineStore('auth', {
  state: () => ({
    isAuth: false,
    userStore: useUserStore()
  }),

  actions: {

    /**
     * Checks if the user is authenticated by retrieving user information from the backend.
     * If the user data is valid, updates the authentication state and user data in the store.
     *
     * @returns {Promise<boolean>} A promise that resolves to `true` if the user is authenticated, otherwise `false`.
     * @throws {Error} If the user data is empty or an error occurs during the request.
     */
    async checkIfAuth() {
      try {
        this.userStore.setUser(await userService.getUserInfo());

        if (this.userStore.user) {
          this.isAuth = true;
          return true;
        } else {
          throw new Error("User data is empty");
        }
      } catch (error) {
        if (this.userStore.user == null) {
          this.isAuth = false;
          return false;
        }

        console.error("Unexpected error during auth check:", error);
        this.isAuth = false;
        this.userStore.setUser(null);
        return false;
      }
    },

    /**
     * Logs in a user with the provided credentials.
     * If the login is successful, checks if the user is authenticated and updates the store.
     *
     * @param {UserLoginDTO} user - A {@link UserLoginDTO} object containing the user's login credentials.
     * @returns {Promise<boolean>} A promise that resolves to `true` if the login is successful, otherwise `false`.
     * @throws {Error} If the login process fails or the response indicates an unsuccessful login.
     */
    async login(user: UserLoginDTO) {
      try {
        const resp = await userService.loginUser(user);
        console.log(resp);

        // Test to see if the user is authenticated
        if (resp) {
          await this.checkIfAuth();
          if (this.userStore.user) {
            return true;
          }
        } else {
          throw new Error("Login was not successful");
        }
        return false;
      } catch (error) {
        console.log("Error when login in to Yard: ", error);
      }
    },

    /**
     * Register a new user in the program with the provided credentials.
     * If the signup is successful, checks if the user is authenticated and updates the store.
     *
     * @param user  a {@link UserRegistrationDTO} object containing the information about the new user
     * @returns {Promise<boolean>} A promise that resolves to `true` if the login is successful, otherwise `false`.
     * @throws {Error} If the login process fails or the response indicates an unsuccessful login.
     */
    async signup(user: UserRegistrationDTO) {
      try {
        const resp = await userService.registerUser(user)

        if (resp) {
          await this.checkIfAuth()

          if (resp) {
            await this.checkIfAuth()
            if (this.userStore.user) {
              return true
            }
          } else {
            throw new Error("Signing up was not successful")
          }
        }
      } catch (error) {
        console.log("Error when signing up to Yard: ", error)
      }
    },

    /**
     * Logs out the user from the application and resets the store
     *
     * @return {Promise<void>}
     */
    async logout() {
      await userService.logoutUser()
      this.isAuth = false
      this.userStore.setUser(null)
    }
  }
})
