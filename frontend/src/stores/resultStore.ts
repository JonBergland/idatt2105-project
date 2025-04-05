import type { ItemsResponseDTO, ItemsRequestDTO } from "@/models/item";
import { defineStore } from "pinia";
import resultService from "@/services/item/resultService";

export const useResultStore = defineStore('result', {
  state: () => ({
    items: [] as ItemsResponseDTO['items'],
    isLoading: false,
    error: null as string | null,

    categories: [] as string[],
    isCategoriesLoading: false,
    categoriesError: null as string | null,
  }),

  actions: {
    async fetchItems(itemRequest: ItemsRequestDTO) {
      this.isLoading = true;
      this.error = null;

      try {
        const response = await resultService.getItems(itemRequest);
        this.items = response.items;
      } catch (error) {
        this.error = "Failed to fetch items.";
        console.error("Error fetching items:", error);
      } finally {
        this.isLoading = false;
      }
    },

    async fetchCategories() {
      this.isCategoriesLoading = true;
      this.categoriesError = null;

      try {
        this.categories = await resultService.getCategories();
      } catch (error) {
        this.categoriesError = "Failed to fetch categories.";
        console.error("Error fetching categories:", error);
      } finally {
        this.isCategoriesLoading = false;
      }
    }
  },
});
