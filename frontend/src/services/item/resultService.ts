import type { ItemsRequestDTO, ItemsResponseDTO } from '@/models/item.ts';
import axiosInstance from "@/services/axiosService";

class ResultService {
  /**
   * Fetches items from the backend based on the provided filters.
   * @param request The filter request object.
   * @returns A promise resolving to the filtered items.
   */
  async getItems(request: ItemsRequestDTO): Promise<ItemsResponseDTO> {
    const response = await axiosInstance.post<ItemsResponseDTO>('/filter', request);
    return response.data;
  }
}

const resultService = new ResultService();
export default resultService;
