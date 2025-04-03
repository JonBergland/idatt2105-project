<script setup lang="ts">
import { ref, onMounted } from 'vue'
import backArrowWhite from '@/assets/icons/back-arrow-white.svg'
import backArrowBlack from '@/assets/icons/back-arrow-black.svg'

defineProps<{
  productName: string
}>()

const emit = defineEmits(['backClick'])

const backArrow = ref('')

/**
 * Updates the color of the back arrow based on media preference
 */
function updateBackArrow() {
  const prefersDark = window.matchMedia('(prefers-color-scheme: dark)').matches
  backArrow.value = prefersDark ? backArrowWhite : backArrowBlack
}

onMounted(() => {
  updateBackArrow()
  const mediaQuery = window.matchMedia('(prefers-color-scheme: dark)')
  mediaQuery.addEventListener('change', updateBackArrow)
})

defineExpose({
  backArrow
})

</script>

<template>
  <div class="product-name-header">
    <img
      :src="backArrow"
      alt="Back"
      class="icon"
      @click="emit('backClick')"
    />

    <h1>{{ productName }}</h1>
  </div>
</template>

<style>
.product-name-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  width: 100%;
  position: relative;
}

.product-name-header img.icon {
  position: absolute;
  cursor: pointer;
  left: 0;
}

.product-name-header h1 {
  margin: 0 auto;
  text-align: center;
}
</style>
