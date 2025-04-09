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
    postItem: vi.fn()
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
});
