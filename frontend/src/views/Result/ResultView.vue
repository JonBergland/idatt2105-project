<script setup lang="ts">
import type { ItemsRequestDTO } from '@/models/item';
import { useRoute, useRouter } from 'vue-router';
import { ref, onMounted, watch} from 'vue';
import { useResultStore } from '@/stores/resultStore';
import ToggleGroup from '@/components/Result/ToggleGroup.vue';
import SearchBar from '@/components/Home/SearchBar.vue';
import ItemGroup from '@/components/Home/ItemGroup.vue';
import PriceFilter from '@/components/Result/PriceFilter.vue';
import CategoryButton from '@/components/Home/CategoryButton.vue';

// Display and sorting options
const sortModes = ref(['New', 'Price Up', 'Price Down']);
const displayModes = ref(['Grid', 'Column']);

// UI state for responsive design
const isFilterVisible = ref(false);
const screenWidth = ref(window.innerWidth);

// Current selection state
const currentDisplayMode = ref('');
const searchValue = ref('');
const sortSelected = ref('published_DESC');
const categorySelected = ref();

// Pagination state for infinite scroll
const isLoadingMore = ref(false);
const currentPage = ref(0);
const itemsPerPage = ref(6);
const hasMoreItems = ref(true);

const route = useRoute();
const router = useRouter();
const resultStore = useResultStore();

const itemsRequest = ref<ItemsRequestDTO>({
    category: null,
    searchWord: null,
    priceMinMax: null,
    sort: 'published_DESC',
    segmentOffset: [0, itemsPerPage.value],
});

window.addEventListener('resize', () => {
  screenWidth.value = window.innerWidth;
});

/**
 * Initializes the component when mounted.
 * This function:
 *
 * 1. Populates filter values from URL query parameters
 *    - Extracts search term, category, sort order, and price range
 *    - Updates both the request object and UI state variables
 *
 * 2. Fetches initial data
 *    - Loads available categories from the API
 *    - Loads the first page of items based on URL parameters
 *
 * 3. Sets up event listeners
 *    - Adds scroll event handler for infinite scrolling
 *
 * This allows users to:
 *   - Navigate directly to specific search results via URL
 *   - Share or bookmark specific filter combinations
 *   - Return to previous searches via browser history
 */
onMounted(() => {
  if (route.query.search) {
    itemsRequest.value.searchWord = route.query.search as string;
    searchValue.value = route.query.search as string;
  }

  if (route.query.category) {
    itemsRequest.value.category = route.query.category as string;
    categorySelected.value = route.query.category as string;
  }

  if (route.query.sort) {
    itemsRequest.value.sort = route.query.sort as string;
    sortSelected.value = route.query.sort as string;
  }

  if (route.query.minPrice && route.query.maxPrice) {
    itemsRequest.value.priceMinMax = [
      Number(route.query.minPrice),
      Number(route.query.maxPrice)
    ];
  }

  resultStore.fetchCategories();
  resultStore.fetchItems(itemsRequest.value);

  window.addEventListener('scroll', handleScroll);
});

/**
 * Monitors user scroll position and triggers loading of more items when the user
 * approaches the bottom of the page (80% scroll threshold).
 *
 * This function is bound to the window's scroll event in onMounted and helps
 * implement the infinite scrolling functionality.
 */
function handleScroll() {
  const scrollPosition = window.scrollY + window.innerHeight;
  const pageHeight = document.documentElement.scrollHeight;

  if (scrollPosition > pageHeight * 0.8 && !isLoadingMore.value && hasMoreItems.value) {
    loadMoreItems();
  }
}

/**
 * Loads the next page of items by incrementing the current page counter
 * and making a request to the API for more items.
 *
 * This function handles:
 * - Updating loading state indicators
 * - Constructing the pagination request with the correct offset
 * - Calling the store's loadMoreItems method
 * - Detecting when there are no more items to load
 *
 * @throws Will log errors to console but won't throw to UI
 */
 async function loadMoreItems() {
  if (isLoadingMore.value || !hasMoreItems.value) return;

  isLoadingMore.value = true;
  currentPage.value++;

  try {
    const nextPageRequest: ItemsRequestDTO = {
      ...itemsRequest.value,
      segmentOffset: [currentPage.value, itemsPerPage.value] as [number, number]
    };

    await resultStore.loadMoreItems(nextPageRequest);

    if (resultStore.newItemsCount < itemsPerPage.value) {
      hasMoreItems.value = false;
    }
  } catch (error) {
    console.error('Failed to load more items:', error);
  } finally {
    isLoadingMore.value = false;
  }
}

