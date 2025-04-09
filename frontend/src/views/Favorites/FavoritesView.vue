<script setup lang="ts">
import { ref, onMounted, onUnmounted } from 'vue';
import { useRouter } from 'vue-router';
import { useUserStore } from "@/stores/userStore";
import { useAuthStore } from "@/stores/authStore";
import ItemGroup from '@/components/Home/ItemGroup.vue';
import type { GetBookmarkedItemsRequest } from '@/models/user';

const router = useRouter();
const userStore = useUserStore();
const authStore = useAuthStore();

// Pagination variables
const isLoadingMore = ref(false);
const currentPage = ref(0);
const itemsPerPage = ref(10);
const hasMoreItems = ref(true);

const request = ref<GetBookmarkedItemsRequest>({
    segmentOffset: [0, itemsPerPage.value],
});

/**
 * Handles the 'item-clicked' event emitted by the RecommendationGrid component
 * @param {number} itemID - The unique identifier of the clicked item
 */
function handleItemClick(itemID: number) {
  router.push({ name: 'product', query: { id: itemID } });
}

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
 * Loads the next page of bookmarked items by incrementing the current page counter
 * and making a request to the API for more items.
 */
async function loadMoreItems() {
  if (isLoadingMore.value || !hasMoreItems.value) return;

  isLoadingMore.value = true;
  currentPage.value++;

  try {
    const nextPageRequest: GetBookmarkedItemsRequest = {
      ...request.value,
      segmentOffset: [currentPage.value * itemsPerPage.value, itemsPerPage.value]
    };

    await userStore.loadMoreBookmarkedItems(nextPageRequest);

    if (userStore.newBookmarkedItemsCount < itemsPerPage.value) {
      hasMoreItems.value = false;
    }
  } catch (error) {
    console.error('Failed to load more favorites:', error);
  } finally {
    isLoadingMore.value = false;
  }
}

/**
 * Checks if the user is authenticated when the component is mounted.
 * If not authenticated the user is pushed to the login page
 */
onMounted(async () => {
  if (!authStore.isAuth) {
    try {
      const isAuthenticated = await authStore.checkIfAuth();
      if (!isAuthenticated) {
        router.push({ name: 'login' });
        return;
      }
    } catch (error) {
      console.error("Error checking authentication:", error);
      router.push({ name: 'login' });
      return;
    }
  }

  try {
    await userStore.fetchBookmarkedItems(request.value);
    console.log("Bookmarked items:", userStore.bookmarkedItems);

    // Set up scroll event listener for infinite scrolling
    window.addEventListener('scroll', handleScroll);
  } catch (error) {
    console.error("Error fetching bookmarked items:", error);
  }
});

/**
 * Cleans up resources when the component is unmounted:
 * - Removes scroll event listener to prevent memory leaks
 */
onUnmounted(() => {
  window.removeEventListener('scroll', handleScroll);
});
</script>

<template>
  <div class="favorites-container">
    <h1>Your favorites</h1>
    <div v-if="!userStore.bookmarkedItems || userStore.bookmarkedItems.length === 0" class="empty-state">
      <p>You haven't added any favorites yet.</p>
      <p>Browse items and click the heart icon to add them to your favorites.</p>
    </div>

    <div v-else>
      <ItemGroup
        :items="userStore.bookmarkedItems"
        @item-clicked="handleItemClick"
      />

      <div v-if="isLoadingMore" class="loading-more">
        Loading more favorites...
      </div>

      <div v-if="!hasMoreItems && userStore.bookmarkedItems.length > 0" class="end-of-results">
        No more favorites to display
      </div>
    </div>
  </div>
</template>

<style scoped>
.favorites-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  text-align: center;
  padding: 32px;
  gap: 16px;
}

.empty-state {
  margin: 40px 0;
  padding: 20px;
  border-radius: 8px;
  background-color: var(--color-background-soft, #f8f9fa);
  max-width: 500px;
  text-align: center;
}

.empty-state p {
  margin: 10px 0;
  color: var(--color-text-secondary, #6c757d);
}

.empty-state p:first-child {
  font-size: 1.2rem;
  font-weight: 500;
  color: var(--color-text, #212529);
}

.loading-more {
  text-align: center;
  padding: 20px 0;
  color: #6c757d;
  width: 100%;
}

.end-of-results {
  text-align: center;
  padding: 20px 0;
  color: #888;
  width: 100%;
}
</style>
