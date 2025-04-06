<script setup lang="ts">
import type { ItemResponseDTO, ItemRequestDTO } from '@/models/item';
import ProductImageComponent from '@/components/ProductPage/ProductImageComponent.vue';
import ProductNameComponent from '@/components/ProductPage/ProductNameComponent.vue'
import placeholderImage from '@/assets/images/placeholder-image.png'
import { onMounted, ref,  } from 'vue';
import { useRoute, useRouter } from 'vue-router'
import ProductInfoComponent from '@/components/ProductPage/ProductInfoComponent.vue';
import { useResultStore } from '@/stores/resultStore';

const route = useRoute()
const router = useRouter()
const resultStore = useResultStore();

const itemRequest = ref<ItemRequestDTO>({itemID: -1})

function handleBackClick() {
  router.back();
}

function handleFavorite(isFavorited: boolean) {
  console.log("Favorite clicked:", isFavorited);

  // TODO: Add functionality for favorite to backend
}

onMounted(() => {
  if (route.query.id) {
    console.log('Product: ', route.query.id)
    itemRequest.value.itemID = typeof route.query.id === 'string' ? parseInt(route.query.id, 10) : -1;
    resultStore.fetchItemDetails(itemRequest.value);
  }
})

</script>

<template>
  <div v-if="!resultStore.isItemLoading" class="product-page-wrapper">
    <ProductNameComponent
    :product-name="resultStore.item?.name || ''"
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
        :item="resultStore.item || {} as ItemResponseDTO"
        />
      </div>
    </div>
  </div>

</template>

<style scoped>

.product-page-wrapper {
  margin: 10px;
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
