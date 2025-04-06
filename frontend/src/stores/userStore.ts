import type { User } from "@/models/user";
import { defineStore } from "pinia";
import userService from "@/services/user/userService"

/**
 * Store for authenticating the user with login and signup
 */
export const useUserStore = defineStore('user', {
  state: () => ({
    user: null as User | null
  }),

  actions: {
    /**
     * Retrieves the user information from the user service and updates the store's user state.
     *
     * @returns {Promise<User | null>} A promise that resolves to the user object if successful,
     * or `null` if the user information could not be retrieved or an error occurs.
     *
     * @throws Will log an error message to the console if an exception occurs during the retrieval process.
     */
    async getUserInfo(): Promise<User | null> {
      try {
        this.user = await userService.getUserInfo()

        if (this.user != null) {
          return this.user
        }
      } catch (error) {
        console.log("Error when getting user information: ", error)
      }

      return null
    },

    /**
     * Updates the user information by sending the provided user object to the user service.
     * After updating, it retrieves the updated user information and compares it with the input.
     *
     * @param user - The user object containing updated information to be sent to the service.
     * @returns A promise that resolves to `true` if the update was successful and the retrieved user matches the input,
     *          otherwise resolves to `false`.
     * @throws Logs an error message to the console if an exception occurs during the update process.
     */
    async postUserInfo(user: User): Promise<boolean> {
      try {
        if (user != null && user != undefined) {
          await userService.updateUserInfo(user);

          let newUser = await this.getUserInfo()

          if (user === newUser) {
            return true
          } else {
            return false
          }
        } else {
          return false
        }
      } catch (error) {
        console.log("Error when updating user information");
        return false;
      }
    }
  }
})
