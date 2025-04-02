<script setup lang="ts">
import CategoryButton from '@/components/Home/CategoryButton.vue';

const props = defineProps({
  categories: {
    type: Array as () => string[],
    required: false,
  },
});

const emit = defineEmits(['category-clicked']);

/**
 * Emits the 'category-clicked' event to the parent component
 *
 * @param {string} category - The name of the clicked category
 */
function handleCategoryClick(category: string) {
  emit('category-clicked', category);
}
</script>

<template>
  <div class="category-grid">
    <CategoryButton
    v-for="(category, index) in props.categories"
    :key="index"
    :name="category"
    @clicked-category="handleCategoryClick"
    />
  </div>
</template>

<style scoped>
.category-grid {
  display: flex;
  justify-content: center;
  align-items: flex-start;
  align-content: flex-start;
  gap: 16px;
  align-self: stretch;
  flex-wrap: wrap;
}
</style>
