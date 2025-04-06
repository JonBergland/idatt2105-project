<script setup lang="ts">
import type { ItemsRequestDTO } from '@/models/item';
import { ref, onMounted } from 'vue';
import { useResultStore } from '@/stores/resultStore';
import ToggleGroup from '@/components/Result/ToggleGroup.vue';
import SearchBar from '@/components/Home/SearchBar.vue';
import ItemGroup from '@/components/Home/ItemGroup.vue';
import PriceFilter from '@/components/Result/PriceFilter.vue';
import CategoryButton from '@/components/Home/CategoryButton.vue';

const sortModes = ref(['New', 'Price Up', 'Price Down']);
const displayModes = ref(['Grid', 'Column']);
const currentDisplayMode = ref('');
const isFilterVisible = ref(false);
const screenWidth = ref(window.innerWidth);

window.addEventListener('resize', () => {
  screenWidth.value = window.innerWidth;
});

const resultStore = useResultStore();

const itemsRequest = ref<ItemsRequestDTO>({
    category: null,
    searchWord: null,
    priceMinMax: null,
    sort: null,
    segmentOffset: [0, 10],
});

onMounted(() => {
  resultStore.fetchCategories();
  resultStore.fetchItems(itemsRequest.value);
});

/**
 * Toggles the visibility of the filter container.
 */
 function toggleFilterVisibility() {
  isFilterVisible.value = !isFilterVisible.value;
  console.log(isFilterVisible.value)
}

/**
 * Handles the display logic based on the provided displayMode.
 *
 * @param {string} displayMode - The mode used to determine the display logic.
 */
function handleDisplay(displayMode: string) {
  currentDisplayMode.value = displayMode;
  console.log(displayMode);
}

/**
 * Handles the sorting functionality based on the provided sort mode.
 *
 * @param {String} sortMode - The mode by which the data should be sorted.
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
  resultStore.fetchItems(itemsRequest.value);
};

/**
 * Handles the 'search' event emitted by the SearchBar component.
 *
 * @param {string} query - The search query entered by the user
 */
 function handleSearch(query: string) {
  itemsRequest.value.searchWord = query;
  resultStore.fetchItems(itemsRequest.value)
}

/**
 * Handles the 'category-clicked' event emitted by the CategoryGrid component
 * @param {string} category - The name of the clicked category
 */
 function handleCategoryClick(category: string) {
  itemsRequest.value.category = category;
  resultStore.fetchItems(itemsRequest.value)
}

/**
 * Handles the event when the price range is updated.
 *
 * @param {Object} priceRange - The updated price range object.
 * @param {number | null} priceRange.min - The minimum price in the range, or null if not set.
 * @param {number | null} priceRange.max - The maximum price in the range, or null if not set.
 */
function handlePriceRangeUpdated(priceRange: { min: number | null; max: number | null }): void {
  itemsRequest.value.priceMinMax = [priceRange.min, priceRange.max];
  resultStore.fetchItems(itemsRequest.value)
}

/**
 * Handles the 'item-clicked' event emitted by the RecommendationGrid component
 * @param {number} itemId - The unique identifier of the clicked item
 */
 function handleItemClick(itemId: number) {
  console.log('Clicked Item: ', itemId);
  //TODO: implement
};

</script>
<template>
    <div class="result-container">
      <div v-if="isFilterVisible || screenWidth >= 768" class="filter-container">
      <h3>Filter</h3>
      <div class="filter-wrapper">
        <p>Category:</p>
        <p v-if="resultStore.categoriesError"> {{ resultStore.categoriesError }}</p>
        <ToggleGroup v-else :names="resultStore.categories" @toggle-selected="handleCategoryClick" direction="column" :allow-deselect="true"/>
      </div>
      <div class="filter-wrapper">
        <p>Price:</p>
        <PriceFilter @price-range-updated="handlePriceRangeUpdated"/>
      </div>
    </div>
      <div class="search-toggle-items-container">
        <div class="search-toggle-container">
            <SearchBar @search-input="handleSearch" />
          <div class="toggle-container">
            <div class="filter-display-container">
              <CategoryButton class="filter-toggle-button" @clicked-category="toggleFilterVisibility" :name="isFilterVisible ? 'Hide Filter' : 'Show Filter'"/>
              <ToggleGroup label="Display: " :names="displayModes" @toggle-selected="handleDisplay" :auto-select-first="true"/>
            </div>
            <ToggleGroup label="Sort by: " :names="sortModes" @toggle-selected="handleSort" :auto-select-first="true"/>
          </div>
        </div>
        <div class="item-group-warpper">
          <p v-if="resultStore.error"> {{ resultStore.error }}</p>
          <ItemGroup :items="resultStore.items" @item-clicked="handleItemClick" :mode="currentDisplayMode" />
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
