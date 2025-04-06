import { setActivePinia, createPinia } from 'pinia';
import { useResultStore } from '@/stores/resultStore';
import resultService from '@/services/item/resultService';
import { describe, it, expect, vi, beforeEach } from 'vitest';

vi.mock('@/services/item/resultService', () => ({
  default: {
    getItems: vi.fn(),
    getCategories: vi.fn(),
  },
}));

describe('Result Store', () => {
  beforeEach(() => {
    setActivePinia(createPinia());
    vi.clearAllMocks();
  });

  describe('normalizeRequest', () => {
    it('should keep null priceMinMax as null', () => {
      const store = useResultStore();
      const request = {
        category: null,
        searchWord: null,
        priceMinMax: null,
        sort: null,
        segmentOffset: [0, 10],
      };

      const normalized = store.normalizeRequest(request);

      expect(normalized.priceMinMax).toBeNull();
    });

    it('should apply default values to null price range values', () => {
      const store = useResultStore();
      const request = {
        category: 'Electronics',
        searchWord: null,
        priceMinMax: [null, null],
        sort: null,
        segmentOffset: [0, 10],
      };

      const normalized = store.normalizeRequest(request);

      expect(normalized.priceMinMax?.[0]).toBe(0);
      expect(normalized.priceMinMax?.[1]).toBe(2147483647);
    });

    it('should apply default segment offset when null', () => {
      const store = useResultStore();
      const request = {
        category: null,
        searchWord: null,
        priceMinMax: null,
        sort: null,
        segmentOffset: null,
      };

      const normalized = store.normalizeRequest(request);

      expect(normalized.segmentOffset).toEqual([0, 10]);
    });
  });

  describe('fetchItems', () => {
    it('should set loading state while fetching', async () => {
      const store = useResultStore();

      vi.mocked(resultService.getItems).mockImplementation(() => {
        return new Promise(resolve => {
          setTimeout(() => resolve({ items: [] }), 100);
        });
      });

      const fetchPromise = store.fetchItems({
        category: null,
        searchWord: null,
        priceMinMax: null,
        sort: null,
        segmentOffset: [0, 10],
      });

      expect(store.isLoading).toBe(true);

      await fetchPromise;

      expect(store.isLoading).toBe(false);
    });

    it('should update items on successful fetch', async () => {
      const store = useResultStore();
      const mockItems = [
        { itemID: 1, name: 'Test Item 1', price: 100, category: 'Electronics', seller: 'User1', description: 'Test', published: '2023-04-01' },
        { itemID: 2, name: 'Test Item 2', price: 200, category: 'Books', seller: 'User2', description: 'Test', published: '2023-04-02' }
      ];

      vi.mocked(resultService.getItems).mockResolvedValue({ items: mockItems });

      await store.fetchItems({
        category: null,
        searchWord: null,
        priceMinMax: null,
        sort: null,
        segmentOffset: [0, 10],
      });

      expect(store.items).toEqual(mockItems);
      expect(store.error).toBeNull();
    });

    it('should handle errors correctly', async () => {
      const store = useResultStore();

      vi.mocked(resultService.getItems).mockRejectedValue(new Error('API error'));

      await store.fetchItems({
        category: null,
        searchWord: null,
        priceMinMax: null,
        sort: null,
        segmentOffset: [0, 10],
      });

      expect(store.error).toBe("Failed to fetch items.");
      expect(store.items).toEqual([]);
    });
  });

  describe('fetchCategories', () => {
    it('should update categories on successful fetch', async () => {
      const store = useResultStore();
      const mockCategories = {
        categories: ['Electronics', 'Books', 'Clothing']
      };

      vi.mocked(resultService.getCategories).mockResolvedValue(mockCategories);

      await store.fetchCategories();

      expect(store.categories).toEqual(mockCategories.categories);
      expect(store.categoriesError).toBeNull();
    });

    it('should handle errors correctly', async () => {
      const store = useResultStore();

      vi.mocked(resultService.getCategories).mockRejectedValue(new Error('API error'));

      await store.fetchCategories();

      expect(store.categoriesError).toBe("Failed to fetch categories.");
      expect(store.isCategoriesLoading).toBe(false);
    });
  });

  describe('loadMoreItems', () => {
    it('should set loading state while fetching more items', async () => {
      const store = useResultStore();

      vi.mocked(resultService.getItems).mockImplementation(() => {
        return new Promise(resolve => {
          setTimeout(() => resolve({ items: [] }), 100);
        });
      });

      const loadMorePromise = store.loadMoreItems({
        category: null,
        searchWord: null,
        priceMinMax: null,
        sort: null,
        segmentOffset: [1, 10],
      });

      expect(store.isLoadingMore).toBe(true);

      await loadMorePromise;

      expect(store.isLoadingMore).toBe(false);
    });

    it('should append new items to existing items', async () => {
      const store = useResultStore();

      const initialItems = [
        { itemID: 1, name: 'Initial Item 1', price: 100, category: 'Electronics', seller: 'User1', description: 'Test', published: '2023-04-01' },
        { itemID: 2, name: 'Initial Item 2', price: 200, category: 'Books', seller: 'User2', description: 'Test', published: '2023-04-02' }
      ];

      const moreItems = [
        { itemID: 3, name: 'More Item 1', price: 300, category: 'Clothing', seller: 'User3', description: 'Test', published: '2023-04-03' },
        { itemID: 4, name: 'More Item 2', price: 400, category: 'Sports', seller: 'User4', description: 'Test', published: '2023-04-04' }
      ];

      store.items = initialItems;

      vi.mocked(resultService.getItems).mockResolvedValue({ items: moreItems });

      await store.loadMoreItems({
        category: null,
        searchWord: null,
        priceMinMax: null,
        sort: null,
        segmentOffset: [1, 10],
      });

      expect(store.items).toHaveLength(4);
      expect(store.items[0].itemID).toBe(1);
      expect(store.items[2].itemID).toBe(3);
      expect(store.newItemsCount).toBe(2);
    });

    it('should set newItemsCount to the number of items received', async () => {
      const store = useResultStore();

      const moreItems = [
        { itemID: 5, name: 'More Item 3', price: 500, category: 'Home', seller: 'User5', description: 'Test', published: '2023-04-05' }
      ];

      vi.mocked(resultService.getItems).mockResolvedValue({ items: moreItems });

      await store.loadMoreItems({
        category: null,
        searchWord: null,
        priceMinMax: null,
        sort: null,
        segmentOffset: [2, 10],
      });

      expect(store.newItemsCount).toBe(1);
    });

    it('should handle empty response correctly', async () => {
      const store = useResultStore();

      store.items = [
        { itemID: 1, name: 'Initial Item', price: 100, category: 'Electronics', seller: 'User1', description: 'Test', published: '2023-04-01' }
      ];

      vi.mocked(resultService.getItems).mockResolvedValue({ items: [] });

      await store.loadMoreItems({
        category: null,
        searchWord: null,
        priceMinMax: null,
        sort: null,
        segmentOffset: [1, 10],
      });

      expect(store.items).toHaveLength(1);
      expect(store.newItemsCount).toBe(0);
    });

    it('should handle errors correctly when loading more items', async () => {
      const store = useResultStore();

      vi.mocked(resultService.getItems).mockRejectedValue(new Error('API error'));

      await store.loadMoreItems({
        category: null,
        searchWord: null,
        priceMinMax: null,
        sort: null,
        segmentOffset: [1, 10],
      });

      expect(store.moreItemsError).toBe("Failed to load more items.");
      expect(store.isLoadingMore).toBe(false);
    });
  });
});
