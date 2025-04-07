<script setup lang="ts">
import type { ItemsRequestDTO } from '@/models/item';
import CategoryGrid from '@/components/Home/CategoryGrid.vue';
import RecommendationGrid from '@/components/Home/ItemGroup.vue';
import SearchBar from '@/components/Home/SearchBar.vue';
import { onMounted, ref, onUnmounted } from 'vue';
import { useRouter } from 'vue-router';
import { useResultStore } from '@/stores/resultStore';

const router = useRouter();
const resultStore = useResultStore();

// Pagination variables
const isLoadingMore = ref(false);
const currentPage = ref(0);
const itemsPerPage = ref(10);
const hasMoreItems = ref(true);

const newItemsRequest = ref<ItemsRequestDTO>({
    category: null,
    searchWord: null,
    priceMinMax: null,
    sort: 'published_DESC',
    segmentOffset: [0, itemsPerPage.value],
});

/**
 * Initializes the component when mounted:
 * - Fetches initial data (categories and first page of items)
 * - Sets up scroll event listener for infinite scrolling
 */
onMounted(() => {
  resultStore.fetchCategories();
  resultStore.fetchItems(newItemsRequest.value);
  window.addEventListener('scroll', handleScroll);
});

/**
 * Cleans up resources when the component is unmounted:
 * - Removes scroll event listener to prevent memory leaks
 */
onUnmounted(() => {
  window.removeEventListener('scroll', handleScroll);
});

/**
 * Monitors user scroll position and triggers loading of more items when the user
 * approaches the bottom of the page (80% scroll threshold).
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
 */
async function loadMoreItems() {
  if (isLoadingMore.value || !hasMoreItems.value) return;

  isLoadingMore.value = true;
  currentPage.value++;

  try {
    const nextPageRequest: ItemsRequestDTO = {
      ...newItemsRequest.value,
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
 * Handles the search event emitted by the SearchBar component.
 * Navigates to the 'result' page with the search query.
 *
 * @param {string} query - The search query entered by the user
 */
function handleSearch(query: string) {
  router.push({ name: 'result', query: { search: query } });
}

/**
 * Handles the 'category-clicked' event emitted by the CategoryGrid component
 * @param {string} category - The name of the clicked category
 */
function handleCategoryClick(category: string) {
  router.push({ name: 'result', query: { category } });
}

/**
 * Handles the 'item-clicked' event emitted by the RecommendationGrid component
 * @param {number} itemID - The unique identifier of the clicked item
 */
function handleItemClick(itemID: number) {
  router.push({ name: 'product', query: { id: itemID } });
}
</script>

<template>
  <div class="home-container">
    <h1>Welcome to the Yard!</h1>
    <div class="search-category-container">
      <SearchBar @search-triggered="handleSearch"/>
      <CategoryGrid :categories="resultStore.categories" @category-clicked="handleCategoryClick"/>
    </div>
    <h3>Recommendations</h3>
    <RecommendationGrid :items="resultStore.items" @item-clicked="handleItemClick"/>
    <div v-if="isLoadingMore" class="loading-more">Loading more items...</div>
    <div v-if="!hasMoreItems && resultStore.items.length > 0" class="end-of-results">No more items to display</div>
  </div>
</template>

<style scoped>
.home-container {
  display: flex;
  padding: 32px 128px;
  flex-direction: column;
  align-items: center;
  gap: 32px;
  align-self: stretch;
}

.search-category-container {
  display: flex;
  padding: 32px 64px;
  flex-direction: column;
  gap: 16px;
  align-self: stretch;
}

h1 {
  font-weight: 600;
  text-align: center;
}

/* Add loading indicator styles */
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
  .home-container {
    padding: 32px;
  }

  .search-category-container {
    padding: 16px 32px;
  }
}
</style>
