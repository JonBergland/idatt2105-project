export interface ItemsRequestDTO {
  category: string | null;
  searchWord: string | null;
  priceMinMax: [number | null, number | null] | null;
  sort: string | null;
  segmentOffset: [number, number] | null;
}

export interface ItemRequestDTO {
  itemID: number;
}

export interface ItemResponseDTO {
  itemID: number;
  name: string;
  category: string;
  seller: string;
  description: string;
  published: string;
  price: number;
  state: string;
  // TODO: Add Image url
}

export interface ItemsResponseDTO {
  items: ItemResponseDTO[];
}

export interface CategoriesResponseDTO {
  categories: string[];
}
