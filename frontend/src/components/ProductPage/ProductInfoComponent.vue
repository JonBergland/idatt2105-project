<script setup lang="ts">
import type { ItemResponseDTO } from '@/models/item';
import { computed, ref } from 'vue';
import "@/assets/color.css"
import "@/assets/base.css"
import "@/assets/main.css"

const emit = defineEmits<{
  (e: 'bid', value: number): void
  (e: 'login'): void
}>();

const errorMessage = ref('');
const bidValue = ref<string>('');

const props = defineProps<{
  item: ItemResponseDTO,
  isAuth: boolean
}>();

const itemStatus = computed(() => {
  const state = props.item.state
  switch (state) {
    case 'available':
      return 'Available';
    case 'archived':
      return 'Not Available: Archived';
    case 'reserved':
      return 'Reserved';
    case 'sold':
      return 'Not Available: Sold';
    default:
      return 'Unknown';
  }
});

function handleBid(): void {
  const bidAmount = parseFloat(bidValue.value);

  if (!bidValue.value || isNaN(bidAmount) || bidAmount <= 0) {
    errorMessage.value = 'Please enter a valid bid amount';
    return;
  }

  errorMessage.value = '';
  emit('bid', bidAmount);

  bidValue.value = '';
}
</script>

<template>
  <div class="product-info-container">
    <h3 class="product-center-header"><strong>{{ itemStatus }}</strong></h3>
    <h3 class="product-center-header"><strong>{{ props.item.price }} kr</strong></h3>
    <div v-if="props.isAuth" class="product-buttons">
      <button class="give-bid-button" @click="handleBid">Give a bid</button>
      <button class="vipps-button" disabled>Pay with vipps</button>
    </div>
    <div v-else class="product-buttons">
      <button class="give-bid-button" @click="$emit('login')">Give a bid</button>
      <button class="vipps-button" disabled>Pay with vipps</button>
    </div>
    <div v-if="props.isAuth" class="bid-input-container">
      <input
        id="bid"
        type="number"
        v-model="bidValue"
        placeholder="Type your bid in kr here:"
      >
      <p v-if="errorMessage" class="error-message">{{ errorMessage }}</p>
    </div>
    <div class="product-information-box">
      <h3><strong>Product information:</strong></h3>
      <p><strong>Published:</strong> {{ props.item.published}}</p>
      <p><strong>Category:</strong> {{ props.item.category}}</p>
      <p><strong>Description:</strong> {{ props.item.description}}</p>
      <h3><strong>Seller information:</strong></h3>
      <p><strong>Location:</strong></p>
      <p><strong>Name of seller:</strong> {{ props.item.seller }}</p>
    </div>
  </div>
</template>

<style scoped>
.product-info-container {
  display: flex;
  flex-direction: column;
  align-items: left;
  gap: 20px;
  width: 100%;
}

.product-center-header {
  margin: 0;
  text-align: center;
  position: relative;
  transform: translateX(-33%);
}

.product-buttons {
  display: flex;
  gap: 20px;
  margin-top: 10px;
}

.give-bid-button,
.vipps-button {
  display: flex;
  width: 120px;
  height: 40px;
  justify-content: center;
  align-items: center;
  border: none;
}

.give-bid-button {
  background-color: var(--color-primary);
  color: white;
}

.give-bid-button:hover {
  background-color: var(--color-primary-hover);
}

.vipps-button {
  background-color: var(--color-vipps);
  color: white;
}

.vipps-button:hover {
  background-color: var(--color-vipps-hover);
}

.vipps-button:disabled {
  background-color: var(--color-vipps-disabled);
}

.product-information-box {
  display: flex;
  flex-direction: column;
  text-align: left;
  width: 100%;
  max-width: 400px;
  gap: 10px;
}

.product-information-box h2 {
  margin: 10px 0;
}

.product-information-box p {
  margin: 5px 0;
}

strong {
  font-weight: bold;
}

/* Add these styles for the error message */
.error-message {
  color: red;
  font-size: 0.9rem;
  margin-top: 5px;
}

.bid-input-container {
  display: flex;
  flex-direction: column;
  width: 100%;
  max-width: 400px;
}

/* Rest of your existing styles remain the same */
</style>
