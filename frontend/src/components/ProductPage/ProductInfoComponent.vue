<script setup lang="ts">
import type { ItemResponseDTO } from '@/models/item';
import { onMounted, ref } from 'vue';
import "@/assets/color.css"
import "@/assets/main.css"


const props = defineProps<{
  item: ItemResponseDTO,
}>()

const itemStatus = ref("")

onMounted(() => {
  if (props.item.state === 'available') itemStatus.value = "Available";
  if (props.item.state === 'archived') itemStatus.value = "Not Available: Archived";
  if (props.item.state === 'reserved') itemStatus.value = "Reserved";
  if (props.item.state === 'sold') itemStatus.value = "Not Available: Sold";
})

</script>

<template>
  <div class="product-info-container">
    <h3 class="product-center-header"><strong>{{ itemStatus }}</strong></h3>
    <h3 class="product-center-header"><strong>{{ props.item.price }} kr</strong></h3>
    <div class="product-buttons">
      <button class="give-bid-button">Give a bid</button>
      <button class="vipps-button">Pay with vipps</button>
    </div>
    <div class="product-information-box">
      <h3><strong>Product information:</strong></h3>
      <p><strong>Published:</strong> {{ props.item.published}}</p>
      <p><strong>Category:</strong> {{ props.item.category}}</p>
      <p><strong>Description:</strong> {{ props.item.description}}</p>
      <h3><strong>Seller information:</strong></h3>
      <p><strong>Location:</strong></p>
      <p><strong>Name of seller:</strong> {{ props.item.seller }}</p>
    </div>
  </div>
</template>

<style scoped>
.product-info-container {
  display: flex;
  flex-direction: column;
  align-items: left;
  gap: 20px;
  width: 100%;
}

.product-center-header {
  margin: 0;
  text-align: center;
  position: relative;
  transform: translateX(-33%);
}

.product-buttons {
  display: flex;
  gap: 20px;
  margin-top: 10px;
}

.give-bid-button,
.vipps-button {
  display: flex;
  width: 120px;
  height: 40px;
  justify-content: center;
  align-items: center;
  border: none;
}

.give-bid-button {
  background-color: var(--color-primary);
  color: white;
}

.give-bid-button:hover {
  background-color: var(--color-primary-hover);
}

.vipps-button {
  background-color: var(--color-vipps);
  color: white;
}

.vipps-button:hover {
  background-color: var(--color-vipps-hover);
}

.product-information-box {
  display: flex;
  flex-direction: column;
  text-align: left;
  width: 100%;
  max-width: 400px;
  gap: 10px;
}

.product-information-box h2 {
  margin: 10px 0;
}

.product-information-box p {
  margin: 5px 0;
}

strong {
  font-weight: bold;
}
</style>
