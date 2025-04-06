import type { ItemsRequestDTO, ItemsResponseDTO, CategoriesResponseDTO } from '@/models/item.ts';
import axiosInstance from "@/services/axiosService";

class ResultService {
  /**
   * Fetches items from the backend based on the provided filters.
   * @param request The filter request object.
   * @returns A promise resolving to the filtered items.
   */
  async getItems(request: ItemsRequestDTO): Promise<ItemsResponseDTO> {
    const response = await axiosInstance.post<ItemsResponseDTO>('/store/item/filter', request);
    return response.data;
  }

  /**
   * Fetches all available categories from the backend.
   * @returns A promise resolving to an array of category names.
   */
  async getCategories(): Promise<CategoriesResponseDTO> {
    const response = await axiosInstance.get<CategoriesResponseDTO>('/store/category');
    return response.data;
  }
}

const resultService = new ResultService();
export default resultService;