/**
 * Updates the URL query parameters based on the current filter state.
 * This ensures the URL reflects the current search criteria and allows
 * for shareable/bookmarkable search results.
 *
 * The function:
 * 1. Creates a query object from the current filters
 * 2. Compares it with the current URL query
 * 3. Only updates if there's a difference (to avoid unnecessary history entries)
 */
function updateUrlParams() {
  const query: Record<string, string> = {};

  if (itemsRequest.value.searchWord) query.search = itemsRequest.value.searchWord;
  if (itemsRequest.value.category) query.category = itemsRequest.value.category;
  if (itemsRequest.value.sort) query.sort = itemsRequest.value.sort;
  if (itemsRequest.value.priceMinMax) {
    const [min, max] = itemsRequest.value.priceMinMax;
    if (min !== null) query.minPrice = min.toString();
    if (max !== null) query.maxPrice = max.toString();
  }

  if (JSON.stringify(query) !== JSON.stringify(route.query)) {
    router.replace({ query });
  }
}

/**
 * Converts internal sort values to user-friendly display labels.
 *
 * @param {string | null} sortValue - The internal sort value (e.g., 'published_DESC')
 * @returns {string} The user-friendly label (e.g., 'New')
 *
 * Mapping:
 * - 'published_DESC' → 'New' (newest items first)
 * - 'price_ASC' → 'Price Up' (cheapest first)
 * - 'price_DESC' → 'Price Down' (most expensive first)
 */
function getSortLabel(sortValue: string | null): string {
  if (!sortValue) return 'published_DESC';
  switch(sortValue) {
    case 'published_DESC': return 'New';
    case 'price_ASC': return 'Price Up';
    case 'price_DESC': return 'Price Down';
    default: return 'published_DESC';
  }
}

/**
 * Toggles the visibility of the filter container.
 */
 function toggleFilterVisibility() {
  isFilterVisible.value = !isFilterVisible.value;
}

/**
 * Handles the display logic based on the provided displayMode.
 *
 * @param {string} displayMode - The mode used to determine the display logic.
 */
function handleDisplay(displayMode: string) {
  currentDisplayMode.value = displayMode;
}

/**
 * Handles sort mode selection from the ToggleGroup component.
 * Converts user-friendly sort names to API-compatible sort values.
 *
 * @param {string} sortMode - The selected sort mode ('New', 'Price Up', 'Price Down')
 */
function handleSort(sortMode: string) {
  switch(sortMode) {
    case 'New':
      itemsRequest.value.sort = 'published_DESC';
      break;
    case 'Price Up':
      itemsRequest.value.sort = 'price_ASC';
      break;
    case 'Price Down':
      itemsRequest.value.sort = 'price_DESC';
      break;
    default:
      itemsRequest.value.sort = null;
  }
};

/**
 * Handles search input from the SearchBar component.
 * Updates the search term in the request object which triggers
 * the watcher to fetch items and update URL parameters.
 *
 * @param {string} query - The search query entered by the user
 */
 function handleSearch(query: string) {
  itemsRequest.value.searchWord = query || null;
}

/**
 * Handles category selection from the ToggleGroup component.
 * Updates the category filter in the request object which triggers
 * the watcher to fetch items and update URL parameters.
 *
 * @param {string | null} category - The selected category name or null if deselected
 */
 function handleCategoryClick(category: string) {
  itemsRequest.value.category = category;
}

/**
 * Handles price range filter updates from the PriceFilter component.
 * Updates the min/max price values in the request object.
 *
 * @param {Object} priceRange - The updated price range
 * @param {number | null} priceRange.min - Minimum price (null for no lower limit)
 * @param {number | null} priceRange.max - Maximum price (null for no upper limit)
 */
function handlePriceRangeUpdated(priceRange: { min: number | null; max: number | null }): void {
  itemsRequest.value.priceMinMax = [priceRange.min, priceRange.max];
}

/**
 * Handles the 'item-clicked' event emitted by the RecommendationGrid component
 * @param {number} itemID - The unique identifier of the clicked item
 */
 function handleItemClick(itemID: number) {
  router.push({ name: 'product', query: { id: itemID } });
};

