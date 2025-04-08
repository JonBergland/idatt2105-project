<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { useUserStore } from "@/stores/userStore";
import { useAuthStore } from "@/stores/authStore";
import ItemForm from '@/components/NewListing/ItemForm.vue';
import type { AddItemRequest } from '@/models/item.ts';

const router = useRouter();
const userStore = useUserStore();
const authStore = useAuthStore();
const isSubmitting = ref(false);
const errorMessage = ref('');
const successMessage = ref('');

async function handleSubmit(request: AddItemRequest) {
  console.log(request);
  isSubmitting.value = true;
  errorMessage.value = '';
  successMessage.value = '';

  try {
    const success = await userStore.postItem(request);

    if (success) {
      successMessage.value = 'Item listed successfully!';
      setTimeout(() => {
        router.push({ name: 'home' });
      }, 1500);
    } else {
      errorMessage.value = 'Failed to post item. Please try again.';
    }
  } catch (error) {
    console.error('Error posting item:', error);
    errorMessage.value = error instanceof Error
      ? error.message
      : 'An unexpected error occurred. Please try again.';
  } finally {
    isSubmitting.value = false;
  }
}

onMounted(async () => {
  if (!authStore.isAuth) {
    try {
      const isAuthenticated = await authStore.checkIfAuth();
      if (!isAuthenticated) {
        router.push({ name: 'login' })
        return;
      }
    } catch (error) {
      console.error("Error checking authentication:", error);
      router.push({ name: 'login' })
      return;
    }
  }
});
</script>

<template>
  <div class="create-item-container">
    <h1>Create New Listing</h1>

    <div v-if="successMessage" class="success-message">
      {{ successMessage }}
    </div>

    <div v-if="errorMessage" class="error-message">
      {{ errorMessage }}
    </div>

    <ItemForm @submit="handleSubmit" :disabled="isSubmitting" />

    <div v-if="isSubmitting" class="loading-indicator">
      Creating your listing...
    </div>
  </div>
</template>


<style scoped>
.create-item-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  text-align: center;
  padding: 32px;
  gap: 16px;
}

.error-message {
  background-color: #f8d7da;
  color: #721c24;
  padding: 10px;
  border-radius: 4px;
  width: 100%;
  max-width: 600px;
  text-align: center;
}

.success-message {
  background-color: #d4edda;
  color: #155724;
  padding: 10px;
  border-radius: 4px;
  width: 100%;
  max-width: 600px;
  text-align: center;
}

.loading-indicator {
  margin-top: 10px;
  color: #6c757d;
  font-style: italic;
}
</style>
