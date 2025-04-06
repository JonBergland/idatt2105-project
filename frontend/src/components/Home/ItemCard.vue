<script setup lang="ts">
import type { ItemResponseDTO } from '@/models/item';
import placholderImage from '@/assets/images/placeholder-image.png'

const props = defineProps({
  item: {
    type: Object as () => ItemResponseDTO,
    required: true,
  },
  size: {
    type: String,
    default: 'narrow',
    validator: (value: string) => ['narrow', 'full'].includes(value),
  },
});

const emit = defineEmits(['clicked-item'])

/**
 * Handles the click event for the item card.
 * Emits 'clicked-item' used to navigate to a product page
 */
function handleClick() {
  emit('clicked-item', props.item.itemID)
}
</script>

<template>
  <div class="item-card"  :class="size === 'narrow' ? 'narrow' : 'full'" @click="handleClick">
    <img :src="placholderImage" alt="" draggable="false" class="item-image" :class="size === 'narrow' ? 'narrow' : 'full'">
    <div class="item-info">
      <p>{{ item.seller }}</p>
      <h3>{{ item.name  }}</h3>
      <p>{{ item.price }} kr</p>
    </div>
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

.item-card.full {
  flex-direction: row;
  align-items: flex-start;
  gap: 16px;
  width: 100%; /* Allow the card to scale down responsively */
  max-width: 702px; /* Standard maximum width */
  min-width: 460px; /* Minimum width for better readability */
  flex-grow: 1;
}

.item-image {
  width: 100%;
  height: auto;
  border-radius: 8px;
  object-fit: cover;
}

.item-image.full {
  width: auto;
}


@media (max-width: 768px) {
  .item-image {
    max-width: 116px;
    max-height: 116px;
  }

  .item-card.narrow {
    width: 150px
  }
  .item-card.full{
    width: 300px;
  }
}

</style>
