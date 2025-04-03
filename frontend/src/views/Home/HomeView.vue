<script setup lang="ts">
import CategoryGrid from '@/components/Home/CategoryGrid.vue';
import RecommendationGrid from '@/components/Home/ItemGrid.vue';
import SearchBar from '@/components/Home/SearchBar.vue';
import { onMounted, ref } from 'vue';
import { useRouter } from 'vue-router';
import placeholderImage from '@/assets/placeholder-image.png'

const router = useRouter();

const items = ref([
  {id: 1, name: 'Playstation 5', location: 'oslo', price: 400, img: placeholderImage},
  {id: 2, name: 'Playst3', location: 'bærum', price: 20, img: placeholderImage},
  {id: 3, name: 'Playstation 1', location: 'asker', price: 20000, img: placeholderImage},
  ])
const categories = ref([
  'category 1',
  'category 2',
  'category 3',
])

/**
 * Fetches the categories from service
 */
 async function loadCategories() {
  try {
    //TODO: implement call from service to fetch categories
  } catch (error) {
    console.error('Failed to fetch categories:', error);
  }
}

/**
 * Fetches the recommended items from service
 */
async function loadRecommendations() {
  try {
    //TODO: implement call from service to fetch items
  } catch (error) {
    console.error('Failed to fetch recommendations:', error);
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
  //router.push({ name: 'item', params: { id: itemId } });
  console.log('Clicked Item: ', itemId)
}

onMounted(() => {
  loadCategories();
  loadRecommendations();
});
</script>

<template>
  <div class="home-container">
    <h1>Welcome to the Yard!</h1>
    <div class="search-category-container">
      <SearchBar @search="handleSearch"/>
      <CategoryGrid :categories="categories" @category-clicked="handleCategoryClick"/>
    </div>
    <h3>Recommendations</h3>
    <RecommendationGrid :items="items" @item-clicked="handleItemClick"/>
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
