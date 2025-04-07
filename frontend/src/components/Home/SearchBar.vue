<script setup lang="ts">
import { ref, watch } from 'vue';

const props = defineProps({
  modelValue: {
    type: String,
    default: ''
  }
});

const emit = defineEmits(['search-triggered', 'search-input', 'update:modelValue']);

const searchInput = ref('');

watch(() => props.modelValue, (newValue) => {
  searchInput.value = newValue;
});

/**
 * Emits the search query to the parent component
 */
 function handleSearchTriggered() {
  emit('search-triggered', searchInput.value);
}

function handleSearchInput() {
  emit('search-input', searchInput.value)
}
</script>

<template>
  <div class="search-bar">
    <div class="input-container ">
      <input
      type="text"
      v-model="searchInput"
      @input="handleSearchInput"
      @keyup.enter="handleSearchTriggered"
      placeholder="Search for anything..." />
      <button @click="handleSearchTriggered">
        <img src="@/assets/icons/search.svg" alt="Search">
      </button>
    </div>
  </div>
</template>

<style scoped>
.search-bar {
  display: flex;
  align-self: stretch;
}

.input-container {
  width: 100%;
  position: relative;
}

.input-container input {
  width: 100%;
  padding: 10px 40px 10px 20px;
  border: 1px solid #ccc;
  border-radius: 24px;
  font-size: 1rem;
}

.input-container button {
  position: absolute;
  top: 50%;
  right: -10px;
  transform: translateY(-50%);
  background-color: transparent;
  border: none;
  cursor: pointer;
}

.input-container button img {
  width: 16px;
  height: 16px;
}
</style>
