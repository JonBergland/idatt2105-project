<script setup lang="ts">
import placeholderImage from '@/assets/images/placeholder-image.png';
import "@/assets/color.css"
import "@/assets/base.css"
import "@/assets/main.css"

defineProps<{
  itemName: string;             // Name of the item that is being sold
  messagingContactRole: string; // Role of the chatter (Buyer or Seller of the product)
  messagingContactName: string; // The name of the person you are chatting with
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
      <h3 class="item-name">{{ itemName }}</h3>
      <!-- Name of contact -->
      <p class="messaging-contact-name">{{ messagingContactRole + ": " + messagingContactName }}</p>
    </div>
    <!-- Indicator for new message -->
    <div v-if="!seenByUser" class="new-message-indicator"></div>
  </div>
</template>

<style scoped>
.message-product-component {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 10px;
  border: 1px solid var(--color-border);
  border-radius: 8px;
  background-color: var(--color-background-soft);
  transition: background-color 0.3s ease, border-color 0.3s ease;
  cursor: pointer;
  position: relative;
}

.message-product-component.active {
  background-color: var(--color-background-muted);
  border-color: var(--color-border);
}

.message-product-image {
  flex-shrink: 0;
  width: 50px;
  height: 50px;
  border-radius: 4px;
  overflow: hidden;
  background-color: var(--color-background);
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

.new-message-indicator {
  width: 10px;
  height: 10px;
  border-radius: 50%;
  background-color: var(--color-trinary);
  position: absolute;
  right: 10px;
  top: 50%;
  transform: translateY(-50%);
}
</style>