/**
 * Watches for filter changes (excluding pagination changes) and:
 * 1. Resets pagination state when filters change
 * 2. Fetches items with the updated filter criteria
 * 3. Updates URL parameters to reflect current search state
 */
 watch([
  () => itemsRequest.value.category,
  () => itemsRequest.value.searchWord,
  () => itemsRequest.value.priceMinMax,
  () => itemsRequest.value.sort
], () => {
  currentPage.value = 0;
  hasMoreItems.value = true;

  const resetRequest: ItemsRequestDTO = {
      ...itemsRequest.value,
      segmentOffset: [0, itemsPerPage.value] as [number, number]
    };

  itemsRequest.value.segmentOffset = [0, itemsPerPage.value];

  resultStore.fetchItems(resetRequest);
  updateUrlParams();
}, { deep: true });

</script>
<template>
    <div class="result-container">
      <div v-if="isFilterVisible || screenWidth >= 768" class="filter-container">
      <h3>Filter</h3>
      <div class="filter-wrapper">
        <p>Category:</p>
        <p v-if="resultStore.categoriesError"> {{ resultStore.categoriesError }}</p>
        <ToggleGroup
          v-else
          :names="resultStore.categories"
          :initial-selected="categorySelected"
          @toggle-selected="handleCategoryClick"
          direction="column"
          :allow-deselect="true"
          />
      </div>
      <div class="filter-wrapper">
        <p>Price:</p>
        <PriceFilter @price-range-updated="handlePriceRangeUpdated"/>
      </div>
    </div>
      <div class="search-toggle-items-container">
        <div class="search-toggle-container">
            <SearchBar
            v-model="searchValue"
            @search-input="handleSearch"
            />
          <div class="toggle-container">
            <div class="filter-display-container">
              <CategoryButton class="filter-toggle-button" @clicked-category="toggleFilterVisibility" :name="isFilterVisible ? 'Hide Filter' : 'Show Filter'"/>
              <ToggleGroup
                label="Display: "
                :names="displayModes"
                @toggle-selected="handleDisplay"
                :auto-select-first="true"/>
            </div>
            <ToggleGroup
              label="Sort by: "
              :names="sortModes"
              :initial-selected="getSortLabel(sortSelected)"
              @toggle-selected="handleSort"
            />
          </div>
        </div>
        <div class="item-group-warpper">
          <p v-if="resultStore.itemsError"> {{ resultStore.itemsError }}</p>
          <ItemGroup :items="resultStore.items" @item-clicked="handleItemClick" :mode="currentDisplayMode" />
          <div v-if="isLoadingMore" class="loading-more">Loading more items...</div>
          <div v-if="!hasMoreItems && resultStore.items.length > 0" class="end-of-results">No more items to display</div>
        </div>
      </div>
    </div>
</template>

<style scoped>
.result-container {
  display: flex;
  padding: 32px 128px;
  align-items: flex-start;
  justify-content: center;
  gap: 24px;

}

.filter-container {
  display: flex;
  min-width: 218px;
  max-width: 218px;
  padding: 16px;
  flex-direction: column;
  align-items: flex-start;
  justify-content: flex-start;
  gap: 24px;
  border-radius: 8px;
  border: 1px solid #D9D9D9;
}

.filter-wrapper {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.search-toggle-items-container {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  gap: 16px;
  width: 702px;
}

.search-toggle-container {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  gap: 16px;
  align-self: stretch;
}

.toggle-container {
  display: flex;
  justify-content: space-between;
  align-items: center;
  align-self: stretch;
  gap: 16px;
}

.category-grid {
  display: flex;
  align-items: flex-start;
}

.item-group-warpper {
  width: 100%;
  max-width: 702px;
}

.filter-toggle-button {
  display: none;
}

.loading-more {
  text-align: center;
  padding: 20px 0;
  width: 100%;
}

.end-of-results {
  text-align: center;
  padding: 20px 0;
  color: #888;
  width: 100%;
}

@media (max-width: 768px) {
  .result-container {
    padding: 32px 32px;
    display: flex;
    align-items: flex-start;
    justify-content: center;
    flex-wrap: wrap;
  }

  .toggle-container {
    flex-direction: column;
    align-items: flex-start;
  }

  .filter-toggle-button {
    display: block;
    margin-bottom: 16px;
  }

  .item-group-warpper {
  width: 100%;
  }

  .search-wrapper {
    display: flex;
    flex-direction: row;

  }

  .filter-display-container {
    display: flex;
    flex-direction: row;
    align-items: flex-start;
    justify-content: center;
    gap: 16px;
  }
}
</style>
