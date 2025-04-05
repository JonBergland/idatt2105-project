<script setup lang="ts">
const props = defineProps({
  name: {
    type: String,
    required: true,
  },
});

const emit = defineEmits(['clicked-category'])

/**
 * Handles the click event for the categoryButton
 * Emits 'clicked-category' used to naviget to the results page
 */
function handleClick() {
  emit('clicked-category', props.name);
}
</script>

<template>
  <button class="category-button" @click="handleClick">{{ name }}</button>
</template>

<style scoped>
.category-button {
  padding: 10px 20px;
  border: 1px solid #ccc;
  border-radius: 24px;
  background-color: white;
  cursor: pointer;
  font-size: 0.9rem;
  transition: background-color 0.3s ease;
  max-width: 120px;
  white-space: nowrap;
}

.category-button:hover {
  background-color: #f0f0f0;
}
</style>
