<script setup lang="ts">
import type { ItemResponseDTO, ItemRequestDTO } from '@/models/item';
import type { AddItemRequest } from '@/models/user';
import ProductImageComponent from '@/components/ProductPage/ProductImageComponent.vue';
import ProductNameComponent from '@/components/ProductPage/ProductNameComponent.vue'
import placeholderImage from '@/assets/images/placeholder-image.png'
import { onMounted, ref, computed } from 'vue';
import { useRoute, useRouter } from 'vue-router'
import ProductInfoComponent from '@/components/ProductPage/ProductInfoComponent.vue';
import { useItemStore } from '@/stores/itemStore';
import { useAuthStore } from '@/stores/authStore';
import { useUserStore } from '@/stores/userStore';
import ItemForm from '@/components/NewListing/ItemForm.vue';

const route = useRoute();
const router = useRouter();
const itemStore = useItemStore();
const authStore = useAuthStore();
const userStore = useUserStore();

const isMyItem = ref(false);
const isEditing = ref(false);

const itemResponse = ref<ItemResponseDTO>({
  itemID: undefined,
  name: '',
  category: '',
  sellerID: undefined,
  seller: '',
  description: '',
  published: '',
  price: undefined,
  state: '',
  bookmark: false
});
const error = ref('');

const itemID = computed(() => {
  const id = route.query.id;
  return typeof id === 'string' ? parseInt(id, 10) : -1;
});

const isBookmarked = computed(() => {
  return !!itemResponse.value?.bookmark;
});


/**
 * Handles the click event for the back button.
 * Navigates the user back to the previous page or view.
 */
function handleBackClick() {
  router.back();
}

/**
 * Handles the logic for favoriting or unfavoriting a product.
 *
 * @param {boolean} isFavorited - A boolean indicating whether the product is currently favorited.
 *                                Pass `true` to mark the product as favorited, or `false` to unfavorite it.
 */
async function handleFavorite(isFavorited: boolean) {
  if (!authStore.isAuth) {
    router.push('/login');
    return;
  }

  try {
    itemResponse.value = {
      ...itemResponse.value,
      bookmark: isFavorited
    };

    const toggleBookmarkRequest = {
      itemID: itemID.value
    }

    await userStore.toggleBookmark(toggleBookmarkRequest)

  } catch (err) {
    console.error('Error updating bookmark status:', err);
    itemResponse.value = {
      ...itemResponse.value,
      bookmark: !isFavorited
    };
  }
}

/**
 * Maps an ItemResponseDTO object to an AddItemRequest object.
 *
 * @param {ItemResponseDTO} item - The item response data transfer object to be mapped.
 * @returns {AddItemRequest} - The resulting AddItemRequest object after mapping.
 */
 function mapItemResponseToAddItemRequest(item: ItemResponseDTO): AddItemRequest {
  if (!item) {
    return {
      name: '',
      description: '',
      price: 0,
      category: ''
    };
  }
  return {
    name: item.name || '',
    description: item.description || '',
    price: item.price || 0,
    category: item.category || ''
  };
}


/**
 * Handles the update of an item in the product page.
 *
 * @param {AddItemRequest} updatedItem - The updated item data to be processed.
 */
 async function handleUpdateItem(updatedItem: AddItemRequest) {
  try {
    const updateRequest = {
      ...updatedItem,
      itemID: itemID.value
    };

    await userStore.updateItemDetails(updateRequest);

    itemResponse.value = {
      ...itemResponse.value,
      ...updatedItem
    };

    isEditing.value = false;

    console.log('Item updated:', updateRequest);
  } catch (error) {
    console.error('Error updating item:', error);
  }
}


/**
 * Fetches the details of a specific item.
 *
 * Checks if the user is authenticated or not, and fetches items
 * from different stores thereafter.
 */
 async function fetchItemDetails() {
  if (itemID.value <= 0) {
    error.value = 'Invalid item ID';
    return;
  }

  try {
    const isAuthenticated = authStore.isAuth || await authStore.checkIfAuth();

    const request: ItemRequestDTO = { itemID: itemID.value };

    if (isAuthenticated) {
      await userStore.fetchUserItemDetails(request);
      itemResponse.value = {
        ...userStore.item,
        bookmark: userStore.item?.bookmark || false
      };
      await userStore.getUserInfo();
      if (userStore.user?.email === userStore.item?.seller) {
        isMyItem.value = true;
      }
    } else {
      await itemStore.fetchItemDetails(request);
      itemResponse.value = {
        ...itemStore.item,
        bookmark: false
      };
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

  <div v-else-if="isEditing" class="edit-listing-container">
    <h1>Edit your listing</h1>
    <ItemForm
      :existingItem="mapItemResponseToAddItemRequest(itemResponse)"
      @submit="handleUpdateItem"
    />
    <button class="cancel-button" @click="isEditing = false">Cancel</button>
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
          :isFavorited="isBookmarked"
          @favorite="handleFavorite"
        />
      </div>
      <div class="product-info-component-wrapper">
        <ProductInfoComponent
          :item="itemResponse"
        />
      </div>
    </div>
    <div v-if="isMyItem" class="my-listing-container">
      <h3>This is your listing</h3>
      <button class="edit-button" @click="isEditing = true">Edit listing</button>
    </div>
  </div>
</template>

<style scoped>
.product-page-wrapper {
  display: flex;
  margin: 10px;
  flex-direction: column;
  gap: 32px;
  padding: 16px;
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

.edit-button {
  width: fit-content;
  align-self: center;
}

.edit-listing-container {
  display: flex;
  flex-direction: column;
  gap: 16px;
  padding: 32px;
}

.cancel-button {
  margin-top: 16px;
  padding: 8px 16px;
  background-color: var(--color-background-mute);
  border: 1px solid var(--color-border);
  border-radius: 4px;
  cursor: pointer;
  width: fit-content;
  align-self: center;
}
.edit-button {
  padding: 8px 16px;
  background-color: var(--color-background-mute);
  border: 1px solid var(--color-border);
  border-radius: 4px;
  cursor: pointer;
  width: fit-content;
  align-self: center;
}

.edit-button:hover {
  background-color: #eee;
}

.cancel-button:hover {
  background-color: #eee;
}

.my-listing-container {
  display: flex;
  flex-direction: column;
  flex-wrap: nowrap;
  text-align: center;
  gap: 16px;
}

.my-listing-container h3{
  background-color: var(--color-secondary);
  color: var(--color-text-secondary);
  padding: 8px;
  border-radius: 8px;
  display: inline-block;
  white-space: nowrap;
  width: fit-content;
  align-self: center;
}
</style>
