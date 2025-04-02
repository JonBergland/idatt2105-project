<script setup lang="ts">
import ItemCard from '@/components/Home/ItemCard.vue';

const emit = defineEmits(['item-clicked']);

const props = defineProps({
  items: {
    type: Array as () => Record<string, any>[],
    required: false,
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
  <div class="recommendations-grid">
    <ItemCard
    v-for="(item, index) in props.items"
    :key="index"
    :item="item"
    @clicked-item="handleItemClick"
    />
  </div>
</template>

<style scoped>
.recommendations-grid {
  display: flex;
  justify-content: center;
  align-items: flex-start;
  align-content: flex-start;
  gap: 24px;
  flex: 1 0 0;
  flex-wrap: wrap;
}

@media (max-width: 768px) {
  .recommendations-grid {
    grid-template-columns: repeat(auto-fit, minmax(150px, 1fr));
  }

  .item-card {
    width: 150px;
  }
}
</style>
