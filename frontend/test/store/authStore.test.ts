import { describe, it, expect, beforeEach, vi } from 'vitest';
import { setActivePinia, createPinia } from 'pinia';
import { useAuthStore } from '@/stores/authStore';
import userService from '@/services/user/userService';
import type { UserLoginDTO, UserRegistrationDTO } from '@/models/user';

vi.mock('@/services/user/userService', () => ({
  default: {
    getUserInfo: vi.fn(),
    loginUser: vi.fn(),
    registerUser: vi.fn(),
    logoutUser: vi.fn()
  }
}));

vi.mock('@/stores/userStore', () => ({
  useUserStore: vi.fn(() => ({
    user: null,
    setUser: vi.fn(),
    getUserInfo: vi.fn()
  }))
}));

describe('AuthStore', () => {
  let authStore: ReturnType<typeof useAuthStore>;

  beforeEach(() => {
    setActivePinia(createPinia());
    vi.resetAllMocks();
    authStore = useAuthStore();
  });

  describe('checkIfAuth', () => {
    it('should return false when user data is empty', async () => {
      userService.getUserInfo.mockResolvedValue(null);

      const result = await authStore.checkIfAuth();

      expect(result).toBe(false);
      expect(authStore.isAuth).toBe(false);
    });

    it('should handle errors and set isAuth to false', async () => {
      userService.getUserInfo.mockRejectedValue(new Error('Network error'));

      const result = await authStore.checkIfAuth();

      expect(authStore.isAuth).toBe(false);
      expect(result).toBe(false);
    });
  });

  describe('login', () => {
    it('should return undefined when login response is false', async () => {
      const mockCredentials: UserLoginDTO = { email: 'test@example.com', password: 'wrong' };
      userService.loginUser.mockResolvedValue(false);

      const result = await authStore.login(mockCredentials);

      expect(result).toBeUndefined();
      expect(authStore.isAuth).toBe(false);
    });

    it('should handle errors during login', async () => {
      const mockCredentials: UserLoginDTO = { email: 'test@example.com', password: 'password123' };
      userService.loginUser.mockRejectedValue(new Error('Network error'));

      const consoleSpy = vi.spyOn(console, 'log');

      const result = await authStore.login(mockCredentials);

      expect(consoleSpy).toHaveBeenCalledWith(
        expect.stringContaining('Error when login in to Yard:'),
        expect.any(Error)
      );
      expect(result).toBeUndefined();
    });
  });

  describe('signup', () => {
    it('should attempt to register user when signup is called', async () => {
      const mockUserData: UserRegistrationDTO = {
        email: 'new@example.com',
        name: 'New',
        surname: 'User',
        password: 'password123',
        phoneNumber: 1234567890,
        countryCode: 1
      };

      userService.registerUser.mockResolvedValue(true);
      vi.spyOn(authStore, 'checkIfAuth').mockResolvedValue(true);

      await authStore.signup(mockUserData);

      expect(userService.registerUser).toHaveBeenCalledWith(mockUserData);
      expect(authStore.checkIfAuth).toHaveBeenCalled();
    });

    it('should handle unsuccessful signup', async () => {
      const mockUserData: UserRegistrationDTO = {
        email: 'new@example.com',
        name: 'New',
        surname: 'User',
        password: 'password123',
        phoneNumber: 1234567890,
        countryCode: 1
      };

      userService.registerUser.mockResolvedValue(false);

      const result = await authStore.signup(mockUserData);

      expect(result).toBeUndefined();
    });

    it('should handle errors during signup', async () => {
      const mockUserData: UserRegistrationDTO = {
        email: 'new@example.com',
        name: 'New',
        surname: 'User',
        password: 'password123',
        phoneNumber: 1234567890,
        countryCode: 1
      };

      userService.registerUser.mockRejectedValue(new Error('Network error'));

      const consoleSpy = vi.spyOn(console, 'log');

      const result = await authStore.signup(mockUserData);

      expect(consoleSpy).toHaveBeenCalledWith(
        expect.stringContaining('Error when signing up to Yard:'),
        expect.any(Error)
      );
      expect(result).toBeUndefined();
    });
  });

  describe('logout', () => {
    it('should reset auth state on logout', async () => {
      authStore.isAuth = true;

      await authStore.logout();

      expect(userService.logoutUser).toHaveBeenCalled();
      expect(authStore.isAuth).toBe(false);
    });
  });
});
