<script setup lang="ts">
import { defineAsyncComponent } from 'vue';

const AsyncAppHeader = defineAsyncComponent(() => import('@/components/MainComponents/AppHeader.vue'));
</script>

<template>
  <Suspense>
    <template #default>
      <div class="app-container">
        <AsyncAppHeader />
        <RouterView />
      </div>
    </template>
    <template #fallback>
      <div class="loading-overlay">
        <div class="loader"></div>
      </div>
    </template>
  </Suspense>
</template>

<style>
.loading-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: white;
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.loader {
  border: 5px solid #f3f3f3;
  border-top: 5px solid var(--color-primary);
  border-radius: 50%;
  width: 50px;
  height: 50px;
  animation: spin 1s linear infinite;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}
</style>
