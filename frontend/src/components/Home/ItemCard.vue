<script setup lang="ts">
const props = defineProps({
  item: {
    type: Object,
    required: true,
  },
});

const emit = defineEmits(['clicked-item'])

/**
 * Handles the click event for the item card.
 * Emits 'clicked-item' used to navigate to a product page
 */
function handleClick() {
  emit('clicked-item', props.item.id)
}
</script>

<template>
  <div class="item-card" @click="handleClick">
    <img :src="item.img" :alt="item.alt" draggable="false" class="item-image">
    <p>{{ item.location }}</p>
    <h3>{{ item.name  }}</h3>
    <p>{{ item.price }} kr</p>
  </div>
</template>

<style scoped>
.item-card {
  display: flex;
  width: 218px;
  padding: 16px;
  flex-direction: column;
  align-items: flex-start;
  gap: 16px;
  border-radius: 8px;
  border: 1px solid #D9D9D9;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.item-card:hover {
  background-color: #f0f0f0;
}

.item-image {
  width: 100%;
  height: auto;
  border-radius: 8px;
  object-fit: cover;
}
</style>
