<script setup lang="ts">
import { ref, watch } from 'vue'
import leftArrow from '@/assets/icons/image-arrow-left.svg'
import rightArrow from '@/assets/icons/image-arrow-right.svg'
import heartIcon from '@/assets/icons/heart.svg'
import heartSelectedIcon from '@/assets/icons/heart-selected.svg'

const props = defineProps<{
  images: string[] // TODO: See how best to handle images from the backend
  isFavorited?: boolean
}>()

const imageNr = ref(0)
const isFavoritedLocal = ref(props.isFavorited || false)

watch(() => props.isFavorited, (newValue) => {
  isFavoritedLocal.value = newValue || false
})

const emit = defineEmits(['favorite'])

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
  isFavoritedLocal.value = !isFavoritedLocal.value
  emit('favorite', isFavoritedLocal.value)
 }
</script>

<template>
  <div class="product-image-container">
    <button class="favorite-button" @click="toggleFavorite">
      <img :src="isFavoritedLocal ? heartSelectedIcon : heartIcon" alt="Favorite" />
    </button>

    <div class="product-image" :style="{ backgroundImage: `url(${images[imageNr]})` }"></div>

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
  width: 100%;
  aspect-ratio: 1 / 1;
  background-color: #f5f5f5;
  border-radius: 8px;
  overflow: hidden;
}

.product-image {
  width: 100%;
  height: 100%;
  background-size: cover;
  background-position: center;
}

.favorite-button {
  position: absolute;
  top: 3%;
  right: 3%;
  width: max(30px, min(60px, 6vw));
  height: max(30px, min(60px, 6vw));
  background: none;
  border: none;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
}

.nav-button {
  position: absolute;
  top: 50%;
  transform: translateY(-50%);
  width: max(30px, min(60px, 6vw));
  height: max(30px, min(60px, 6vw));
  background: none;
  border: none;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
}

.nav-button.left {
  left: 3%;
}

.nav-button.right {
  right: 3%;
}

.nav-button img,
.favorite-button img {
  width: 100%;
  height: 100%;
  /* min-width: 20px; */
  object-fit: contain;
}
</style>
