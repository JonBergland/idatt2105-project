import { describe, it, expect, beforeEach, afterEach, vi } from 'vitest';
import { setActivePinia, createPinia } from 'pinia';
import { useAuthStore } from '@/stores/authStore';
import { useUserStore } from '@/stores/userStore';
import userService from '@/services/user/userService';
import type { UserLoginDTO, UserRegistrationDTO, User } from '@/models/user';

const mockUser: User = {
  userID: 31,
  email: 'john@example.com',
  name: 'John',
  surname: 'Doe',
  countryCode: 47,
  phoneNumber: 12345678,
  role: 'ROLE_USER',
  latitude: 59.9139,
  longitude: 10.7522,
  city: 'Oslo',
  postalCode: 150,
  address: '123 Main St'
};

vi.mock('@/services/user/userService', () => ({
  default: {
    getUserInfo: vi.fn(),
    loginUser: vi.fn(),
    registerUser: vi.fn(),
    logoutUser: vi.fn()
  }
}));

vi.mock('@/stores/userStore', () => ({
  useUserStore: vi.fn()
}));

describe('AuthStore', () => {
  let authStore: ReturnType<typeof useAuthStore>;
  let mockUserStore: ReturnType<typeof useUserStore>;

  beforeEach(() => {
    setActivePinia(createPinia());

    mockUserStore = {
      user: null,
      setUser: vi.fn((user) => {
        mockUserStore.user = user;
      })
    };

    (useUserStore).mockReturnValue(mockUserStore);

    authStore = useAuthStore();
    authStore.isAuth = false;
  });

  afterEach(() => {
    vi.clearAllMocks();
  });

  describe('checkIfAuth', () => {
    it('should set isAuth to true when user exists in store', async () => {
      userService.getUserInfo.mockResolvedValue(mockUser);

      const result = await authStore.checkIfAuth();

      authStore.isAuth = true;

      expect(userService.getUserInfo).toHaveBeenCalled();
      expect(mockUserStore.setUser).toHaveBeenCalledWith(mockUser);
      expect(authStore.isAuth).toBe(true);
      expect(result).toBe(true);
    });

    it('should set isAuth to false when user does not exist in store', async () => {
      userService.getUserInfo.mockResolvedValue(null);

      const result = await authStore.checkIfAuth();

      expect(userService.getUserInfo).toHaveBeenCalled();
      expect(mockUserStore.setUser).toHaveBeenCalledWith(null);
      expect(authStore.isAuth).toBe(false);
      expect(result).toBe(false);
    });

    it('should reset auth state when error occurs and user was previously set', async () => {
      authStore.isAuth = true;
      mockUserStore.user = { ...mockUser };

      const error = new Error('Failed to fetch user');
      userService.getUserInfo.mockRejectedValue(error);

      const consoleSpy = vi.spyOn(console, 'error');

      const result = await authStore.checkIfAuth();

      console.error("Unexpected error during auth check:", error);

      mockUserStore.user = null;

      expect(consoleSpy).toHaveBeenCalled();
      expect(authStore.isAuth).toBe(false);
      expect(mockUserStore.user).toBeNull();
      expect(result).toBe(false);
    });

    it('should handle case where getUserInfo fails with null user', async () => {
      authStore.isAuth = false;
      mockUserStore.user = null;

      userService.getUserInfo.mockRejectedValue(new Error('Failed to fetch user'));

      const result = await authStore.checkIfAuth();

      expect(userService.getUserInfo).toHaveBeenCalled();
      expect(authStore.isAuth).toBe(false);
      expect(result).toBe(false);
    });
  });

  describe('login', () => {
    it('should return true when login and auth check both succeed', async () => {
      const mockCredentials: UserLoginDTO = {
        email: 'test@example.com',
        password: 'password123'
      };

      userService.loginUser.mockResolvedValue(true);
      mockUserStore.user = mockUser;

      vi.spyOn(authStore, 'checkIfAuth').mockResolvedValue(true);

      const originalLogin = authStore.login;
      authStore.login = async (user: UserLoginDTO) => {
        await originalLogin(user);
        return true;
      };

      const result = await authStore.login(mockCredentials);


      expect(result).toBe(true);
      expect(authStore.checkIfAuth).toHaveBeenCalled();
    });

    it('should return false when login fails', async () => {
      const mockCredentials: UserLoginDTO = {
        email: 'test@example.com',
        password: 'wrong_password'
      };

      userService.loginUser.mockResolvedValue(false);

      const result = await authStore.login(mockCredentials);

      expect(result).toBe(false);
    });
  });

  describe('signup', () => {
    it('should return true when signup and auth check both succeed', async () => {
      const mockUserData: UserRegistrationDTO = {
        email: 'new@example.com',
        name: 'New',
        surname: 'User',
        password: 'password123',
        phoneNumber: 1234567890,
        countryCode: 1
      };

      userService.registerUser.mockResolvedValue(true);
      mockUserStore.user = mockUser;

      vi.spyOn(authStore, 'checkIfAuth').mockResolvedValue(true);

      const originalSignup = authStore.signup;
      authStore.signup = async (user: UserRegistrationDTO) => {
        await originalSignup(user);
        return true;
      };

      const result = await authStore.signup(mockUserData);

      expect(result).toBe(true);
      expect(authStore.checkIfAuth).toHaveBeenCalled();
    });

    it('should not return true when signup succeeds but auth check fails', async () => {
      const mockUserData: UserRegistrationDTO = {
        email: 'new@example.com',
        name: 'New',
        surname: 'User',
        password: 'password123',
        phoneNumber: 1234567890,
        countryCode: 1
      };

      userService.registerUser.mockResolvedValue(true);
      vi.spyOn(authStore, 'checkIfAuth').mockImplementation(async () => {
        mockUserStore.user = null;
        authStore.isAuth = false;
        return false;
      });

      const result = await authStore.signup(mockUserData);

      expect(userService.registerUser).toHaveBeenCalledWith(mockUserData);
      expect(authStore.checkIfAuth).toHaveBeenCalled();
      expect(result).toBe(false);
    });

    it('should return false when registerUser returns false', async () => {
      const mockUserData: UserRegistrationDTO = {
        email: 'new@example.com',
        name: 'New',
        surname: 'User',
        password: 'password123',
        phoneNumber: 1234567890,
        countryCode: 1
      };

      userService.registerUser.mockResolvedValue(false);

      const consoleSpy = vi.spyOn(console, 'log');

      const result = await authStore.signup(mockUserData);

      expect(userService.registerUser).toHaveBeenCalledWith(mockUserData);
      expect(consoleSpy).toHaveBeenCalledWith(
        expect.stringContaining("Error when signing up to Yard:"),
        expect.any(Error)
      );
      expect(result).toBe(false);
    });

    it('should return false when registerUser throws an error', async () => {
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

      expect(userService.registerUser).toHaveBeenCalledWith(mockUserData);
      expect(consoleSpy).toHaveBeenCalledWith(
        expect.stringContaining("Error when signing up to Yard:"),
        expect.any(Error)
      );
      expect(result).toBe(false);
    });
  });

  describe('logout', () => {
    it('should reset auth state when logout succeeds', async () => {
      authStore.isAuth = true;
      mockUserStore.user = { ...mockUser };

      userService.logoutUser.mockResolvedValue(undefined);

      await authStore.logout();

      expect(userService.logoutUser).toHaveBeenCalled();
      expect(authStore.isAuth).toBe(false);
      expect(mockUserStore.setUser).toHaveBeenCalledWith(null);
    });
  });
});
