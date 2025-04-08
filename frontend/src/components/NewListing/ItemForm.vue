<script setup lang="ts">
import type { AddItemRequest } from '@/models/item.ts';
import { ref, onMounted } from 'vue';
import { useItemStore } from '@/stores/itemStore.ts'
import ToggleGroup from '@/components/Result/ToggleGroup.vue';

const itemStore = useItemStore();

const emit = defineEmits<{
  submit: [item: AddItemRequest]
}>();

const item = ref<AddItemRequest>({
  name: '',
  description: '',
  price: 0,
  category: ''
});

function handleCategoryClick(categoryName: string) {
  item.value.category = categoryName;
}

function submitForm() {
  // Emit the current form data to parent
  emit('submit', item.value);
}

onMounted(() => {
  itemStore.fetchCategories();
})
</script>

<template>
  <form @submit.prevent="submitForm" class="item-form">
    <div class="form-group">
      <label for="name">Item Name:</label>
      <input type="text" id="name" v-model="item.name" required />
    </div>
    <div class="form-group">
      <label for="description">Description:</label>
      <textarea id="description" v-model="item.description" required></textarea>
    </div>
    <div class="form-group">
      <label for="price">Price:</label>
      <input type="number" id="price" v-model="item.price" required />
    </div>
    <div class="form-group">
      <label for="category">Category:</label>
      <p v-if="itemStore.categoriesError"> {{ itemStore.categoriesError }}</p>
      <ToggleGroup
          v-else
          :names="itemStore.categories"
          :auto-select-first="true"
          @toggle-selected="handleCategoryClick"
          direction="row"
          :allow-deselect="false"
          id="category"
          />
    </div>
    <button type="submit">Submit</button>
  </form>
</template>

<style scoped>
.item-form {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.form-group {
  display: flex;
  flex-direction: column;
}

button {
  align-self: flex-start;
}
</style>
