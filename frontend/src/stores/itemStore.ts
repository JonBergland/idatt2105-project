import type { ItemsResponseDTO, ItemsRequestDTO, CategoriesResponseDTO, ItemRequestDTO, ItemResponseDTO } from "@/models/item";
import { defineStore } from "pinia";
import itemService from "@/services/item/itemService";

// Define constants for default values
const DEFAULT_MIN_PRICE = 0;
const DEFAULT_MAX_PRICE = 2147483647; // Maximum 32-bit integer
const DEFAULT_SEGMENT_SIZE = 10;

export const useItemStore = defineStore('result', {
  state: () => ({
    items: [] as ItemsResponseDTO['items'],
    isItemsLoading: false,
    itemsError: null as string | null,

    categories: [] as CategoriesResponseDTO['categories'],
    isCategoriesLoading: false,
    categoriesError: null as string | null,

    isLoadingMore: false,
    moreItemsError: null as string | null,
    newItemsCount: 0,

    item: null as ItemResponseDTO | null,
    isItemLoading: false,
    itemError: null as string | null,
  }),

  actions: {
    /**
     * Normalizes the request by applying default values to missing fields.
     * Creates a new object and doesn't modify the original.
     * @param request The original request object
     * @returns A normalized request object with defaults applied
     */
    normalizeRequest(request: ItemsRequestDTO): ItemsRequestDTO {
      const normalizedRequest = { ...request };

      // Handle price range - if it's null, keep it null (backend might handle differently)
      // If it has values but some are null, apply defaults
      if (normalizedRequest.priceMinMax !== null) {
        const [min, max] = normalizedRequest.priceMinMax || [null, null];
        normalizedRequest.priceMinMax = [
          min ?? DEFAULT_MIN_PRICE,
          max ?? DEFAULT_MAX_PRICE
        ];
      }

      // Handle segment offset with defaults if null
      if (normalizedRequest.segmentOffset === null) {
        normalizedRequest.segmentOffset = [0, DEFAULT_SEGMENT_SIZE];
      }

      return normalizedRequest;
    },

    /**
     * Fetches items from the backend based on the provided filters.
     * Automatically normalizes the request before sending.
     * @param itemRequest The filter request object
     */
    async fetchItems(itemRequest: ItemsRequestDTO) {
      this.isItemsLoading = true;
      this.itemsError = null;

      try {
        const normalizedRequest = this.normalizeRequest(itemRequest);
        const response = await itemService.getItems(normalizedRequest);
        this.items = response.items;
      } catch (error) {
        this.itemsError = "Failed to fetch items.";
        console.error("Error fetching items:", error);
      } finally {
        this.isItemsLoading = false;
      }
    },

    /**
     * Fetches more items and appends them to the existing items array.
     * Used for infinite scrolling functionality.
     *
     * @param request The filter and pagination request
     */
    async loadMoreItems(request: ItemsRequestDTO): Promise<void> {
      this.isLoadingMore = true;
      this.moreItemsError = null;

      try {
        const normalizedRequest = this.normalizeRequest(request);
        const response = await itemService.getItems(normalizedRequest);
        this.items = [...this.items, ...response.items];
        this.newItemsCount = response.items.length;
      } catch (error) {
        this.moreItemsError = "Failed to load more items.";
        console.error("Error loading more items:", error);
      } finally {
        this.isLoadingMore = false;
      }
    },

    /**
     * Fetches the list of categories from the result service and updates the store state.
     */
    async fetchCategories() {
      this.isCategoriesLoading = true;
      this.categoriesError = null;

      try {
        const response = await itemService.getCategories();
        this.categories = response.categories;
      } catch (error) {
        this.categoriesError = "Failed to fetch categories.";
        console.error("Error fetching categories:", error);
      } finally {
        this.isCategoriesLoading = false;
      }
    },

    /**
     * Fetches info about an item.
     */
    async fetchItemDetails(request: ItemRequestDTO) {
      this.isItemLoading = true;
      this.itemError = null;

      try {
        const response = await itemService.getItemDetails(request);
        this.item = response;
      } catch (error) {
        this.itemError = "Failed to fetch item.";
        console.error("Error fetching item:", error);
      } finally {
        this.isItemLoading = false;
      }
    }
  }
});
