<script setup lang="ts">
import { ref, onMounted, computed } from 'vue';
import { useRouter } from 'vue-router';
import { useUserStore } from "@/stores/userStore";
import { useAuthStore } from "@/stores/authStore";
import ItemGroup from '@/components/Home/ItemGroup.vue';
import type { GetBookmarkedItemsRequest } from '@/models/user';

const router = useRouter();
const userStore = useUserStore();
const authStore = useAuthStore();
const loading = ref(true); // Add loading state

const itemsPerPage = ref(10);

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
 * Checks if the user is authenticated when the component is mounted.
 * If not authenticated the user is pushed to the login page
 */
onMounted(async () => {
  loading.value = true; // Set loading state

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
  } catch (error) {
    console.error("Error fetching bookmarked items:", error);
  } finally {
    loading.value = false;
  }
});
</script>

<template>
  <div class="favorites-container">
    <h1>Your favorites</h1>

    <div v-if="loading" class="loading-indicator">
      Loading your favorites...
    </div>

    <div v-else-if="!userStore.bookmarkedItems || userStore.bookmarkedItems.length === 0" class="empty-state">
      <p>You haven't added any favorites yet.</p>
      <p>Browse items and click the heart icon to add them to your favorites.</p>
    </div>

    <ItemGroup
      v-else
      :items="userStore.bookmarkedItems"
      @item-clicked="handleItemClick"
    />
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

.loading-indicator {
  margin-top: 10px;
  color: #6c757d;
  font-style: italic;
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
</style>
