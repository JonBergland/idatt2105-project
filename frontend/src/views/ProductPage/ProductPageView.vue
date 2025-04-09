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
import type { PlaceBid } from '@/models/bid';

const route = useRoute();
const router = useRouter();
const itemStore = useItemStore();
const authStore = useAuthStore();
const userStore = useUserStore();

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
  bookmark: false // Initialize with a default value
});

const error = ref('');
const bidResponse = ref('');

// Extract ID from route query params
const itemId = computed(() => {
  const id = route.query.id;
  return typeof id === 'string' ? parseInt(id, 10) : -1;
});

// Computed property to track bookmark status
const isBookmarked = computed(() => {
  return !!itemResponse.value?.bookmark;
});

const isMyItem = ref(false);
const isEditing = ref(false);

/**
 * Handles the event when the back button is clicked.
 * Uses the router to go back to previous page
 */
function handleBackClick() {
  router.back();
}


/**
 * Handles the logic for favoriting or unfavoriting a product.
 *
 * @param {boolean} isFavorited - A boolean indicating whether the product is currently favorited.
 *                                Pass `true` to unfavorite the product, or `false` to favorite it.
 * @returns {Promise<void>} A promise that resolves when the operation is complete.
 */
async function handleFavorite(isFavorited: boolean) {
  if (!authStore.isAuth) {
    router.push('/login');
    return;
  }

  try {
    // Update local state immediately for responsive UI
    itemResponse.value = {
      ...itemResponse.value,
      bookmark: isFavorited
    };

    // TODO: API call to toogle the favorite

    console.log(`Item ${itemId.value} bookmark status set to ${isFavorited}`);
  } catch (err) {
    console.error('Error updating bookmark status:', err);
    // Revert UI if API call fails
    itemResponse.value = {
      ...itemResponse.value,
      bookmark: !isFavorited
    };
  }
}

/**
 * Handles the bidding process for a product.
 *
 * @param {number} bid      The bid amount entered by the user.
 * @returns {Promise<void>} A promise that resolves when the bid is processed.
 */
async function handleBid(bid: number) {
  if (!bid || bid <= 0) {
    console.error('Invalid bid amount');
    bidResponse.value = 'Invalid bid amount';
    return;
  }

  if (!itemResponse.value || !itemResponse.value.itemID) {
    console.error('Cannot place bid: Invalid item data');
    bidResponse.value = 'Cannot place bid: Invalid item data';
    return;
  }

  try {
    const bidData: PlaceBid = {
      itemID: itemResponse.value.itemID,
      askingPrice: bid
    };

    const result = await userStore.giveBidOnItem(bidData);

    if (result > 0) {
      console.log(`Bid placed successfully: ${bid} kr`);
      bidResponse.value = `Bid placed successfully: ${bid} kr`;
    } else {
      console.error('Failed to place bid');
      bidResponse.value = 'Failed to place bid';
    }
  } catch (error) {
    console.error('Error placing bid:', error);
    bidResponse.value = 'Error placing bid:';
  }
}

/**
 * Handles the login click event.
 * This function is triggered when the user clicks the give bid button
 * without being logged in
 * @function handleLoginClick
 */
function handleLoginClick() {
  router.push({ name: "login" })
}

/**
 * Maps ItemResponseDTO to AddItemRequest format for editing
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
 * Handles updating an existing item
 */
 async function handleUpdateItem(updatedItem: AddItemRequest) {
  try {
    const updateRequest = {
      ...updatedItem,
      itemID: itemId.value
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
 * Fetches item details based on authentication status
 */
 async function fetchItemDetails() {
  if (itemId.value <= 0) {
    error.value = 'Invalid item ID';
    return;
  }

  try {
    const isAuthenticated = authStore.isAuth || await authStore.checkIfAuth();

    const request: ItemRequestDTO = { itemID: itemId.value };

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
          :isAuth="authStore.isAuth"
          :bidResponse="bidResponse"
          @bid="handleBid"
          @login="handleLoginClick"
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
