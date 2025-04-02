<script setup lang="ts">
import { onMounted, ref } from 'vue';
import "@/assets/color.css"
import "@/assets/main.css"


const props = defineProps<{
  isAvailable: boolean
  price: Number
  state: string
  comment: string
  location: string
  sellerName: string
}>()

const available = ref("")

onMounted(() => {
  available.value = props.isAvailable ? "Available" : "Not available"
})

</script>

<template>
  <div class="product-info-container">
    <h2 class="product-center-header"><strong>{{ available }}</strong></h2>
    <h2 class="product-center-header"><strong>{{ props.price }} kr</strong></h2>
    <div class="product-buttons">
      <button class="give-bid-button">Give a bid</button>
      <button class="vipps-button">Pay with vipps</button>
    </div>
    <div class="product-information-box">
      <h2><strong>Product information:</strong></h2>
      <p><strong>State:</strong> {{ props.state }}</p>
      <p><strong>Comment from seller:</strong> {{ props.comment }}</p>
      <h2><strong>Seller information:</strong></h2>
      <p><strong>Location:</strong> {{ props.location }}</p>
      <p><strong>Name of seller:</strong> {{ props.sellerName }}</p>
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
  text-align: left;
  width: 100%;
  max-width: 400px;
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
