<script setup lang="ts">
import type { ItemsRequestDTO } from '@/models/item';
import CategoryGrid from '@/components/Home/CategoryGrid.vue';
import RecommendationGrid from '@/components/Home/ItemGroup.vue';
import SearchBar from '@/components/Home/SearchBar.vue';
import { onMounted, ref } from 'vue';
import { useRouter } from 'vue-router';
import { useResultStore } from '@/stores/resultStore';

const router = useRouter();

const resultStore = useResultStore();

const newItemsRequest = ref<ItemsRequestDTO>({
    category: null,
    searchWord: null,
    priceMinMax: null,
    sort: 'published_DESC',
    segmentOffset: [0, 10],
});

onMounted(() => {
  resultStore.fetchCategories();
  resultStore.fetchItems(newItemsRequest.value);
})

/**
 * Handles the search event emitted by the SearchBar component.
 * Navigates to the 'result' page with the search query.
 *
 * @param {string} query - The search query entered by the user
 */
 function handleSearch(query: string) {
  router.push({ name: 'result', query: { search: query } });
  console.log('Searched for: ', query)
}

/**
 * Handles the 'category-clicked' event emitted by the CategoryGrid component
 * @param {string} category - The name of the clicked category
 */
 function handleCategoryClick(category: string) {
  router.push({ name: 'result', query: { category } });
  console.log('Clicked Category: ', category)
}

/**
 * Handles the 'item-clicked' event emitted by the RecommendationGrid component
 * @param {number} itemId - The unique identifier of the clicked item
 */
 function handleItemClick(itemId: number) {
  router.push({ name: 'items', params: { id: itemId } });
  console.log('Clicked Item: ', itemId)
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

@media (max-width: 768px) {
  .home-container {
    padding: 32px;
  }

  .search-category-container {
    padding: 16px 32px;
  }
}
</style>
