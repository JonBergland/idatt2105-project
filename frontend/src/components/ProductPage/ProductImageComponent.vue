<script setup lang="ts">
import { onMounted, ref } from 'vue'
import leftArrow from '@/assets/icons/image-arrow-left.svg'
import rightArrow from '@/assets/icons/image-arrow-right.svg'
import heartIcon from '@/assets/icons/heart.svg'
import heartSelectedIcon from '@/assets/icons/heart-selected.svg'

const props = defineProps<{
  images: string[] // TODO: See how best to handle images from the backend
}>()

const imageNr = ref(0)
const isFavorited = ref(false)
const showArrows = ref(true)

/**
 * Navigate to the previous image
 */
function prevImage() {
  if (imageNr.value > 0) {
    imageNr.value--
  }
}

/**
 * Navigate to the next image
 */
function nextImage() {
  if (imageNr.value < props.images.length - 1) {
    imageNr.value++
  }
}

/**
 * Toggle the favorite state
 */
function toggleFavorite() {
  isFavorited.value = !isFavorited.value
}

onMounted(() => {
  if (props.images.length <= 1) {
    showArrows.value = false
  }
})

</script>

<template>
  <div class="product-image-container">
    <!-- Favorite Button -->
    <button class="favorite-button" @click="toggleFavorite">
      <img :src="isFavorited ? heartSelectedIcon : heartIcon" alt="Favorite" />
    </button>

    <!-- Image Display -->
    <div class="product-image" :style="{ backgroundImage: `url(${images[imageNr]})` }"></div>

    <!-- Navigation Buttons -->
    <button class="nav-button left" @click="prevImage">
      <img :src="leftArrow" alt="Previous" />
    </button>
    <button class="nav-button right" @click="nextImage">
      <img :src="rightArrow" alt="Next" />
    </button>
  </div>
</template>

<style scoped>
.product-image-container {
  position: relative;
  width: 300px;
  height: 300px;
  background-color: #f5f5f5;
  border-radius: 8px;
  overflow: hidden;
  display: flex;
  align-items: center;
  justify-content: center;
}

.product-image {
  width: 100%;
  height: 100%;
  background-size: cover;
  background-position: center;
}

.favorite-button {
  position: absolute;
  top: 10px;
  right: 10px;
  background: none;
  border: none;
  cursor: pointer;
}

.nav-button {
  position: absolute;
  top: 50%;
  transform: translateY(-50%);
  background: none;
  border: none;
  cursor: pointer;
}

.nav-button.left {
  left: 10px;
}

.nav-button.right {
  right: 10px;
}

.nav-button img,
.favorite-button img {
  width: 24px;
  height: 24px;
}
</style>
