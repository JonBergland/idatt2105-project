<script setup lang="ts">
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { useUserStore } from "@/stores/userStore";
import ItemForm from '@/components/NewListing/ItemForm.vue';
import type { AddItemRequest } from '@/models/item.ts';

const router = useRouter();
const userStore = useUserStore();
const isSubmitting = ref(false);

function handleSubmit(request: AddItemRequest) {
  console.log(request)
  isSubmitting.value = true;
  try {
    userStore.postItem(request);
  } catch (error) {
    console.error('Error posting item:', error);
    // Add error handling UI as needed
  } finally {
    isSubmitting.value = false;
  }
}
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
