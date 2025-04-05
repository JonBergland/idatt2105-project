import type { ItemsResponseDTO, ItemsRequestDTO } from "@/models/item";
import { defineStore } from "pinia";
import resultService from "@/services/item/resultService";

export const useResultStore = defineStore('result', {
  state: () => ({
    items: [] as ItemsResponseDTO['items'],
    isLoading: false,
    error: null as string | null,
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
  },
});
