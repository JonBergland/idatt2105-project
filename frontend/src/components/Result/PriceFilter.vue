<script setup lang="ts">
import { ref } from 'vue';

const minPrice = ref<string | null>(null);
const maxPrice = ref<string | null>(null);

const isRangeValid = ref(false);
const isNumberNegative = ref(false);


const emit = defineEmits(['price-range-updated']);

/**
 * Handles changes to the price filter input.
 * This function is triggered when the user modifies the price range,
 * It checkes if the input is valid and emits 'price-range-updated'
 * If not valid it an error message apears
 */
function handlePriceChange() {
  isNumberNegative.value = false;

  if (minPrice.value === '') {
    minPrice.value = null;
  }
  if (maxPrice.value === '') {
    maxPrice.value = null;
  }

  const min = minPrice.value ? parseFloat(minPrice.value) : null;
  const max = maxPrice.value ? parseFloat(maxPrice.value) : null;

  if (min !== null && max !== null && min > max) {
    isRangeValid.value = true;
  } else {
    isRangeValid.value = false;
  }

  if (min !== null && min < 0) {
    isNumberNegative.value = true;
  }

  if (max !== null && max < 0) {
    isNumberNegative.value = true;
  }

  if (isNumberNegative.value === false && isRangeValid.value === false) {
    emit('price-range-updated', { min: min, max: max });
  }
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
  <p v-if="isRangeValid" class="error-message">Max price must be lower than min price</p>
  <p v-if="isNumberNegative" class="error-message">Number cant be negative</p>
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

.error-message {
  color: red;
  font-size: 0.9em;
  margin: 0;
}
</style>
