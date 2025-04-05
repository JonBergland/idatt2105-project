<script setup lang="ts">
import type { ItemsResponseDTO } from '@/models/item';
import ItemCard from '@/components/Home/ItemCard.vue';

const emit = defineEmits(['item-clicked']);

const props = defineProps({
  items: {
    type: Array as () => ItemsResponseDTO['items'],
    required: false,
  },
  mode: {
    type: String,
    default: 'Grid',
    validator: (value: string) => ['Grid', 'Column'].includes(value),
  },
});

/**
 * Emits the 'item-clicked' event to the parent component
 *
 * @param {number} itemId - The unique identifier of the clicked item.
 */
function handleItemClick(itemId: number) {
  emit('item-clicked', itemId);
}
</script>

<template>
  <div :class="['item-group', mode === 'Grid' ? 'item-grid' : 'item-column']">
    <ItemCard
      v-for="(item, index) in props.items"
      :key="index"
      :item="item"
      :size="mode === 'Grid' ? 'narrow' : 'full'"
      @clicked-item="handleItemClick"
    />
  </div>
</template>

<style scoped>
.item-group {
  width: 100%;
}

.item-grid {
  display: flex;
  justify-content: center;
  align-items: flex-start;
  align-content: flex-start;
  gap: 24px;
  flex-wrap: wrap;
}

.item-column {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

@media (max-width: 768px) {
  .item-grid {
    grid-template-columns: repeat(auto-fit, minmax(150px, 1fr));
  }
}
</style>
