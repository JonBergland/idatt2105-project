<script setup lang="ts">
import { ref } from 'vue';

// Define the minimum and maximum price range
const minPrice = ref<number | null>(null);
const maxPrice = ref<number | null>(null);

// Emit the selected price range to the parent component
const emit = defineEmits(['price-range-updated']);

/**
 * Handles the change in price range
 */
function handlePriceChange() {
  emit('price-range-updated', { min: minPrice.value, max: maxPrice.value });
}
</script>

<template>
  <div class="price-filter">
    <div class="input-wrapper">
      <input
        type="number"
        v-model="minPrice"
        @input="handlePriceChange"
        placeholder="Min"
        class="price-input"
      />
      <p>From kr</p>
    </div>
    <div class="input-wrapper">
      <input
        type="number"
        v-model="maxPrice"
        @input="handlePriceChange"
        placeholder="Max"
        class="price-input"
      />
      <p>To kr</p>
    </div>
  </div>
</template>

<style scoped>

.price-filter {
  display: flex;
  align-items: fle;
  gap: 8px;
  padding-top: 8px;
}

.input-wrapper {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.input-wrapper p {
  font-size: 14px;
  padding-left: 8px;
}

.price-input {
  width: 100%;
  padding: 8px;
  border: 1px solid #ccc;
  border-radius: 4px;
  font-size: 14px;
}

.price-input:focus {
  outline: 1px solid #D9D9D9;
}
</style>
