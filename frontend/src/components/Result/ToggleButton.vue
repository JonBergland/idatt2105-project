<script setup lang="ts">
const props = defineProps({
  name: {
    type: String,
    required: true,
  },
  selected: {
    type: Boolean,
    required: true,
  },
});

const emit = defineEmits(['clicked-toggle'])

/**
 * Handles the click event for the categoryButton
 * Emits 'clicked-category' used to naviget to the results page
 */
function handleClick() {
  emit('clicked-toggle', props.name);
}
</script>

<template>
  <div
  class="toggle-button"
  :class="{ selected: props.selected}"
  @click="handleClick"
  >
    {{ name }}
</div>
</template>

<style scoped>
.toggle-button {
  background-color: #F5F5F5;
  padding: 8px;
  border-radius: 8px;
  cursor: pointer;
  display: inline-block;
  width: auto;
  white-space: nowrap;
  color: #2c3e50;
}

.toggle-button:hover {
  background-color: #f0f0f0;
}

.toggle-button.selected {
  background-color: #0D5E01;
  color: white;
}
</style>
