<script setup lang="ts">
import placeholderImage from '@/assets/images/placeholder-image.png';

const props = defineProps<{
  itemName: string;
  sellerName: string;
  productImage: string;
  isActive: boolean;
  seenByUser: boolean;
}>();
</script>

<template>
  <div
    class="message-product-component"
    :class="{ active: isActive }"
  >
    <div class="message-product-image">
      <img :src="productImage || placeholderImage" alt="product image" />
    </div>
    <div class="message-product-info">
      <!-- Name of item -->
      <h2 class="item-name">{{ itemName }}</h2>
      <!-- Name of seller -->
      <h3 class="seller-name">Seller: {{ sellerName }}</h3>
    </div>
  </div>
</template>

<style scoped>
.message-product-component {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 10px;
  border: 1px solid #d9d9d9;
  border-radius: 8px;
  background-color: #f9f9f9;
  transition: background-color 0.3s ease, border-color 0.3s ease;
  cursor: pointer;
}

.message-product-component.active {
  background-color: #e6e6e6; /* Darker background for active state */
  border-color: #b3b3b3; /* Highlighted border for active state */
}

.message-product-image {
  flex-shrink: 0;
  width: 50px;
  height: 50px;
  border-radius: 4px;
  overflow: hidden;
  background-color: #f0f0f0;
}

.message-product-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.message-product-info {
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.item-name {
  font-size: 16px;
  font-weight: 500;
  margin: 0;
  color: #333;
}

.seller-name {
  font-size: 14px;
  font-weight: 400;
  margin: 0;
  color: #666;
}
</style>
