export interface ItemsRequestDTO {
  category: string | null;
  searchWord: string | null;
  priceMinMax: [number | null, number | null] | null;
  sort: string | null;
  segmentOffset: [number, number] | null;
}

export interface ItemResponseDTO {
  itemID: number;
  name: string;
  category: string;
  seller: string;
  description: string;
  published: string;
  price: number;
}

export interface ItemsResponseDTO {
  items: ItemResponseDTO[];
}

export interface CategoriesResponseDTO {
  categories: string[];
}
