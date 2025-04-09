import { describe, it, expect, beforeEach, vi, afterEach } from 'vitest';
import { setActivePinia, createPinia } from 'pinia';
import { useUserStore } from '@/stores/userStore.ts';
import userService from '@/services/user/userService';
import type { User } from '@/models/user';

vi.mock('@/services/user/userService', () => ({
  default: {
    getUserInfo: vi.fn(),
    updateUserInfo: vi.fn(),
    getUserItems: vi.fn(),
    postItem: vi.fn(),
    getUserItemDetails: vi.fn(),
    updateItem: vi.fn(),
    toggleBookmark: vi.fn(),
    getBookmarkedItems: vi.fn()
  }
}));

describe('UserStore', () => {
  let store: ReturnType<typeof useUserStore>;

  beforeEach(() => {
    setActivePinia(createPinia());
    store = useUserStore();

    vi.resetAllMocks();
  });

  afterEach(() => {
    vi.resetAllMocks();
  });

  describe('setUser', () => {
    it('should set the user state', () => {
      const mockUser = {
        userID: 1,
        name: 'John',
        surname: 'Doe',
        email: 'john@example.com'
      };

      store.setUser(mockUser);

      expect(store.user).toEqual(mockUser);
    });

    it('should set user to null', () => {
      store.setUser({ userID: 1, name: 'Test', surname: 'User', email: 'test@example.com' });

      store.setUser(null);

      expect(store.user).toBeNull();
    });
  });

  describe('getUserInfo', () => {
    it('should fetch user info and update state', async () => {
      const mockUser = {
        userID: 1,
        name: 'John',
        surname: 'Doe',
        email: 'john@example.com'
      };

      userService.getUserInfo.mockResolvedValue(mockUser);

      const result = await store.getUserInfo();

      expect(userService.getUserInfo).toHaveBeenCalledTimes(1);
      expect(store.user).toEqual(mockUser);
      expect(result).toEqual(mockUser);
    });

    it('should return null if user info could not be retrieved', async () => {
      userService.getUserInfo.mockResolvedValue(null);

      const result = await store.getUserInfo();

      expect(userService.getUserInfo).toHaveBeenCalledTimes(1);
      expect(store.user).toBeNull();
      expect(result).toBeNull();
    });

    it('should handle errors and return null', async () => {
      userService.getUserInfo.mockRejectedValue(new Error('Network error'));

      const result = await store.getUserInfo();

      expect(userService.getUserInfo).toHaveBeenCalledTimes(1);
      expect(result).toBeNull();
    });
  });

  describe('postUserInfo', () => {
    it('should update user info and return true when successful', async () => {
      const mockUser = {
        userID: 1,
        name: 'John',
        surname: 'Doe',
        email: 'john@example.com'
      };

      userService.updateUserInfo.mockResolvedValue(undefined);
      vi.spyOn(store, 'getUserInfo').mockResolvedValue(mockUser);

      const result = await store.postUserInfo(mockUser);

      expect(userService.updateUserInfo).toHaveBeenCalledWith(mockUser);
      expect(store.getUserInfo).toHaveBeenCalledTimes(1);
      expect(result).toBe(true);
    });

    it('should return false when retrieved user does not match input', async () => {
      const inputUser = {
        userID: 1,
        name: 'John',
        surname: 'Doe',
        email: 'john@example.com'
      };

      const differentUser = {
        userID: 1,
        name: 'Jane',
        surname: 'Doe',
        email: 'john@example.com'
      };

      userService.updateUserInfo.mockResolvedValue(undefined);
      vi.spyOn(store, 'getUserInfo').mockResolvedValue(differentUser);

      const result = await store.postUserInfo(inputUser);

      expect(result).toBe(false);
    });

    it('should return false when user is null', async () => {
      const result = await store.postUserInfo(null as unknown as User);

      expect(result).toBe(false);
      expect(userService.updateUserInfo).not.toHaveBeenCalled();
    });

    it('should return false when an error occurs', async () => {
      const mockUser = {
        userID: 1,
        name: 'John',
        surname: 'Doe',
        email: 'john@example.com'
      };

      userService.updateUserInfo.mockRejectedValue(new Error('Network error'));

      const result = await store.postUserInfo(mockUser);

      expect(result).toBe(false);
    });
  });

  describe('updateUserItems', () => {
    it('should update user items when user is logged in', async () => {
      const mockUser = {
        userID: 1,
        name: 'John',
        surname: 'Doe',
        email: 'john@example.com'
      };

      const mockItems = {
        items: [
          { id: 1, name: 'Item 1', price: 100 },
          { id: 2, name: 'Item 2', price: 200 }
        ]
      };

      store.setUser(mockUser);
      userService.getUserItems.mockResolvedValue(mockItems);

      await store.updateUserItems();

      expect(userService.getUserItems).toHaveBeenCalledTimes(1);
      expect(store.userItems).toEqual(mockItems);
    });

    it('should throw an error when user is not logged in', async () => {
      store.setUser(null);

      await store.updateUserItems();

      expect(userService.getUserItems).not.toHaveBeenCalled();
      expect(store.userItems).toEqual({ items: [] });
    });

    it('should handle null response from service', async () => {
      const mockUser = {
        userID: 1,
        name: 'John',
        surname: 'Doe',
        email: 'john@example.com'
      };

      store.setUser(mockUser);
      userService.getUserItems.mockResolvedValue(null);

      await store.updateUserItems();

      expect(store.userItems).toEqual({ items: [] });
    });

    it('should handle errors from service', async () => {
      const mockUser = {
        userID: 1,
        name: 'John',
        surname: 'Doe',
        email: 'john@example.com'
      };

      store.setUser(mockUser);
      userService.getUserItems.mockRejectedValue(new Error('Network error'));

      await store.updateUserItems();

      expect(store.userItems).toEqual({ items: [] });
    });
  });

  describe('postItem', () => {
    it('should post an item and return true when successful', async () => {
      const mockItemRequest = {
        name: 'Test Item',
        description: 'Test Description',
        price: 100,
        category: 'Electronics'
      };

      userService.postItem.mockResolvedValue(undefined);

      const result = await store.postItem(mockItemRequest);

      expect(userService.postItem).toHaveBeenCalledWith(mockItemRequest);
      expect(result).toBe(true);
    });

    it('should return false when an error occurs', async () => {
      const mockItemRequest = {
        name: 'Test Item',
        description: 'Test Description',
        price: 100,
        category: 'Electronics'
      };

      userService.postItem.mockRejectedValue(new Error('Server error'));

      const result = await store.postItem(mockItemRequest);

      expect(userService.postItem).toHaveBeenCalledWith(mockItemRequest);
      expect(result).toBe(false);
    });
  });

  describe('fetchUserItemDetails', () => {
    it('should fetch user item details successfully', async () => {
      const mockRequest: ItemRequestDTO = { itemID: 123 };
      const mockResponse = {
        itemID: 123,
        name: 'Test Item',
        description: 'Test Description',
        price: 100,
        category: 'Electronics',
        seller: 'test@example.com',
        sellerID: 1,
        state: 'available'
      };

      userService.getUserItemDetails.mockResolvedValue(mockResponse);

      await store.fetchUserItemDetails(mockRequest);

      expect(userService.getUserItemDetails).toHaveBeenCalledWith(mockRequest);
      expect(store.item).toEqual(mockResponse);
      expect(store.isItemLoading).toBe(false);
      expect(store.itemError).toBeNull();
    });

    it('should handle error when fetching item details fails', async () => {
      const mockRequest: ItemRequestDTO = { itemID: 123 };

      userService.getUserItemDetails.mockRejectedValue(new Error('Failed to fetch'));

      await store.fetchUserItemDetails(mockRequest);

      expect(store.isItemLoading).toBe(false);
      expect(store.itemError).toBe('Failed to fetch item.');
      expect(store.item).toBeNull();
    });
  });

  describe('updateItemDetails', () => {
    it('should update item details successfully', async () => {
      const mockRequest = {
        itemID: 123,
        name: 'Updated Name',
        description: 'Updated Description',
        price: 150,
        category: 'Updated Category'
      };

      store.item = {
        itemID: 123,
        name: 'Original Name',
        description: 'Original Description',
        price: 100,
        category: 'Original Category',
        seller: 'test@example.com',
        sellerID: 1,
        state: 'available'
      };

      store.userItems = {
        items: [{
          itemID: 123,
          name: 'Original Name',
          description: 'Original Description',
          price: 100,
          category: 'Original Category',
          seller: 'test@example.com',
          sellerID: 1,
          state: 'available'
        }]
      };

      userService.updateItem.mockResolvedValue(undefined);

      const result = await store.updateItemDetails(mockRequest);

      expect(userService.updateItem).toHaveBeenCalledWith(mockRequest);
      expect(result).toBe(true);
      expect(store.item?.name).toBe('Updated Name');
      expect(store.item?.description).toBe('Updated Description');
      expect(store.item?.price).toBe(150);
      expect(store.item?.category).toBe('Updated Category');
      expect(store.userItems.items[0].name).toBe('Updated Name');
      expect(store.isItemLoading).toBe(false);
    });

    it('should update item details in store even if item ID does not match current item', async () => {
      const mockRequest = {
        itemID: 123,
        name: 'Updated Name',
        description: 'Updated Description',
        price: 150,
        category: 'Updated Category'
      };

      store.item = {
        itemID: 456,
        name: 'Different Item',
        description: 'Different Description',
        price: 200,
        category: 'Different Category',
        seller: 'other@example.com',
        sellerID: 2,
        state: 'available'
      };

      store.userItems = {
        items: [{
          itemID: 123,
          name: 'Original Name',
          description: 'Original Description',
          price: 100,
          category: 'Original Category',
          seller: 'test@example.com',
          sellerID: 1,
          state: 'available'
        }]
      };

      userService.updateItem.mockResolvedValue(undefined);

      const result = await store.updateItemDetails(mockRequest);

      expect(result).toBe(true);
      expect(store.item?.itemID).toBe(456);
      expect(store.item?.name).toBe('Different Item');
      expect(store.userItems.items[0].name).toBe('Updated Name');
    });

    it('should handle error when updating item details fails', async () => {
      const mockRequest = {
        itemID: 123,
        name: 'Updated Name',
        description: 'Updated Description',
        price: 150,
        category: 'Updated Category'
      };

      userService.updateItem.mockRejectedValue(new Error('Failed to update'));

      const result = await store.updateItemDetails(mockRequest);

      expect(result).toBe(false);
      expect(store.itemError).toBe('Failed to update item.');
      expect(store.isItemLoading).toBe(false);
    });
  });

  describe('toggleBookmark', () => {
    it('should toggle bookmark successfully', async () => {
      const mockRequest: ToggleBookmarkRequest = {
        itemID: 123,
        bookmark: true
      };

      userService.toggleBookmark.mockResolvedValue({});

      const result = await store.toggleBookmark(mockRequest);

      expect(userService.toggleBookmark).toHaveBeenCalledWith(mockRequest);
      expect(result).toBe(true);
    });

    it('should handle error when toggling bookmark fails', async () => {
      const mockRequest: ToggleBookmarkRequest = {
        itemID: 123,
        bookmark: true
      };

      userService.toggleBookmark.mockRejectedValue(new Error('Failed to toggle bookmark'));

      const result = await store.toggleBookmark(mockRequest);

      expect(result).toBe(false);
    });
  });

  describe('fetchBookmarkedItems', () => {
    it('should fetch bookmarked items successfully when user is logged in', async () => {
      const mockUser = { email: 'test@example.com' } as User;
      const mockRequest: GetBookmarkedItemsRequest = {
        segmentOffset: [0, 10]
      };
      const mockResponse = [
        {
          itemID: 123,
          name: 'Bookmarked Item',
          description: 'Description',
          price: 100,
          category: 'Electronics'
        }
      ];

      store.setUser(mockUser);
      userService.getBookmarkedItems.mockResolvedValue(mockResponse);

      await store.fetchBookmarkedItems(mockRequest);

      expect(userService.getBookmarkedItems).toHaveBeenCalledWith(mockRequest);
      expect(store.bookmarkedItems).toEqual(mockResponse);
    });

    it('should handle situation when user is not logged in', async () => {
      const mockRequest: GetBookmarkedItemsRequest = {
        segmentOffset: [0, 10]
      };

      store.setUser(null);

      await store.fetchBookmarkedItems(mockRequest);

      expect(userService.getBookmarkedItems).not.toHaveBeenCalled();
      expect(store.bookmarkedItems).toEqual([]);
    });

    it('should handle error when fetching bookmarked items fails', async () => {
      const mockUser = { email: 'test@example.com' } as User;
      const mockRequest: GetBookmarkedItemsRequest = {
        segmentOffset: [0, 10]
      };

      store.setUser(mockUser);
      userService.getBookmarkedItems.mockRejectedValue(new Error('Failed to fetch'));

      await store.fetchBookmarkedItems(mockRequest);

      expect(store.bookmarkedItems).toEqual([]);
    });
  });

  describe('loadMoreBookmarkedItems', () => {
    it('should load more bookmarked items successfully', async () => {
      const mockRequest: GetBookmarkedItemsRequest = {
        segmentOffset: [10, 10]
      };
      const existingItems = [
        { itemID: 1, name: 'Item 1' },
        { itemID: 2, name: 'Item 2' }
      ];
      const newItems = [
        { itemID: 3, name: 'Item 3' },
        { itemID: 4, name: 'Item 4' }
      ];

      store.bookmarkedItems = existingItems;
      userService.getBookmarkedItems.mockResolvedValue(newItems);

      await store.loadMoreBookmarkedItems(mockRequest);

      expect(userService.getBookmarkedItems).toHaveBeenCalledWith(mockRequest);
      expect(store.bookmarkedItems).toEqual([...existingItems, ...newItems]);
      expect(store.newBookmarkedItemsCount).toBe(newItems.length);
    });

    it('should handle error when loading more bookmarked items fails', async () => {
      const mockRequest: GetBookmarkedItemsRequest = {
        segmentOffset: [10, 10]
      };
      const existingItems = [
        { itemID: 1, name: 'Item 1' },
        { itemID: 2, name: 'Item 2' }
      ];

      store.bookmarkedItems = existingItems;
      userService.getBookmarkedItems.mockRejectedValue(new Error('Failed to load more'));

      await expect(store.loadMoreBookmarkedItems(mockRequest)).rejects.toThrow('Failed to load more');

      expect(store.bookmarkedItems).toEqual(existingItems);
    });
  });
});
