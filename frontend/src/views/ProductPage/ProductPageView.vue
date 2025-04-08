<script setup lang="ts">
import type { ItemResponseDTO, ItemRequestDTO } from '@/models/item';
import ProductImageComponent from '@/components/ProductPage/ProductImageComponent.vue';
import ProductNameComponent from '@/components/ProductPage/ProductNameComponent.vue'
import placeholderImage from '@/assets/images/placeholder-image.png'
import { onMounted, ref, computed } from 'vue';
import { useRoute, useRouter } from 'vue-router'
import ProductInfoComponent from '@/components/ProductPage/ProductInfoComponent.vue';
import { useItemStore } from '@/stores/itemStore';
import { useAuthStore } from '@/stores/authStore';
import { useUserStore } from '@/stores/userStore';

const route = useRoute()
const router = useRouter()
const itemStore = useItemStore();
const authStore = useAuthStore();
const userStore = useUserStore();

const itemResponse = ref<ItemResponseDTO>({});
const error = ref('');

// Extract ID from route query params
const itemId = computed(() => {
  const id = route.query.id;
  return typeof id === 'string' ? parseInt(id, 10) : -1;
});

function handleBackClick() {
  router.back();
}

function handleFavorite(isFavorited: boolean) {
  console.log("Favorite clicked:", isFavorited);
  // TODO: Add functionality for favorite to backend
}

/**
 * Fetches item details based on authentication status
 */
async function fetchItemDetails() {
  if (itemId.value <= 0) {
    error.value = 'Invalid item ID';
    return;
  }

  try {

    // Ensure auth state is current
    const isAuthenticated = authStore.isAuth || await authStore.checkIfAuth();

    // Create item request object
    const request: ItemRequestDTO = { itemID: itemId.value };

    if (isAuthenticated) {
      // Fetch with user context
      await userStore.fetchUserItemDetails(request);
      itemResponse.value = userStore.item || {};
    } else {
      // Fetch without user context
      await itemStore.fetchItemDetails(request);
      itemResponse.value = itemStore.item || {};
    }
  } catch (err) {
    console.error('Error fetching item details:', err);
    error.value = 'Failed to load item details';
  }
}

onMounted(fetchItemDetails);
</script>

<template>

  <div v-if="error" class="error-state">
    {{ error }}
  </div>

  <div v-else class="product-page-wrapper">
    <ProductNameComponent
      :product-name="itemResponse?.name || ''"
      @back-click="handleBackClick"
    />
    <div class="product-info-wrapper">
      <div class="product-image-wrapper">
        <ProductImageComponent
          id="product-image-component"
          :images="[placeholderImage, placeholderImage]"
          @favorite="handleFavorite"
        />
      </div>
      <div class="product-info-component-wrapper">
        <ProductInfoComponent
          :item="itemResponse"
        />
      </div>
    </div>
  </div>
</template>

<style scoped>
.product-page-wrapper {
  margin: 10px;
}

.error-state {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 300px;
  font-size: 1.2rem;
  color: var(--color-text-secondary);
}

.error-state {
  color: var(--color-error, #dc3545);
}

.product-info-wrapper {
  display: flex;
  gap: 20px;
  align-items: flex-start;
  width: 100%;
  margin-top: 20px;
}

.product-image-wrapper {
  flex: 1;
  display: flex;
  justify-content: center;
  align-items: center;
  max-width: 50%;
}

.product-image-wrapper > * {
  width: 60%;
}

.product-info-component-wrapper {
  flex: 1;
  max-width: 50%;
}
</style>
