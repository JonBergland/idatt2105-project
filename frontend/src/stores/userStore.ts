import type { ItemsResponseDTO } from "@/models/item";
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
        if (user) {
          await userService.updateUserInfo(user);

          const newUser = await this.getUserInfo()

          const success = newUser !== null &&
            user.userID === newUser.userID &&
            user.name === newUser.name &&
            user.surname === newUser.surname &&
            user.email === newUser.email;

          return success;
        } else {
          return false
        }
      } catch (error) {
        console.log("Error when updating user information: ", error);
        return false;
      }
    },

    async getUserItems() {
      try {
        if (this.user) {
          const resp: ItemsResponseDTO | null = await userService.getUserItems(this.user);

          if (resp !== null) {
            return resp
          } else {
            throw new Error("Response was null");
          }
        } else {
          throw new Error("User not logged in");
        }

      } catch (error) {
        console.log("Unexpected error from trying to retrieve user items: ", error);
        // Return no items when there is an error
        let items: ItemsResponseDTO = {
          items: []
        }
        return items
      }
    }
  }
})
