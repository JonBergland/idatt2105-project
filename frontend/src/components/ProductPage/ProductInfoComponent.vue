<script setup lang="ts">
import { onMounted, ref } from 'vue';


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
    <h2>{{ available }}</h2>
    <h2>{{ props.price }} kr</h2>
    <div class="product-buttons">
      <button class="give-bid-button">Give a bid</button>
      <button class="vipps-button">Pay with vipps</button>
    </div>
    <div class="product-information-box">
      <h2>Product information:</h2>
      <p>State: {{ props.state }}</p>
      <p>Comment from seller: {{ props.comment }}</p>
      <h2>Seller information:</h2>
      <p>Location: {{ props.location }}</p>
      <p>Name of seller: {{ props.sellerName }}</p>
    </div>
  </div>
</template>
