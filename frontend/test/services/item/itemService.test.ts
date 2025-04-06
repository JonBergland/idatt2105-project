import { describe, it, expect, vi, beforeEach } from 'vitest';
import resultService from '@/services/item/resultService';
import axiosInstance from '@/services/axiosService';

vi.mock('@/services/axiosService', () => ({
  default: {
    post: vi.fn(),
    get: vi.fn(),
  },
}));

describe('ResultService', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  describe('getItems', () => {
    it('should call the correct endpoint with the provided request data', async () => {
      const mockRequest = {
        category: 'Electronics',
        searchWord: 'laptop',
        priceMinMax: [100, 1000],
        sort: 'price_ASC',
        segmentOffset: [0, 10],
      };

      const mockResponse = {
        data: {
          items: [
            {
              itemID: 1,
              name: 'Test Laptop',
              category: 'Electronics',
              seller: 'TestUser',
              description: 'A test laptop',
              published: '2023-01-01',
              price: 500,
            },
          ],
        },
      };

      vi.mocked(axiosInstance.post).mockResolvedValueOnce(mockResponse);

      const result = await resultService.getItems(mockRequest);

      expect(axiosInstance.post).toHaveBeenCalledTimes(1);
      expect(axiosInstance.post).toHaveBeenCalledWith('/store/item/filter', mockRequest);
      expect(result).toEqual(mockResponse.data);
      expect(result.items).toHaveLength(1);
      expect(result.items[0].name).toBe('Test Laptop');
    });

    it('should handle null values in the request correctly', async () => {
      const mockRequest = {
        category: null,
        searchWord: null,
        priceMinMax: null,
        sort: null,
        segmentOffset: [0, 10],
      };

      const mockResponse = {
        data: {
          items: [],
        },
      };

      vi.mocked(axiosInstance.post).mockResolvedValueOnce(mockResponse);

      const result = await resultService.getItems(mockRequest);

      expect(axiosInstance.post).toHaveBeenCalledWith('/store/item/filter', mockRequest);
      expect(result).toEqual(mockResponse.data);
    });

    it('should propagate errors from the API', async () => {
      const mockRequest = {
        category: 'Electronics',
        searchWord: null,
        priceMinMax: null,
        sort: null,
        segmentOffset: [0, 10],
      };

      const mockError = new Error('Network Error');
      vi.mocked(axiosInstance.post).mockRejectedValueOnce(mockError);

      await expect(resultService.getItems(mockRequest)).rejects.toThrow('Network Error');
      expect(axiosInstance.post).toHaveBeenCalledWith('/store/item/filter', mockRequest);
    });
  });

  describe('getCategories', () => {
    it('should call the correct endpoint and return categories', async () => {
      const mockResponse = {
        data: {
          categories: ['Electronics', 'Clothing', 'Books', 'Furniture'],
        },
      };

      vi.mocked(axiosInstance.get).mockResolvedValueOnce(mockResponse);

      const result = await resultService.getCategories();

      expect(axiosInstance.get).toHaveBeenCalledTimes(1);
      expect(axiosInstance.get).toHaveBeenCalledWith('/store/category');
      expect(result).toEqual(mockResponse.data);
      expect(result.categories).toHaveLength(4);
      expect(result.categories).toContain('Electronics');
    });

    it('should propagate errors from the API', async () => {
      const mockError = new Error('API Error');
      vi.mocked(axiosInstance.get).mockRejectedValueOnce(mockError);

      await expect(resultService.getCategories()).rejects.toThrow('API Error');
      expect(axiosInstance.get).toHaveBeenCalledWith('/store/category');
    });
  });
});
