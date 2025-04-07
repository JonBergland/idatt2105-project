<script setup lang="ts">
import { ref } from 'vue';
import type { AddItemRequest } from '@/models/item.ts';

const item = ref<AddItemRequest>({
  name: '',
  description: '',
  price: 0,
  category: ''
});

const submitForm = async () => {
  try {
    //TODO: call on user store to submit item
  } catch (error) {
    console.error('Failed to submit item:', error);
  }
};
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
      <CategorySelect v-model="item.category" />
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
