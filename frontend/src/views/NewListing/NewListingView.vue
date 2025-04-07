<script setup lang="ts">
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import ItemForm from '@/components/NewListing/ItemForm.vue';
import type { AddItemRequest } from '@/models/item.ts';

const router = useRouter();

const handleSubmit = async (itemData: AddItemRequest) => {
  try {

    router.back();
  } catch (error) {
    console.error('Failed to create item:', error);
  }
};
</script>

<template>
  <div class="create-item-container">
    <h1>Create New Listing</h1>
    <ItemForm @submit="handleSubmit" />
  </div>
</template>


<style scoped>
.create-item-container {
  padding: 32px;
}
</style>
