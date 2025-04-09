import type { ItemRequestDTO, ItemsResponseDTO, ItemResponseDTO } from "@/models/item";
import type { User, AddItemRequest, UpdateItemRequest, ToggleBookmarkRequest, GetBookmarkedItemsRequest} from "@/models/user";
import { defineStore } from "pinia";
import userService from "@/services/user/userService"

/**
 * Store for authenticating the user with login and signup
 */
export const useUserStore = defineStore('user', {
  state: () => ({
    user: null as User | null,
    userItems: { items: [] } as ItemsResponseDTO,
    item: null as ItemResponseDTO | null,
    isItemLoading: false,
    itemError: null as string | null,
    messagesNotSeen: false,
    bookmarkedItems: [],
    newBookmarkedItemsCount: 0,
  }),

  actions: {
    /**
     * Setter for the user state.
     *
     * @param user - The user object to set as the current user.
     */
    setUser(user: User | null): void {
      this.user = user;
    },

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

    /**
     * Updates the `userItems` property by fetching the user's items from the `userService`.
     *
     * @throws {Error} If the user is not logged in.
     * @throws {Error} If the response from the `userService` is null.
     *
     * @returns {Promise<void>} A promise that resolves when the user items are successfully updated.
     */
    async updateUserItems(): Promise<void> {
      try {
        if (this.user) {
          const resp = await userService.getUserItems();

          if (resp !== null) {
            this.userItems = resp
          } else {
            throw new Error("Response was null");
          }
        } else {
          throw new Error("User not logged in");
        }
      } catch (error) {
        console.log("Unexpected error from trying to retrieve user items: ", error);
        this.userItems =  { items: [] }
      }
    },

    /**
     * Sends a request to post an item using the provided `AddItemRequest` object.
     *
     * @param request - The request object containing the details of the item to be posted.
     * @returns A promise that resolves to `true` if the item was successfully posted,
     *          or `false` if an error occurred during the operation.
     * @throws Logs an error message to the console if the operation fails.
     */
    async postItem(request: AddItemRequest): Promise<boolean> {
      try {
        await userService.postItem(request);
        return true;
      } catch (error) {
        console.log("Error when getting user information: ", error)
        return false;
      }
    },

    /**
     * Fetches the details of a user item based on the provided request.
     *
     * @param request - An object of type `ItemRequestDTO` containing the details of the item to fetch.
     *
     */
    async fetchUserItemDetails(request: ItemRequestDTO) {
      this.isItemLoading = true;
      this.itemError = null;

      try {
        const response = await userService.getUserItemDetails(request);
        this.item = response;
      } catch (error) {
        this.itemError = "Failed to fetch item.";
        console.error("Error fetching item:", error);
      } finally {
        this.isItemLoading = false;
      }
    },

    /**
     * Updates the details of an existing item.
     *
     * @param request - The request object containing the updated item details and item ID.
     * @returns A promise that resolves to `true` if the item was successfully updated,
     *          or `false` if an error occurred during the operation.
     */
    async updateItemDetails(request: UpdateItemRequest): Promise<boolean> {
      this.isItemLoading = true;
      this.itemError = null;

      try {
        await userService.updateItem(request);

        if (this.item && this.item.itemID === request.itemID) {
          this.item = {
            ...this.item,
            name: request.name,
            description: request.description,
            price: request.price,
            category: request.category
          };
        }

        if (this.userItems && this.userItems.items) {
          const index = this.userItems.items.findIndex(item => item.itemID === request.itemID);
          if (index !== -1) {
            this.userItems.items[index] = {
              ...this.userItems.items[index],
              name: request.name,
              description: request.description,
              price: request.price,
              category: request.category
            };
          }
        }

        return true;
      } catch (error) {
        this.itemError = "Failed to update item.";
        console.error("Error updating item:", error);
        return false;
      } finally {
        this.isItemLoading = false;
      }
    },

    /**
     * Toggles the bookmark status for a given request.
     *
     *
     * @param {ToggleBookmarkRequest} request - The request object containing the necessary
     * information to toggle the bookmark.
     * @returns {Promise<boolean>} A promise that resolves to `true` if the operation
     * succeeds, or `false` if an error occurs.
     */
    async toggleBookmark(request: ToggleBookmarkRequest): Promise<boolean> {
      try {
        await userService.toggleBookmark(request);
        return true;
      } catch (error) {
        console.log("Error when toggeling bookmark: ", error)
        return false;
      }
    },

    /**
     * Fetches the bookmarked items for the currently logged-in user.
     *
     * @param request - An object containing the parameters required to fetch the bookmarked items.
     * @returns A promise that resolves when the bookmarked items have been fetched and stored.
     *
     * @throws An error if the user is not logged in or if the response from the service is null.
     *
     * If an error occurs during the fetch operation, the `bookmarkedItems` property is reset to an empty list.
     */
    async fetchBookmarkedItems(request: GetBookmarkedItemsRequest): Promise<void> {
      try {
        if (this.user) {
          const response = await userService.getBookmarkedItems(request);

          if (response !== null) {
            this.bookmarkedItems = response
          } else {
            throw new Error("Response was null");
          }
        } else {
          throw new Error("User not logged in");
        }
      } catch (error) {
        console.log("Unexpected error from trying to retrieve user items: ", error);
        this.bookmarkedItems = [];
      }
    },

    async loadMoreBookmarkedItems(request: GetBookmarkedItemsRequest) {
      try {
        const response = await userService.getBookmarkedItems(request);
        const newItems = response;

        // Store the count of new items for pagination logic
        this.newBookmarkedItemsCount = newItems.length;

        // Append new items to existing items
        this.bookmarkedItems = [...this.bookmarkedItems, ...newItems];

        return newItems;
      } catch (error) {
        console.error('Error loading more bookmarked items:', error);
        throw error;
      }
    }
  }
})
